package net.contargo.iris.distance.service;

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
 * Unit test for {@link DistanceServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class DistanceServiceImplUnitTest {

    private DistanceService sut;

    @Mock
    private RoundingService roundingServiceMock;

    private BigDecimal value, valueRounded;

    @Before
    public void setUp() {

        value = new BigDecimal(42.2);
        valueRounded = new BigDecimal(43);

        when(roundingServiceMock.roundDistance(value)).thenReturn(valueRounded);

        sut = new DistanceServiceImpl(roundingServiceMock);
    }


    @Test
    public void getDistanceFromTruckRoute() {

        TruckRoute truckRoute = new TruckRoute(value, new BigDecimal(1.11), null);

        BigDecimal distance = sut.getDistance(truckRoute);

        assertThat(valueRounded, is(distance));
        verify(roundingServiceMock).roundDistance(value);
    }


    @Test
    public void getTollDistanceFromTruckRoute() {

        TruckRoute truckRoute = new TruckRoute(new BigDecimal(1.11), value, null);

        BigDecimal tollDistance = sut.getTollDistance(truckRoute);

        assertThat(valueRounded, is(tollDistance));
        verify(roundingServiceMock).roundDistance(value);
    }
}
