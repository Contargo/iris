package net.contargo.iris.routing.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.RoutingQueryStrategyProvider;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.OSRMNonRoutableRouteException;
import net.contargo.iris.truck.service.OSRMTruckRouteService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.closeTo;


/**
 * Integration test of {@link net.contargo.iris.truck.service.OSRMTruckRouteService}.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
public class OSRMTruckRouteServiceIntegrationTest {

    @Autowired
    private RoutingQueryStrategyProvider provider;

    private OSRMTruckRouteService OSRMTruckRouteService;

    @Before
    public void setUp() {

        OSRMTruckRouteService = new OSRMTruckRouteService(provider);
    }


    @Test
    public void testRoute() {

        TruckRoute truckRoute = OSRMTruckRouteService.route(new GeoLocation(new BigDecimal(49.015),
                    new BigDecimal(8.42)), new GeoLocation(new BigDecimal(50D), new BigDecimal(10D)));

        assertThat(truckRoute.getDistance(), closeTo(new BigDecimal("234.72400"), BigDecimal.TEN));
        assertThat(truckRoute.getDuration(), closeTo(new BigDecimal("189.30000"), BigDecimal.TEN));
    }


    @Test(expected = OSRMNonRoutableRouteException.class)
    public void testNonRoutableRoute() {

        GeoLocation start = new GeoLocation(new BigDecimal(51.013755), new BigDecimal(59.941406));
        GeoLocation destination = new GeoLocation(new BigDecimal(34.813803), new BigDecimal(-34.101563));

        OSRMTruckRouteService.route(start, destination);
    }
}
