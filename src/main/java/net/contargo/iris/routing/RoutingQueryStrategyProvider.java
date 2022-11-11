package net.contargo.iris.routing;

import net.contargo.iris.routing.osrm.Osrm5QueryStrategy;

import org.slf4j.Logger;

import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;


public class RoutingQueryStrategyProvider {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

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

        RoutingQueryStrategy strategy = new Osrm5QueryStrategy(restTemplate, baseUri);

        LOG.debug("Returning a query strategy of type {} applicable endpoint URI {}",
            strategy.getClass().getSimpleName(), baseUri);

        return strategy;
    }
}
