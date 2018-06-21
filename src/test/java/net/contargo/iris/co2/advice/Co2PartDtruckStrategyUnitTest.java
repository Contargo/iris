package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;

import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartDtruckStrategyUnitTest {

    private Co2PartDtruckStrategy sut = new Co2PartDtruckStrategy();

    @Test
    public void getEmissionForRoutePartFull() {

        RoutePart routePart = new RoutePart();
        routePart.setContainerState(FULL);
        routePart.getData().setDtruckDistance(new BigDecimal("420"));

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(new BigDecimal("369.6")));
    }


    @Test
    public void getEmissionForRoutePartEmpty() {

        RoutePart routePart = new RoutePart();
        routePart.setContainerState(EMPTY);
        routePart.getData().setDtruckDistance(new BigDecimal("420"));

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(new BigDecimal("306.6")));
    }
}
