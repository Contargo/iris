package net.contargo.iris.route;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteComboUnitTest {

    @Test
    public void getWaterwayRouteTypes() {

        RouteType[] routeTypes = RouteCombo.WATERWAY.getRouteTypes();

        assertThat(routeTypes, arrayWithSize(1));
        assertThat(routeTypes, arrayContainingInAnyOrder(RouteType.BARGE));
    }


    @Test
    public void getRailwayRouteTypes() {

        RouteType[] routeTypes = RouteCombo.RAILWAY.getRouteTypes();

        assertThat(routeTypes, arrayWithSize(1));
        assertThat(routeTypes, arrayContainingInAnyOrder(RouteType.RAIL));
    }


    @Test
    public void getWaterwayRailRouteTypes() {

        RouteType[] routeTypes = RouteCombo.WATERWAY_RAIL.getRouteTypes();

        assertThat(routeTypes, arrayWithSize(1));
        assertThat(routeTypes, arrayContainingInAnyOrder(RouteType.BARGE_RAIL));
    }


    @Test
    public void getAllRouteTypes() {

        RouteType[] routeTypes = RouteCombo.ALL.getRouteTypes();

        assertThat(routeTypes, arrayWithSize(3));
        assertThat(routeTypes, arrayContainingInAnyOrder(RouteType.BARGE, RouteType.RAIL, RouteType.BARGE_RAIL));
    }
}
