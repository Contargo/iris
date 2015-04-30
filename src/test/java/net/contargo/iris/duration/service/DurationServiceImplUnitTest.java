package net.contargo.iris.duration.service;

import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.truck.TruckRoute;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit test of {@link DurationServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class DurationServiceImplUnitTest {

    private DurationService sut;

    @Mock
    private RoundingService roundingServiceMock;

    private BigDecimal value, valueRounded;

    @Before
    public void setUp() {

        value = new BigDecimal(42.2);
        valueRounded = new BigDecimal(43);

        when(roundingServiceMock.roundDuration(value)).thenReturn(valueRounded);

        sut = new DurationServiceImpl(roundingServiceMock);
    }


    @Test
    public void getDurationFromTruckRoute() {

        TruckRoute truckRoute = new TruckRoute(BigDecimal.ONE, BigDecimal.TEN, value);

        BigDecimal duration = sut.getDuration(truckRoute);

        assertThat(valueRounded, is(duration));
        verify(roundingServiceMock).roundDuration(value);
    }
}
