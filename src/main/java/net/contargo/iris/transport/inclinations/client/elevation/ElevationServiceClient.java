package net.contargo.iris.transport.inclinations.client.elevation;

import net.contargo.iris.transport.inclinations.client.InclinationsClient;
import net.contargo.iris.transport.inclinations.dto.Point2D;
import net.contargo.iris.transport.inclinations.dto.Point3D;

import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class ElevationServiceClient implements InclinationsClient {

    private static final String URL = "{mapsHost}/elevation";
    private final RestTemplate restTemplate;
    private final String mapsHost;

    public ElevationServiceClient(RestTemplate restTemplate, String mapsHost) {

        this.restTemplate = restTemplate;
        this.mapsHost = mapsHost;
    }

    @Override
    public List<Point3D> getElevations(List<Point2D> points) {

        return stream(restTemplate.postForObject(URL, toElevationServicePoints(points),
                        ElevationServicePoint3D[].class, mapsHost)).map(p ->
                    new Point3D(p.getElevation(), p.getLatitude(), p.getLongitude())).collect(toList());
    }


    private List<ElevationServicePoint2D> toElevationServicePoints(List<Point2D> points) {

        return points.stream()
            .map(p -> new ElevationServicePoint2D(p.getLatitude(), p.getLongitude(), p.getOsmId()))
            .collect(toList());
    }
}
