package net.contargo.iris.co2.advice;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.transport.service.FlowDirection;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.terminal.Region.NIEDERRHEIN;
import static net.contargo.iris.terminal.Region.NOT_SET;
import static net.contargo.iris.terminal.Region.OBERRHEIN;
import static net.contargo.iris.terminal.Region.SCHELDE;
import static net.contargo.iris.transport.service.FlowDirection.DOWNSTREAM;
import static net.contargo.iris.transport.service.FlowDirection.UPSTREAM;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;

import static java.math.RoundingMode.UP;


/**
 * This test is a validation for {@link net.contargo.iris.transport.service.Co2CalculatorUnitTest}.
 *
 * @author  Ben Antony - antony@synyx.de
 */
public class Co2PartStrategyUnitTest {

    private static final int DISTANCE = 100;

    private Co2PartTruckStrategy co2PartTruckStrategy;

    private Co2PartRailStrategy co2PartRailStrategy;
    private Co2PartBargeStrategy co2PartBargeStrategy;

    @Before
    public void setUp() {

        co2PartTruckStrategy = new Co2PartTruckStrategy();

        co2PartRailStrategy = new Co2PartRailStrategy();

        Co2BargeRegionMap co2RegionMap = new Co2BargeRegionMap();
        co2PartBargeStrategy = new Co2PartBargeStrategy(co2RegionMap);
    }


    @Test
    public void testTruck() {

        assertThat(truck(DISTANCE, FULL), comparesEqualTo(new BigDecimal("88.00")));
        assertThat(truck(DISTANCE, EMPTY), comparesEqualTo(new BigDecimal("73")));
    }


    @Test
    public void testRail() {

        assertThat(rail(DISTANCE, DISTANCE, FULL), comparesEqualTo(new BigDecimal("84.00")));
        assertThat(rail(DISTANCE, DISTANCE, EMPTY), comparesEqualTo(new BigDecimal("67.00")));
    }


    @Test
    public void testWater() {

        assertThat(water(DISTANCE, NIEDERRHEIN, FULL, UPSTREAM), comparesEqualTo(new BigDecimal("31.00")));
        assertThat(water(DISTANCE, NIEDERRHEIN, EMPTY, UPSTREAM), comparesEqualTo(new BigDecimal("27.00")));
        assertThat(water(DISTANCE, NIEDERRHEIN, FULL, DOWNSTREAM), comparesEqualTo(new BigDecimal("17.00")));
        assertThat(water(DISTANCE, NIEDERRHEIN, EMPTY, DOWNSTREAM), comparesEqualTo(new BigDecimal("14.00")));

        assertThat(water(DISTANCE, OBERRHEIN, FULL, UPSTREAM), comparesEqualTo(new BigDecimal("43.00")));
        assertThat(water(DISTANCE, OBERRHEIN, EMPTY, UPSTREAM), comparesEqualTo(new BigDecimal("40.00")));
        assertThat(water(DISTANCE, OBERRHEIN, FULL, DOWNSTREAM), comparesEqualTo(new BigDecimal("23.00")));
        assertThat(water(DISTANCE, OBERRHEIN, EMPTY, DOWNSTREAM), comparesEqualTo(new BigDecimal("21.00")));

        assertThat(water(DISTANCE, SCHELDE, FULL, UPSTREAM), comparesEqualTo(new BigDecimal("42.70")));
        assertThat(water(DISTANCE, SCHELDE, EMPTY, UPSTREAM), comparesEqualTo(new BigDecimal("37.50")));
        assertThat(water(DISTANCE, SCHELDE, FULL, DOWNSTREAM), comparesEqualTo(new BigDecimal("42.70")));
        assertThat(water(DISTANCE, SCHELDE, EMPTY, DOWNSTREAM), comparesEqualTo(new BigDecimal("37.50")));

        assertThat(water(DISTANCE, NOT_SET, FULL, UPSTREAM), comparesEqualTo(new BigDecimal("31.00")));
        assertThat(water(DISTANCE, NOT_SET, EMPTY, UPSTREAM), comparesEqualTo(new BigDecimal("27.00")));
        assertThat(water(DISTANCE, NOT_SET, FULL, DOWNSTREAM), comparesEqualTo(new BigDecimal("17.00")));
        assertThat(water(DISTANCE, NOT_SET, EMPTY, DOWNSTREAM), comparesEqualTo(new BigDecimal("14.00")));
    }


    private BigDecimal truck(int distance, ContainerState containerState) {

        RoutePartData data = new RoutePartData();
        data.setDistance(new BigDecimal(distance));

        RoutePart routePart = new RoutePart();
        routePart.setData(data);
        routePart.setContainerState(containerState);

        BigDecimal emissionForRoutePart = co2PartTruckStrategy.getEmissionForRoutePart(routePart);

        return round(emissionForRoutePart);
    }


    private BigDecimal rail(int dieselDistance, int electroDistance, ContainerState containerState) {

        RoutePartData data = new RoutePartData();

        data.setElectricDistance(new BigDecimal(electroDistance));
        data.setRailDieselDistance(new BigDecimal(dieselDistance));

        RoutePart routePart = new RoutePart();
        routePart.setData(data);
        routePart.setContainerState(containerState);

        return round(co2PartRailStrategy.getEmissionForRoutePart(routePart));
    }


    private BigDecimal water(int distance, Region region, ContainerState containerState, FlowDirection flowDirection) {

        Terminal terminal = new Terminal();
        terminal.setRegion(region);

        Seaport seaport = new Seaport();

        RoutePartData data = new RoutePartData();
        data.setBargeDieselDistance(new BigDecimal(distance));

        RoutePart routePart;

        if (flowDirection == DOWNSTREAM) {
            routePart = new RoutePart(BARGE, terminal, seaport, null, containerState);
        } else {
            routePart = new RoutePart(BARGE, seaport, terminal, null, containerState);
        }

        routePart.setData(data);

        return round(co2PartBargeStrategy.getEmissionForRoutePart(routePart));
    }


    private static BigDecimal round(BigDecimal co2Value) {

        return co2Value.setScale(2, UP);
    }
}
