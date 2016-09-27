package net.contargo.iris.osrm.service;

import net.contargo.iris.GeoLocation;

import org.springframework.web.client.RestTemplate;


/**
 * An OSRM specific implementation of {@code RoutingServiceQueryStrategy} suitable for querying an OSRM version 5
 * routing service.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class Osrm5QueryStrategy implements RoutingQueryStrategy {

    private final String baseUrl;
    private final RestTemplate osrmRestClient;

    public Osrm5QueryStrategy(RestTemplate osrmRestClient, String baseUrl) {

        this.osrmRestClient = osrmRestClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public RoutingQueryResult route(GeoLocation start, GeoLocation destination) {

        throw new UnsupportedOperationException();
    }
}
