package net.contargo.iris.routing.osrm;

import net.contargo.iris.routing.RoutingQueryStrategy;
import net.contargo.iris.routing.RoutingQueryStrategyProvider;

import org.junit.Test;

import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.IsInstanceOf.instanceOf;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;


public class RoutingQueryStrategyProviderUnitTest {

    private RestTemplate restTemplateMock = mock(RestTemplate.class);

    private RoutingQueryStrategyProvider sut;

    @Test
    public void returnsOsrm4QueryStrategy() {

        sut = new RoutingQueryStrategyProvider(restTemplateMock, "http://localhost/osrm/viaroute");

        RoutingQueryStrategy strategy = sut.strategy();
        assertThat(strategy, instanceOf(Osrm4QueryStrategy.class));
    }


    @Test
    public void returnsOsrm5QueryStrategy() {

        sut = new RoutingQueryStrategyProvider(restTemplateMock, "http://localhost/osrm/route");

        RoutingQueryStrategy strategy = sut.strategy();
        assertThat(strategy, instanceOf(Osrm5QueryStrategy.class));
    }


    @Test(expected = IllegalArgumentException.class)
    public void test() {

        sut = new RoutingQueryStrategyProvider(restTemplateMock, "http://localhost/osrm/not-supported");

        sut.strategy();
    }
}
