package net.contargo.iris.transport.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.OsrmRoutingClient;
import net.contargo.iris.routing.RoutingQueryResult;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static net.contargo.iris.routing.osrm.OSRMProfile.DRIVING;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.route.RouteStatus.NO_ROUTE;
import static net.contargo.iris.transport.route.RouteStatus.OK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

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
    private OsrmRoutingClient osrmRoutingClient;

    @InjectMocks
    private RouteService sut;

    @Test
    public void route() {

        GeoLocation start = new GeoLocation();
        GeoLocation end = new GeoLocation();

        Map<String, Double> distancesByCountry = new HashMap<>();
        distancesByCountry.put("DE", 60000.0);
        distancesByCountry.put("FR", 1299.3);

        RoutingQueryResult routingResult = new RoutingQueryResult(200, 61299.3, 22068, new BigDecimal("60.10"),
                asList("geometry1", "geometry2"), distancesByCountry);

        when(osrmRoutingClient.route(start, end, DRIVING)).thenReturn(routingResult);

        RouteResult result = sut.route(start, end, ROAD);

        assertThat(result.getDistance(), is(62));
        assertThat(result.getDuration(), is(368));
        assertThat(result.getToll(), is(61));
        assertThat(result.getGeometries().get(0), is("geometry1"));
        assertThat(result.getGeometries().get(1), is("geometry2"));
        assertThat(result.getDistancesByCountry().keySet(), hasSize(2));
        assertThat(result.getDistancesByCountry().get("DE"), is(60));
        assertThat(result.getDistancesByCountry().get("FR"), is(2));
        assertThat(result.getStatus(), is(OK));
    }


    @Test
    public void routeWithError() {

        GeoLocation start = new GeoLocation();
        GeoLocation end = new GeoLocation();
        Map<String, Double> distancesByCountry = Collections.emptyMap();
        RoutingQueryResult routingResult = new RoutingQueryResult(207, 0, 0, ZERO, null, distancesByCountry);

        when(osrmRoutingClient.route(start, end, DRIVING)).thenReturn(routingResult);

        RouteResult result = sut.route(start, end, ROAD);

        assertThat(result.getStatus(), is(NO_ROUTE));
    }
}
