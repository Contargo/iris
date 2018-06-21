package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;

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
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartTruckStrategyUnitTest {

    private Co2PartTruckStrategy sut = new Co2PartTruckStrategy();

    @Test
    public void getEmissionForRoutePartFull() {

        RoutePart routePart = new RoutePart();
        routePart.setContainerState(FULL);
        routePart.getData().setDistance(new BigDecimal("42"));

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(new BigDecimal("36.96")));
    }


    @Test
    public void getEmissionForRoutePartEmpty() {

        RoutePart routePart = new RoutePart();
        routePart.setContainerState(EMPTY);
        routePart.getData().setDistance(new BigDecimal("42"));

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(new BigDecimal("30.66")));
    }
}
