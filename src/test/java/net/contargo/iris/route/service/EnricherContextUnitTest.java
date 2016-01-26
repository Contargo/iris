package net.contargo.iris.route.service;

import org.junit.Test;

import static net.contargo.iris.route.RouteDirection.EXPORT;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;


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
        assertThat(sut.getErrors().size(), is(0));
    }


    @Test
    public void buildWithRouteDirection() {

        sut = new EnricherContext.Builder().routeDirection(EXPORT).build();
        assertThat(sut.getRouteDirection(), is(EXPORT));
    }
}
