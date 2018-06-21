package net.contargo.iris.co2;

import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.FlowDirection.DOWNSTREAM;
import static net.contargo.iris.FlowDirection.UPSTREAM;
import static net.contargo.iris.co2.Co2Calculator.handling;
import static net.contargo.iris.co2.Co2Calculator.rail;
import static net.contargo.iris.co2.Co2Calculator.road;
import static net.contargo.iris.co2.Co2Calculator.water;
import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.terminal.Region.NIEDERRHEIN;
import static net.contargo.iris.terminal.Region.NOT_SET;
import static net.contargo.iris.terminal.Region.OBERRHEIN;
import static net.contargo.iris.terminal.Region.SCHELDE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2CalculatorUnitTest {

    private static final int DISTANCE = 100;

    @Test
    public void testRoad() {

        assertThat(road(DISTANCE, FULL), comparesEqualTo(new BigDecimal("88.00")));
        assertThat(road(DISTANCE, EMPTY), comparesEqualTo(new BigDecimal("73")));
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


    @Test
    public void testHandling() {

        assertThat(handling(true, true), comparesEqualTo(new BigDecimal("8.00")));
        assertThat(handling(true, false), comparesEqualTo(new BigDecimal("4.00")));
        assertThat(handling(false, true), comparesEqualTo(new BigDecimal("4.00")));
        assertThat(handling(false, false), comparesEqualTo(new BigDecimal("0.00")));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testRailNullLoadingState() {

        rail(DISTANCE, DISTANCE, null);
    }
}
