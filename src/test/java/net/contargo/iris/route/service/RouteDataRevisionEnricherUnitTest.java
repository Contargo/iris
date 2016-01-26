package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;

import org.junit.Before;
import org.junit.Test;

import static net.contargo.iris.route.RouteType.RAIL;
import static net.contargo.iris.route.RouteType.TRUCK;

import static org.hamcrest.Matchers.hasKey;

import static org.hamcrest.core.IsNot.not;

import static org.junit.Assert.assertThat;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * Unit test of {@link RouteDataRevisionEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class RouteDataRevisionEnricherUnitTest {

    private RouteDataRevisionEnricher sut;

    @Before
    public void setUp() {

        sut = new RouteDataRevisionEnricher();
    }


    @Test
    public void enrich() throws CriticalEnricherException {

        Route route = new Route();
        RouteData routeData = new RouteData();
        routeData.setParts(asList(new RoutePart(new GeoLocation(), new GeoLocation(), TRUCK),
                new RoutePart(new GeoLocation(), new GeoLocation(), TRUCK)));
        route.setData(routeData);

        EnricherContext context = new EnricherContext.Builder().build();
        context.addError("swiss-route", "error");

        sut.enrich(route, context);

        assertThat(route.getErrors(), not(hasKey("swiss-route")));
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichNoneDirectTruck() throws CriticalEnricherException {

        Route route = new Route();
        RouteData routeData = new RouteData();
        routeData.setParts(singletonList(new RoutePart(new GeoLocation(), new GeoLocation(), RAIL)));
        route.setData(routeData);

        EnricherContext context = new EnricherContext.Builder().build();
        context.addError("swiss-route", "error");

        sut.enrich(route, context);
    }
}
