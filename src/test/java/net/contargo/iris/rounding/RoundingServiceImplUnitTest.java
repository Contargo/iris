package net.contargo.iris.rounding;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * Unit test for implementation of RoundingServiceImpl: {@link RoundingServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class RoundingServiceImplUnitTest {

    private RoundingService roundingService = null;

    @Before
    public void setup() {

        roundingService = new RoundingServiceImpl();
    }


    @Test
    public void testRoundingDistance() {

        assertThat(new BigDecimal(3), is(roundingService.roundDistance(new BigDecimal(2.222222))));
        assertThat(new BigDecimal(2), is(roundingService.roundDistance(new BigDecimal(2.000222))));
        assertThat(new BigDecimal(2), is(roundingService.roundDistance(new BigDecimal(2.000))));
    }


    @Test
    public void testRoundingDuration() {

        assertThat(new BigDecimal(3), is(roundingService.roundDuration(new BigDecimal(2.222222))));
        assertThat(new BigDecimal(3), is(roundingService.roundDuration(new BigDecimal(2.19))));
        assertThat(new BigDecimal(2), is(roundingService.roundDuration(new BigDecimal(2.09))));
    }
}
