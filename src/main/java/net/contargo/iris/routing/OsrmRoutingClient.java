package net.contargo.iris.routing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.osrm.OSRMProfile;
import net.contargo.iris.routing.osrm.Osrm5Response;

import org.slf4j.Logger;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import java.lang.invoke.MethodHandles;

import java.util.List;

import static net.contargo.iris.routing.RoutingQueryResult.STATUS_NO_ROUTE;
import static net.contargo.iris.routing.RoutingQueryResult.STATUS_OK;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;


/**
 * Interface of the osrm services.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  David Schilling - schilling@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public class OsrmRoutingClient {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public OsrmRoutingClient(RestTemplate restTemplate, String baseUrl) {

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public RoutingQueryResult route(GeoLocation start, GeoLocation destination, OSRMProfile profile) {

        try {
            Osrm5Response osrm5Response = sendQuery(start, destination, profile);

            List<String> geometries = osrm5Response.getGeometries();

            return new RoutingQueryResult(STATUS_OK, osrm5Response.getDistance().doubleValue(),
                    osrm5Response.getDuration().doubleValue(), osrm5Response.getToll(), geometries,
                    osrm5Response.getDistancesByCountry());
        } catch (HttpClientErrorException e) {
            return handleClientError(e);
        }
    }


    private Osrm5Response sendQuery(GeoLocation start, GeoLocation destination, OSRMProfile profile) {

        String uriPattern = baseUrl + "/v1/%s/%s,%s;%s,%s?overview=false&alternatives=false&steps=true";
        String uri = String.format(uriPattern, profile.toString().toLowerCase(), start.getLongitude(),
                start.getLatitude(), destination.getLongitude(), destination.getLatitude());

        LOG.debug("Sending OSRM routing query with URI {}", uri);

        return restTemplate.exchange(uri, GET, EMPTY, Osrm5Response.class).getBody();
    }


    private RoutingQueryResult handleClientError(HttpClientErrorException clientException) {

        try {
            String response = clientException.getResponseBodyAsString();
            ErrorResponse error = new ObjectMapper().readValue(response, ErrorResponse.class);

            if (error.noRoute()) {
                return new RoutingQueryResult(STATUS_NO_ROUTE, 0.0, 0.0, null, emptyList(), emptyMap());
            }

            throw clientException;
        } catch (IOException ioException) {
            throw clientException;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ErrorResponse {

        private String code;

        @JsonCreator
        public ErrorResponse(@JsonProperty("code") String code) {

            this.code = code;
        }

        boolean noRoute() {

            return "NoRoute".equals(code);
        }
    }
}