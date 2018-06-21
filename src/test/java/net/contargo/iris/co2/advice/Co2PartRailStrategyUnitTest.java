package net.contargo.iris.co2.advice;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;

import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.FULL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;


/**
 * Unit test of {@link Co2PartRailStrategy}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartRailStrategyUnitTest {

    private Co2PartRailStrategy sut = new Co2PartRailStrategy();

    @Test
    public void getEmissionForRoutePartFull() {

        RoutePart routePart = new RoutePart();
        routePart.setContainerState(FULL);
        routePart.getData().setRailDieselDistance(new BigDecimal("105"));
        routePart.getData().setElectricDistance(new BigDecimal("530"));

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);

        assertThat(co2, comparesEqualTo(new BigDecimal("232.7")));
    }


    @Test
    public void getEmissionForRoutePartEmpty() {

        RoutePart routePart = new RoutePart();
        routePart.setContainerState(ContainerState.EMPTY);
        routePart.getData().setRailDieselDistance(new BigDecimal("105"));
        routePart.getData().setElectricDistance(new BigDecimal("530"));

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(new BigDecimal("185.1")));
    }
}
