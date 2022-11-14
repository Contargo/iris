package net.contargo.iris.rounding;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * Unit test for implementation of RoundingService: {@link RoundingService}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class RoundingServiceUnitTest {

    @Test
    public void testRoundingDistance() {

        assertThat(new BigDecimal(3), is(RoundingService.roundDistance(new BigDecimal(2.222222))));
        assertThat(new BigDecimal(2), is(RoundingService.roundDistance(new BigDecimal(2.000222))));
        assertThat(new BigDecimal(2), is(RoundingService.roundDistance(new BigDecimal(2.000))));
    }


    @Test
    public void testRoundingDuration() {

        assertThat(new BigDecimal(3), is(RoundingService.roundDuration(new BigDecimal(2.222222))));
        assertThat(new BigDecimal(3), is(RoundingService.roundDuration(new BigDecimal(2.19))));
        assertThat(new BigDecimal(2), is(RoundingService.roundDuration(new BigDecimal(2.09))));
    }
}
