package net.contargo.iris.route;

import org.junit.Test;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.RAIL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public class RouteComboUnitTest {

    @Test
    public void getWaterwayRouteTypes() {

        RouteType[] routeTypes = RouteCombo.WATERWAY.getRouteTypes();

        assertThat(routeTypes, arrayWithSize(1));
        assertThat(routeTypes, arrayContainingInAnyOrder(BARGE));
    }


    @Test
    public void getRailwayRouteTypes() {

        RouteType[] routeTypes = RouteCombo.RAILWAY.getRouteTypes();

        assertThat(routeTypes, arrayWithSize(1));
        assertThat(routeTypes, arrayContainingInAnyOrder(RAIL));
    }


    @Test
    public void getAllRouteTypes() {

        RouteType[] routeTypes = RouteCombo.ALL.getRouteTypes();

        assertThat(routeTypes, arrayWithSize(2));
        assertThat(routeTypes, arrayContainingInAnyOrder(BARGE, RAIL));
    }
}
