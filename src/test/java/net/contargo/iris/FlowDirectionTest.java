package net.contargo.iris;

import net.contargo.iris.route.RoutePart;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class FlowDirectionTest {

    @Test
    public void from() {

        assertThat(FlowDirection.from(RoutePart.Direction.DOWNSTREAM), is(FlowDirection.DOWNSTREAM));
        assertThat(FlowDirection.from(RoutePart.Direction.UPSTREAM), is(FlowDirection.UPSTREAM));
    }


    @Test(expected = IllegalArgumentException.class)
    public void fromNotSet() {

        FlowDirection.from(RoutePart.Direction.NOT_SET);
    }
}
