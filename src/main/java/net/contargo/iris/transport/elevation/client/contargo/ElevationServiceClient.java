package net.contargo.iris.transport.elevation.client.contargo;

import net.contargo.iris.transport.elevation.client.ElevationProviderClient;
import net.contargo.iris.transport.elevation.dto.Point2D;
import net.contargo.iris.transport.elevation.dto.Point3D;

import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class ElevationServiceClient implements ElevationProviderClient {

    private static final String URL = "{elevationServiceHost}/elevation";

    private final RestTemplate restTemplate;
    private final String elevationServiceHost;

    public ElevationServiceClient(RestTemplate restTemplate, String elevationServiceHost) {

        this.restTemplate = restTemplate;
        this.elevationServiceHost = elevationServiceHost;
    }

    @Override
    public List<Point3D> getElevations(List<Point2D> points) {

        return stream(post(points)) //
            .map(ElevationServiceClient::toPoint3D)
            .collect(toList());
    }


    private ElevationServicePoint3D[] post(List<Point2D> points) {

        return restTemplate.postForObject(URL, toElevationServicePoints(points), ElevationServicePoint3D[].class,
                elevationServiceHost);
    }


    private List<ElevationServicePoint2D> toElevationServicePoints(List<Point2D> points) {

        return points.stream()
            .map(p -> new ElevationServicePoint2D(p.getLatitude(), p.getLongitude(), p.getOsmId()))
            .collect(toList());
    }


    private static Point3D toPoint3D(ElevationServicePoint3D point) {

        return new Point3D(point.getElevation(), point.getLatitude(), point.getLongitude());
    }
}
