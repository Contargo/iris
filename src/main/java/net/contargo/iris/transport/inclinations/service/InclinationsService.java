package net.contargo.iris.transport.inclinations.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.inclinations.client.ElevationProviderClient;
import net.contargo.iris.transport.inclinations.client.RoutingClient;
import net.contargo.iris.transport.inclinations.dto.Point2D;
import net.contargo.iris.transport.inclinations.dto.Point3D;
import net.contargo.iris.transport.inclinations.smoothing.ElevationSmoother;

import org.springframework.core.convert.ConversionService;

import java.util.List;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.inclinations.service.Inclinations.zero;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class InclinationsService {

    private final ElevationProviderClient elevationServiceClient;
    private final RoutingClient routingClient;
    private final ElevationSmoother elevationSmoother;
    private final ConversionService conversionService;

    public InclinationsService(ElevationProviderClient elevationServiceClient, RoutingClient routingClient,
        ElevationSmoother elevationSmoother, ConversionService conversionService) {

        this.elevationServiceClient = elevationServiceClient;
        this.routingClient = routingClient;
        this.elevationSmoother = elevationSmoother;
        this.conversionService = conversionService;
    }

    /**
     * Returns the total positive and negative inclinations of all segments having mode of transport ROAD.
     *
     * @param  description  the transport description
     *
     * @return  the calculated inclinations
     *
     * @see  Inclinations
     */
    public Inclinations get(TransportDescriptionDto description) {

        return description.transportChain.stream()
            .filter(t -> t.modeOfTransport == ROAD)
            .map(this::getInclinationsFor)
            .reduce(zero(), Inclinations::add);
    }


    private Inclinations getInclinationsFor(TransportDescriptionDto.TransportDescriptionSegment segment) {

        GeoLocation start = conversionService.convert(segment.from, GeoLocation.class);
        GeoLocation end = conversionService.convert(segment.to, GeoLocation.class);

        List<Point2D> points2D = routingClient.getPoints(start, end);
        List<Point3D> points3D = elevationServiceClient.getElevations(points2D);

        List<Point3D> smoothedPoints = elevationSmoother.smooth(points3D);

        return mapToInclinations(smoothedPoints);
    }


    private Inclinations mapToInclinations(List<Point3D> points) {

        Inclinations result = zero();

        for (int i = 1; i < points.size(); i++) {
            int deltaElevation = points.get(i).getElevation() - points.get(i - 1).getElevation();
            result = result.with(deltaElevation);
        }

        return result;
    }
}
