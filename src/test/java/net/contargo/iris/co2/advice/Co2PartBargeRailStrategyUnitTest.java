package net.contargo.iris.co2.advice;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.RAIL;
import static net.contargo.iris.terminal.Region.OBERRHEIN;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartBargeRailStrategyUnitTest {

    private Co2PartBargeRailStrategy sut;
    private Terminal terminal;

    @Before
    public void setUp() {

        sut = new Co2PartBargeRailStrategy(new Co2BargeRegionMap());

        terminal = new Terminal();
        terminal.setRegion(OBERRHEIN);
    }


    @Test
    public void getEmissionForRoutePart() {

        Seaport seaport = new Seaport();

        SubRoutePart subRoutePart1 = new SubRoutePart();
        subRoutePart1.setOrigin(seaport);
        subRoutePart1.setDestination(terminal);
        subRoutePart1.setBargeDieselDistance(TEN);
        subRoutePart1.setElectricDistance(ZERO);
        subRoutePart1.setRailDieselDistance(ZERO);
        subRoutePart1.setRouteType(BARGE);

        SubRoutePart subRoutePart2 = new SubRoutePart();
        subRoutePart2.setOrigin(terminal);
        subRoutePart2.setDestination(new Terminal());
        subRoutePart2.setBargeDieselDistance(ZERO);
        subRoutePart2.setElectricDistance(new BigDecimal("100"));
        subRoutePart2.setRailDieselDistance(new BigDecimal("40"));
        subRoutePart2.setRouteType(RAIL);

        RoutePart routePart = new RoutePart();
        routePart.setSubRouteParts(asList(subRoutePart1, subRoutePart2));
        routePart.setOrigin(seaport);
        routePart.setDestination(terminal);
        routePart.setContainerState(ContainerState.EMPTY);

        BigDecimal emission = sut.getEmissionForRoutePart(routePart);
        assertThat(subRoutePart1.getCo2(), comparesEqualTo(new BigDecimal(4)));
        assertThat(subRoutePart2.getCo2(), comparesEqualTo(new BigDecimal(43)));
        assertThat(emission, comparesEqualTo(new BigDecimal(55)));
    }


    @Test
    public void getEmissionForRoutePartReverse() {

        Seaport seaport = new Seaport();

        SubRoutePart subRoutePart1 = new SubRoutePart();
        subRoutePart1.setOrigin(terminal);
        subRoutePart1.setDestination(seaport);
        subRoutePart1.setBargeDieselDistance(new BigDecimal("100"));
        subRoutePart1.setElectricDistance(ZERO);
        subRoutePart1.setRailDieselDistance(ZERO);
        subRoutePart1.setRouteType(BARGE);

        SubRoutePart subRoutePart2 = new SubRoutePart();
        subRoutePart2.setOrigin(new Terminal());
        subRoutePart2.setDestination(terminal);
        subRoutePart2.setBargeDieselDistance(ZERO);
        subRoutePart2.setElectricDistance(new BigDecimal("100"));
        subRoutePart2.setRailDieselDistance(new BigDecimal("34"));
        subRoutePart2.setRouteType(RAIL);

        RoutePart routePart = new RoutePart();
        routePart.setSubRouteParts(asList(subRoutePart2, subRoutePart1));
        routePart.setOrigin(seaport);
        routePart.setDestination(terminal);
        routePart.setContainerState(ContainerState.FULL);

        BigDecimal emission = sut.getEmissionForRoutePart(routePart);
        assertThat(subRoutePart2.getCo2(), comparesEqualTo(new BigDecimal(51)));
        assertThat(subRoutePart1.getCo2(), comparesEqualTo(new BigDecimal(43)));
        assertThat(emission, comparesEqualTo(new BigDecimal(102)));
    }
}
