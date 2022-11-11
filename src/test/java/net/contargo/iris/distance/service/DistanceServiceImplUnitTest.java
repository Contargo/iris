package net.contargo.iris.distance.service;

import net.contargo.iris.truck.TruckRoute;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.util.Collections.singletonMap;


/**
 * Unit test for {@link DistanceServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class DistanceServiceImplUnitTest {

    private DistanceService sut;

    private BigDecimal value, valueRounded;

    @Before
    public void setUp() {

        value = new BigDecimal(42.2);
        valueRounded = new BigDecimal(43);

        sut = new DistanceServiceImpl();
    }


    @Test
    public void getDistanceFromTruckRoute() {

        TruckRoute truckRoute = new TruckRoute(value, new BigDecimal(1.11), null, singletonMap("DE", value));

        BigDecimal distance = sut.getDistance(truckRoute);

        assertThat(valueRounded, is(distance));
    }


    @Test
    public void getTollDistanceFromTruckRoute() {

        TruckRoute truckRoute = new TruckRoute(new BigDecimal(1.11), value, null,
                singletonMap("DE", new BigDecimal(1.11)));

        BigDecimal tollDistance = sut.getTollDistance(truckRoute);

        assertThat(valueRounded, is(tollDistance));
    }
}
