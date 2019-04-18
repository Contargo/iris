package net.contargo.iris.transport.elevation;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportStop;
import net.contargo.iris.transport.elevation.client.ElevationProviderClient;
import net.contargo.iris.transport.elevation.client.RoutingClient;
import net.contargo.iris.transport.elevation.dto.ElevationPoint;
import net.contargo.iris.transport.elevation.dto.Point3D;
import net.contargo.iris.transport.elevation.smoothing.DistanceCalculationUtil;
import net.contargo.iris.transport.elevation.smoothing.ElevationSmoother;

import org.springframework.core.convert.ConversionService;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * @author  Petra Scherer - scherer@synyx.de
 */

public class ElevationsService {

    private final ElevationProviderClient elevationServiceClient;
    private final RoutingClient routingClient;
    private final ElevationSmoother elevationSmoother;
    private final ConversionService conversionService;

    public ElevationsService(ElevationProviderClient elevationServiceClient, RoutingClient routingClient,
        ElevationSmoother elevationSmoother, ConversionService conversionService) {

        this.elevationServiceClient = elevationServiceClient;
        this.routingClient = routingClient;
        this.elevationSmoother = elevationSmoother;
        this.conversionService = conversionService;
    }

    public Elevations getElevationsFor(TransportDescriptionDto description) {

        List<Point3D> smoothedPoints = description.transportChain.stream()
                .map(s -> routingClient.getPoints(toGeolocation(s.from), toGeolocation(s.to)))
                .map(elevationServiceClient::getElevations)
                .map(elevationSmoother::smooth)
                .flatMap(List::stream)
                .collect(toList());

        return new Elevations(transformSmoothedPointsToElevationPoints(smoothedPoints));
    }


    private GeoLocation toGeolocation(TransportStop stop) {

        return conversionService.convert(stop, GeoLocation.class);
    }


    private List<ElevationPoint> transformSmoothedPointsToElevationPoints(List<Point3D> smoothedPoints) {

        List<ElevationPoint> elevationPoints = new ArrayList<>();

        elevationPoints.add(new ElevationPoint(smoothedPoints.get(0).getElevation(), 0));

        for (int i = 1; i < smoothedPoints.size(); i++) {
            double fromLat = smoothedPoints.get(i - 1).getLatitude().doubleValue();
            double fromLon = smoothedPoints.get(i - 1).getLongitude().doubleValue();
            double toLat = smoothedPoints.get(i).getLatitude().doubleValue();
            double toLon = smoothedPoints.get(i).getLongitude().doubleValue();

            double dist = DistanceCalculationUtil.calculateDistance(fromLat, fromLon, toLat, toLon);
            double summedDist = elevationPoints.get(i - 1).getSummedDistance() + dist;

            elevationPoints.add(new ElevationPoint(smoothedPoints.get(i).getElevation(),
                    (int) Math.round(summedDist)));
        }

        return elevationPoints;
    }
}
