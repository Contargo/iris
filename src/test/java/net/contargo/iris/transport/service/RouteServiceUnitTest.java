package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.routing.RoutingQueryStrategy;
import net.contargo.iris.routing.RoutingQueryStrategyProvider;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static net.contargo.iris.routing.osrm.OSRMProfile.DRIVING;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.service.RouteStatus.NO_ROUTE;
import static net.contargo.iris.transport.service.RouteStatus.OK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteServiceUnitTest {

    @Mock
    private RoutingQueryStrategyProvider routingQueryStrategyProviderMock;

    @InjectMocks
    private RouteService sut;

    @Test
    public void route() {

        GeoLocation start = new GeoLocation();
        GeoLocation end = new GeoLocation();

        RoutingQueryResult routingResult = new RoutingQueryResult(200, 61299.3, 22068, new BigDecimal("60.10"),
                asList("geometry1", "geometry2"));

        RoutingQueryStrategy strategyMock = mock(RoutingQueryStrategy.class);
        when(routingQueryStrategyProviderMock.strategy()).thenReturn(strategyMock);
        when(strategyMock.route(start, end, DRIVING)).thenReturn(routingResult);

        RouteResult result = sut.route(start, end, ROAD);

        assertThat(result.getDistance(), is(62));
        assertThat(result.getDuration(), is(368));
        assertThat(result.getToll(), is(61));
        assertThat(result.getGeometries().get(0), is("geometry1"));
        assertThat(result.getGeometries().get(1), is("geometry2"));
        assertThat(result.getStatus(), is(OK));
    }


    @Test
    public void routeWithError() {

        GeoLocation start = new GeoLocation();
        GeoLocation end = new GeoLocation();

        RoutingQueryResult routingResult = new RoutingQueryResult(207, 0, 0, ZERO, null);

        RoutingQueryStrategy strategyMock = mock(RoutingQueryStrategy.class);
        when(routingQueryStrategyProviderMock.strategy()).thenReturn(strategyMock);
        when(strategyMock.route(start, end, DRIVING)).thenReturn(routingResult);

        RouteResult result = sut.route(start, end, ROAD);

        assertThat(result.getStatus(), is(NO_ROUTE));
    }
}
