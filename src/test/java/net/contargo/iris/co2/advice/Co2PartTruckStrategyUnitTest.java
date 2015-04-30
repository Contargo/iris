package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;


/**
 * Unit test of {@link Co2PartTruckStrategy}.
 *
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class Co2PartTruckStrategyUnitTest {

    private Co2PartTruckStrategy sut;

    private BigDecimal distance;
    private RoutePart routePart;

    @Before
    public void before() {

        routePart = new RoutePart();
        distance = new BigDecimal("42");

        sut = new Co2PartTruckStrategy();
    }


    @Test
    public void getEmissionForRoutePartFull() {

        routePart.setContainerState(FULL);
        routePart.getData().setDistance(distance);

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(BigDecimal.valueOf(distance.doubleValue() * 0.88)));
    }


    @Test
    public void getEmissionForRoutePartEmpty() {

        routePart.setContainerState(EMPTY);
        routePart.getData().setDistance(distance);

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(BigDecimal.valueOf(distance.doubleValue() * 0.73)));
    }
}
