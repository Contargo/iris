package net.contargo.iris.co2.advice;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.FULL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;


/**
 * Unit test of {@link Co2PartRailStrategy}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2PartRailStrategyUnitTest {

    private Co2PartRailStrategy sut;

    private RoutePart routePart;
    private BigDecimal dieselDistance;
    private BigDecimal electricDistance;

    @Before
    public void before() {

        dieselDistance = new BigDecimal(2);
        electricDistance = new BigDecimal(3);

        routePart = new RoutePart();

        sut = new Co2PartRailStrategy();
    }


    @Test
    public void getEmissionForRoutePartFull() {

        routePart.setContainerState(FULL);
        routePart.getData().setRailDieselDistance(dieselDistance);
        routePart.getData().setElectricDistance(electricDistance);

        BigDecimal diesel = BigDecimal.valueOf(dieselDistance.doubleValue() * 0.5);
        BigDecimal electronic = BigDecimal.valueOf(electricDistance.doubleValue() * 0.34);
        BigDecimal expectedEmission = diesel.add(electronic);

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, equalTo(expectedEmission));
    }


    @Test
    public void getEmissionForRoutePartEmpty() {

        routePart.setContainerState(ContainerState.EMPTY);
        routePart.getData().setRailDieselDistance(dieselDistance);
        routePart.getData().setElectricDistance(electricDistance);

        BigDecimal diesel = BigDecimal.valueOf(dieselDistance.doubleValue() * 0.4);
        BigDecimal electronic = BigDecimal.valueOf(electricDistance.doubleValue() * 0.27);
        BigDecimal expectedEmission = diesel.add(electronic);

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);
        assertThat(co2, comparesEqualTo(expectedEmission));
    }
}
