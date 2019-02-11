package net.contargo.iris.transport.inclinations.client.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.inclinations.client.RoutingClient;
import net.contargo.iris.transport.inclinations.dto.Point2D;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class OsrmRoutingClient implements RoutingClient {

    private static final String url =
        "{osrmHost}/v1/driving/{lon1},{lat1};{lon2},{lat2}?overview=full&geometries=geojson&annotations=nodes";
    private static final int INDEX_LAT = 1;
    private static final int INDEX_LON = 0;

    private final RestTemplate restTemplate;
    private final String osrmHost;

    public OsrmRoutingClient(RestTemplate restTemplate, String osrmHost) {

        this.restTemplate = restTemplate;
        this.osrmHost = osrmHost;
    }

    @Override
    public List<Point2D> getPoints(GeoLocation start, GeoLocation end) {

        return restTemplate.getForObject(url, AnnotatedOsrmResponse.class, osrmHost, start.getLongitude(),
                    start.getLatitude(), end.getLongitude(), end.getLatitude())
            .getRoutes()
            .stream()
            .map(OsrmRoutingClient::mergeToPoints)
            .flatMap(List::stream)
            .collect(toList());
    }


    private static List<Point2D> mergeToPoints(AnnotatedOsrmResponse.Route route) {

        List<BigDecimal[]> coordinates = route.getGeometry().getCoordinates();

        List<Long> nodeIds = route.getLegs()
                .stream()
                .map(AnnotatedOsrmResponse.Route.Leg::getAnnotation)
                .map(AnnotatedOsrmResponse.Route.Leg.Annotation::getNodes)
                .flatMap(List::stream)
                .collect(toList());

        if (coordinates.size() != nodeIds.size()) {
            throw new IllegalStateException("Received different sizes of coordinates and nodeIds");
        }

        List<Point2D> result = new ArrayList<>();

        for (int i = 0; i < coordinates.size(); i++) {
            BigDecimal[] coordinatesContainer = coordinates.get(i);
            BigDecimal latitude = coordinatesContainer[INDEX_LAT];
            BigDecimal longitude = coordinatesContainer[INDEX_LON];
            result.add(new Point2D(latitude, longitude, nodeIds.get(i)));
        }

        return result;
    }
}
