package net.contargo.iris.duration.service;

import net.contargo.iris.truck.TruckRoute;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.ONE;

import static java.util.Collections.singletonMap;


/**
 * Unit test of {@link DurationServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class DurationServiceImplUnitTest {

    private DurationService sut;

    private BigDecimal value, valueRounded;

    @Before
    public void setUp() {

        value = new BigDecimal(42.2);
        valueRounded = new BigDecimal(43);

        sut = new DurationServiceImpl();
    }


    @Test
    public void getDurationFromTruckRoute() {

        TruckRoute truckRoute = new TruckRoute(ONE, BigDecimal.TEN, value, singletonMap("DE", ONE));

        BigDecimal duration = sut.getDuration(truckRoute);

        assertThat(valueRounded, is(duration));
    }
}
