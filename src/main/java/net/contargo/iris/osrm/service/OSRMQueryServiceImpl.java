package net.contargo.iris.osrm.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.util.HttpUtilException;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;


/**
 * Implementation of the OSRM query service.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class OSRMQueryServiceImpl implements OSRMQueryService {

    private static final int STATUS_NO_ROUTE = 207;

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

    public OSRMQueryServiceImpl(RestTemplate osrmRestClient, String baseUrl) {

        this.osrmRestClient = osrmRestClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public OSRMQueryResult getOSRMXmlRoute(GeoLocation start, GeoLocation destination) {

        try {
            return createOSRMQueryResult(route(start, destination));
        } catch (HttpUtilException | IOException e) {
            throw new RoutingException("Error querying OSRM service: ", e);
        }
    }


    private OSRMJsonResponse route(GeoLocation start, GeoLocation destination) throws IOException {

        return osrmRestClient.getForEntity(createOSRMQueryString(start, destination), OSRMJsonResponse.class).getBody();
    }


    private String createOSRMQueryString(GeoLocation start, GeoLocation destination) {

        return baseUrl + Q_QUESTIONMARK + Q_LOCATION + Q_EQUALS + start.getLatitude() + Q_COMMA + start.getLongitude()
            + Q_AMPERSAND + Q_LOCATION + Q_EQUALS + destination.getLatitude() + Q_COMMA + destination.getLongitude()
            + Q_AMPERSAND + Q_ZOOM + Q_EQUALS + 0 + Q_AMPERSAND + Q_GEOMETRY + Q_EQUALS + false + Q_AMPERSAND
            + Q_INSTRUCTIONS + Q_EQUALS + true + Q_AMPERSAND + Q_ALTERNATIVE + Q_EQUALS + false;
    }


    private OSRMQueryResult createOSRMQueryResult(OSRMJsonResponse response) {

        double totalDistance = 0d;
        double totalTime = 0d;
        int status = response.getStatus();

        if (status != STATUS_NO_ROUTE) {
            totalDistance = response.getRoute_summary().getTotal_distance();
            totalTime = response.getRoute_summary().getTotalTime();
        }

        return new OSRMQueryResult(status, totalDistance, totalTime, response.getRoute_instructions());
    }
}
