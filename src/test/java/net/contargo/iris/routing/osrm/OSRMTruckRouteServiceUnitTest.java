package net.contargo.iris.routing.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.OsrmRoutingClient;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.OSRMNonRoutableRouteException;
import net.contargo.iris.truck.service.OSRMTruckRouteService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static net.contargo.iris.routing.osrm.OSRMProfile.DRIVING;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.hamcrest.Matchers.hasSize;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;


/**
 * Unit test of {@link net.contargo.iris.truck.service.OSRMTruckRouteService}.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class OSRMTruckRouteServiceUnitTest {

    private static final double DEFAULT_TOTAL_DISTANCE = 1.1;
    private static final double DEFAULT_TOTAL_TIME = 2.2;

    private OSRMTruckRouteService sut;

    @Mock
    private OsrmRoutingClient osrmRoutingClient;

    private GeoLocation start;
    private GeoLocation destination;

    @Before
    public void setup() {

        sut = new OSRMTruckRouteService(osrmRoutingClient);

        destination = new GeoLocation(new BigDecimal("49.1"), new BigDecimal("8.1"));
        start = new GeoLocation(new BigDecimal("49.0"), new BigDecimal("8.0"));
    }


    @Test(expected = OSRMNonRoutableRouteException.class)
    public void routeIsNotRoutable() {

        when(osrmRoutingClient.route(start, destination, DRIVING)).thenReturn(new RoutingQueryResult(207, 1.1, 2.2,
                TEN, emptyList(), emptyMap()));

        sut.route(start, destination);
    }


    @Test
    public void delegatesCallToQueryService() {

        makeMockReturn(DEFAULT_TOTAL_DISTANCE, DEFAULT_TOTAL_TIME);

        TruckRoute route = sut.route(start, destination);
        assertThat(route, notNullValue());
        verify(osrmRoutingClient).route(start, destination, DRIVING);
    }


    @Test
    public void parsesSectionsAndReturnsDistanceInMeters() {

        makeMockReturn(12000, DEFAULT_TOTAL_TIME);

        TruckRoute route = sut.route(start, destination);
        assertThat(route, notNullValue());
        assertThat(route.getDistance(), equalTo(new BigDecimal("12.00000")));
    }


    @Test
    public void parsesSectionsAndReturnsDistancesByCountryInMeters() {

        makeMockReturn(12000, DEFAULT_TOTAL_TIME);

        TruckRoute route = sut.route(start, destination);
        assertThat(route, notNullValue());
        assertThat(route.getDistancesByCountry().keySet(), hasSize(1));
        assertThat(route.getDistancesByCountry().get("DE"), equalTo(new BigDecimal("12.00000")));
    }


    @Test
    public void parsesSectionsAndReturnsTimeInMinutes() {

        makeMockReturn(DEFAULT_TOTAL_DISTANCE, 120);

        TruckRoute route = sut.route(start, destination);
        assertThat(route, notNullValue());
        assertThat(route.getDuration(), equalTo(new BigDecimal("2.00000")));
    }


    private void makeMockReturn(double totalDistance, double totalTime) {

        RoutingQueryResult response = new RoutingQueryResult(0, totalDistance, totalTime, ZERO,
                singletonList("geometries"), singletonMap("DE", totalDistance));

        when(osrmRoutingClient.route(start, destination, DRIVING)).thenReturn(response);
    }
}
