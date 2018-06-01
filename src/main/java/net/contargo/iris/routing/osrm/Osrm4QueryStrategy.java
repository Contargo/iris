package net.contargo.iris.routing.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.RoutingException;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.routing.RoutingQueryStrategy;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static net.contargo.iris.routing.RoutingQueryResult.STATUS_NO_ROUTE;


/**
 * An OSRM specific implementation of {@code RoutingServiceQueryStrategy} suitable for querying an OSRM version 4
 * routing service.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
public class Osrm4QueryStrategy implements RoutingQueryStrategy {

    private static final String Q_QUESTIONMARK = "?";
    private static final String Q_EQUALS = "=";
    private static final String Q_AMPERSAND = "&";
    private static final String Q_COMMA = ",";

    private static final String Q_LOCATION = "loc";
    private static final String Q_ZOOM = "z";
    private static final String Q_GEOMETRY = "geometry";
    private static final String Q_INSTRUCTIONS = "instructions";
    private static final String Q_ALTERNATIVE = "alt";

    private final String baseUrl;
    private final RestTemplate osrmRestClient;

    public Osrm4QueryStrategy(RestTemplate osrmRestClient, String baseUrl) {

        this.osrmRestClient = osrmRestClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public RoutingQueryResult route(GeoLocation start, GeoLocation destination) {

        try {
            return createOSRMQueryResult(routeRequest(start, destination));
        } catch (RestClientException e) {
            throw new RoutingException("Error querying OSRM service: ", e);
        }
    }


    @Override
    public RoutingQueryResult route(GeoLocation start, GeoLocation destination, OSRMProfile profile) {

        throw new UnsupportedOperationException("Osrm4QueryStrategy does not support profiles");
    }


    private OSRM4Response routeRequest(GeoLocation start, GeoLocation destination) {

        return osrmRestClient.getForEntity(createOSRMQueryString(start, destination), OSRM4Response.class).getBody();
    }


    private String createOSRMQueryString(GeoLocation start, GeoLocation destination) {

        return baseUrl + Q_QUESTIONMARK + Q_LOCATION + Q_EQUALS + start.getLatitude() + Q_COMMA + start.getLongitude()
            + Q_AMPERSAND + Q_LOCATION + Q_EQUALS + destination.getLatitude() + Q_COMMA + destination.getLongitude()
            + Q_AMPERSAND + Q_ZOOM + Q_EQUALS + 0 + Q_AMPERSAND + Q_GEOMETRY + Q_EQUALS + false + Q_AMPERSAND
            + Q_INSTRUCTIONS + Q_EQUALS + true + Q_AMPERSAND + Q_ALTERNATIVE + Q_EQUALS + false;
    }


    private RoutingQueryResult createOSRMQueryResult(OSRM4Response response) {

        double totalDistance = 0d;
        double totalTime = 0d;
        int status = response.getStatus();

        if (status != STATUS_NO_ROUTE) {
            totalDistance = response.getRoute_summary().getTotal_distance();
            totalTime = response.getRoute_summary().getTotalTime();
        }

        return new RoutingQueryResult(status, totalDistance, totalTime, response.getToll());
    }
}
