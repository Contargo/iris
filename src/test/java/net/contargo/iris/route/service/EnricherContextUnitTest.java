package net.contargo.iris.route.service;

import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RouteType;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link net.contargo.iris.route.service.EnricherContext}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class EnricherContextUnitTest {

    private EnricherContext sut;

    @Test
    public void buildPlain() {

        sut = new EnricherContext.Builder().build();
        assertThat(sut.getRouteDirection(), nullValue());
        assertThat(sut.getRouteTypes(), nullValue());
        assertThat(sut.getErrors().size(), is(0));
    }


    @Test
    public void buildWithRouteDirection() {

        sut = new EnricherContext.Builder().routeDirection(RouteDirection.EXPORT).build();
        assertThat(sut.getRouteDirection(), is(RouteDirection.EXPORT));
    }


    @Test
    public void buildWithRouteTypes() {

        sut = new EnricherContext.Builder().routeTypes(asList(RouteType.BARGE, RouteType.RAIL)).build();
        assertThat(sut.getRouteTypes(), hasItem(RouteType.BARGE));
        assertThat(sut.getRouteTypes(), hasItem(RouteType.RAIL));
        assertThat(sut.getRouteTypes(), hasSize(2));
    }
}
