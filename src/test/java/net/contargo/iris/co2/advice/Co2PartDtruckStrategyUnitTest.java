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
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartDtruckStrategyUnitTest {

    private Co2PartDtruckStrategy sut;

    private BigDecimal distance;
    private RoutePart routePart;

    @Before
    public void before() {

        routePart = new RoutePart();
        distance = new BigDecimal("42");

        sut = new Co2PartDtruckStrategy();
    }


    @Test
    public void getEmissionForRoutePartFull() {

        routePart.setContainerState(FULL);
        routePart.getData().setDtruckDistance(distance);

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(new BigDecimal("36.96")));
    }


    @Test
    public void getEmissionForRoutePartEmpty() {

        routePart.setContainerState(EMPTY);
        routePart.getData().setDtruckDistance(distance);

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(new BigDecimal("30.66")));
    }
}
