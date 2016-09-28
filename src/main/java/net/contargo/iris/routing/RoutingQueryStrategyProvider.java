package net.contargo.iris.routing;

import net.contargo.iris.routing.osrm.Osrm4QueryStrategy;
import net.contargo.iris.routing.osrm.Osrm5QueryStrategy;

import org.slf4j.Logger;

import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Provides an OSRM {@code RoutingServiceQueryStrategy} applicable for a specific host based on the given logical host
 * name (e.g. "maps1") and a corresponding host specific endpoint URI (e.g. "http://maps1.contargo.net/osrm/viaroute")
 * as defined in the application configuration. If the configured endpoint has its path set to
 * {@link #OSRM_4_ENDPOINT_PATH}, the returned strategy is of type {@link Osrm4QueryStrategy} and if the path is equal
 * to {@code OSRM_5_ENDPOINT_PATH}, a strategy of type {@link Osrm5QueryStrategy} is returned.
 *
 * @author  Oliver Messner - messner@synyx.de
 * @author  David Schilling - schilling@synyx.de
 * @see  RoutingQueryStrategy
 */
public class RoutingQueryStrategyProvider {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final String OSRM_4_ENDPOINT_PATH = "/viaroute";
    private static final String OSRM_5_ENDPOINT_PATH = "/route";

    private final RestTemplate restTemplate;
    private final String baseUri;

    public RoutingQueryStrategyProvider(RestTemplate restTemplate, String baseUri) {

        this.restTemplate = restTemplate;
        this.baseUri = baseUri;
    }

    /**
     * Returns an OSRM {@code RoutingServiceQueryStrategy} implementation that is applicable for the application
     * configuration.
     *
     * @return  a {@code RoutingQueryStrategy}
     *
     * @throws  IllegalArgumentException  if there is no endpoint URI configured for the given logical host name or no
     *                                    matching strategy is available for the configured endpoint URI
     */
    public RoutingQueryStrategy strategy() {

        boolean osrm4Endpoint = baseUri.endsWith(OSRM_4_ENDPOINT_PATH);
        boolean osrm5Endpoint = baseUri.endsWith(OSRM_5_ENDPOINT_PATH);

        if (!osrm4Endpoint && !osrm5Endpoint) {
            throw new IllegalArgumentException("No strategy found applicable for endpoint URI " + baseUri);
        }

        RoutingQueryStrategy strategy = osrm4Endpoint ? new Osrm4QueryStrategy(restTemplate, baseUri)
                                                      : new Osrm5QueryStrategy(restTemplate, baseUri);

        LOG.debug("Returning a query strategy of type {} applicable endpoint URI {}",
            strategy.getClass().getSimpleName(), baseUri);

        return strategy;
    }
}
