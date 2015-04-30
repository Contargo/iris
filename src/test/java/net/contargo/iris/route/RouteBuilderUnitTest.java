package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


/**
 * Unit test for {@link net.contargo.iris.route.RouteBuilder}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class RouteBuilderUnitTest {

    private RouteBuilder routeBuilder;

    @Test
    public void testNewRoute() {

        routeBuilder = new RouteBuilder(new GeoLocation(new BigDecimal(42.0), new BigDecimal(8.3)), ContainerType.FORTY,
                ContainerState.FULL);

        assertThat(routeBuilder.getRoute(), notNullValue());
    }


    @Test
    public void testSettingRouteAttributes() {

        GeoLocation loc = new GeoLocation(new BigDecimal(42.0), new BigDecimal(8.3));
        GeoLocation otherLoc = new GeoLocation(new BigDecimal(42.01), new BigDecimal(8.19));

        routeBuilder = new RouteBuilder(loc, ContainerType.FORTY, ContainerState.FULL);
        routeBuilder.goTo(otherLoc, RouteType.TRUCK);

        Route route = routeBuilder.getRoute();
        assertThat(route.getData().getParts(), hasSize(1));

        RoutePart part = route.getData().getParts().get(0);
        assertThat(part.getContainerType(), is(ContainerType.FORTY));
        assertThat(part.getContainerState(), is(ContainerState.FULL));
        assertThat(part.getOrigin(), is(loc));
        assertThat(part.getDestination(), is(otherLoc));
    }


    @Test
    public void testUnloadContainer() {

        GeoLocation one = new GeoLocation(new BigDecimal(42.0), new BigDecimal(8.3));
        GeoLocation two = new GeoLocation(new BigDecimal(42.01), new BigDecimal(8.19));
        GeoLocation three = new GeoLocation(new BigDecimal(40.87), new BigDecimal(7.9));

        routeBuilder = new RouteBuilder(one, ContainerType.FORTY, ContainerState.FULL);
        routeBuilder.goTo(two, RouteType.BARGE);
        routeBuilder.unloadContainer();
        routeBuilder.goTo(three, RouteType.TRUCK);

        Route route = routeBuilder.getRoute();
        assertThat(route.getData().getParts(), hasSize(2));

        // show that route is from location one to location two and container is full
        RoutePart part = route.getData().getParts().get(0);
        assertThat(part.getContainerType(), is(ContainerType.FORTY));
        assertThat(part.getContainerState(), is(ContainerState.FULL));

        assertThat(part.getOrigin(), is(one));
        assertThat(part.getDestination(), is(two));

        // show that route is from location two to location three and container is empty
        RoutePart part2 = route.getData().getParts().get(1);
        assertThat(part2.getContainerType(), is(ContainerType.FORTY));
        assertThat(part2.getContainerState(), is(ContainerState.EMPTY));
        assertThat(part2.getOrigin(), is(two));
        assertThat(part2.getDestination(), is(three));
    }
}
