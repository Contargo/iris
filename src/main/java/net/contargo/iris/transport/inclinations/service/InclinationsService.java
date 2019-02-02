package net.contargo.iris.transport.inclinations.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.inclinations.client.InclinationsClient;
import net.contargo.iris.transport.inclinations.client.InclinationsRoutingClient;
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

    private final InclinationsClient elevationServiceClient;
    private final InclinationsRoutingClient inclinationsRoutingClient;
    private final ElevationSmoother elevationSmoother;
    private final ConversionService conversionService;

    public InclinationsService(InclinationsClient elevationServiceClient,
        InclinationsRoutingClient inclinationsRoutingClient, ElevationSmoother elevationSmoother,
        ConversionService conversionService) {

        this.elevationServiceClient = elevationServiceClient;
        this.inclinationsRoutingClient = inclinationsRoutingClient;
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

        List<Point2D> points2D = inclinationsRoutingClient.getPoints(start, end);
        List<Point3D> points3D = elevationServiceClient.getElevations(points2D);

        // smooth the list of 3D points before calculating inclinations
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
