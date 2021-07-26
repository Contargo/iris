package net.contargo.iris.co2;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.terminal.Region;

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
import static net.contargo.iris.terminal.Region.MAIN;
import static net.contargo.iris.terminal.Region.MITTELRHEIN;
import static net.contargo.iris.terminal.Region.NIEDERRHEIN;
import static net.contargo.iris.terminal.Region.NOT_SET;
import static net.contargo.iris.terminal.Region.OBERRHEIN;
import static net.contargo.iris.terminal.Region.SCHELDE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Bjoern Martin - martin@synyx.de
 */
public class Co2CalculatorUnitTest {

    private static final int DISTANCE = 100;

    @Test
    public void testTruck() {

        assertThat(road(new RoadParams(FULL, false)), comparesEqualTo(new BigDecimal("108.50")));
        assertThat(road(new RoadParams(EMPTY, false)), comparesEqualTo(new BigDecimal("83.60")));
    }


    @Test
    public void testDTruck() {

        assertThat(road(new RoadParams(FULL, true)), comparesEqualTo(new BigDecimal("113.60")));
        assertThat(road(new RoadParams(EMPTY, true)), comparesEqualTo(new BigDecimal("85.50")));
    }


    @Test
    public void testRail() {

        assertThat(rail(new RailParams(Co2CalculationParams.Rail.Direction.IMPORT)),
            comparesEqualTo(new BigDecimal("51.40")));

        assertThat(rail(new RailParams(Co2CalculationParams.Rail.Direction.EXPORT)),
            comparesEqualTo(new BigDecimal("51.40")));
    }


    @Test
    public void testWater() {

        assertThat(water(new WaterParams(MAIN, FULL, UPSTREAM)), comparesEqualTo(new BigDecimal("86.20")));
        assertThat(water(new WaterParams(MAIN, EMPTY, UPSTREAM)), comparesEqualTo(new BigDecimal("34.80")));
        assertThat(water(new WaterParams(MAIN, FULL, DOWNSTREAM)), comparesEqualTo(new BigDecimal("44.10")));
        assertThat(water(new WaterParams(MAIN, EMPTY, DOWNSTREAM)), comparesEqualTo(new BigDecimal("19.50")));

        assertThat(water(new WaterParams(MITTELRHEIN, FULL, UPSTREAM)), comparesEqualTo(new BigDecimal("67.00")));
        assertThat(water(new WaterParams(MITTELRHEIN, EMPTY, UPSTREAM)), comparesEqualTo(new BigDecimal("27.70")));
        assertThat(water(new WaterParams(MITTELRHEIN, FULL, DOWNSTREAM)), comparesEqualTo(new BigDecimal("36.20")));
        assertThat(water(new WaterParams(MITTELRHEIN, EMPTY, DOWNSTREAM)), comparesEqualTo(new BigDecimal("14.40")));

        assertThat(water(new WaterParams(NIEDERRHEIN, FULL, UPSTREAM)), comparesEqualTo(new BigDecimal("41.50")));
        assertThat(water(new WaterParams(NIEDERRHEIN, EMPTY, UPSTREAM)), comparesEqualTo(new BigDecimal("27.00")));
        assertThat(water(new WaterParams(NIEDERRHEIN, FULL, DOWNSTREAM)), comparesEqualTo(new BigDecimal("27.60")));
        assertThat(water(new WaterParams(NIEDERRHEIN, EMPTY, DOWNSTREAM)), comparesEqualTo(new BigDecimal("10.00")));

        assertThat(water(new WaterParams(OBERRHEIN, FULL, UPSTREAM)), comparesEqualTo(new BigDecimal("47.50")));
        assertThat(water(new WaterParams(OBERRHEIN, EMPTY, UPSTREAM)), comparesEqualTo(new BigDecimal("16.20")));
        assertThat(water(new WaterParams(OBERRHEIN, FULL, DOWNSTREAM)), comparesEqualTo(new BigDecimal("21.80")));
        assertThat(water(new WaterParams(OBERRHEIN, EMPTY, DOWNSTREAM)), comparesEqualTo(new BigDecimal("11.00")));

        assertThat(water(new WaterParams(SCHELDE, FULL, UPSTREAM)), comparesEqualTo(new BigDecimal("75.90")));
        assertThat(water(new WaterParams(SCHELDE, EMPTY, UPSTREAM)), comparesEqualTo(new BigDecimal("10.80")));
        assertThat(water(new WaterParams(SCHELDE, FULL, DOWNSTREAM)), comparesEqualTo(new BigDecimal("36.00")));
        assertThat(water(new WaterParams(SCHELDE, EMPTY, DOWNSTREAM)), comparesEqualTo(new BigDecimal("35.00")));

        assertThat(water(new WaterParams(NOT_SET, FULL, UPSTREAM)), comparesEqualTo(new BigDecimal("31.00")));
        assertThat(water(new WaterParams(NOT_SET, EMPTY, UPSTREAM)), comparesEqualTo(new BigDecimal("27.00")));
        assertThat(water(new WaterParams(NOT_SET, FULL, DOWNSTREAM)), comparesEqualTo(new BigDecimal("17.00")));
        assertThat(water(new WaterParams(NOT_SET, EMPTY, DOWNSTREAM)), comparesEqualTo(new BigDecimal("14.00")));
    }


    @Test
    public void testHandling() {

        assertThat(handling(new HandlingParams(0)), comparesEqualTo(new BigDecimal("0")));
        assertThat(handling(new HandlingParams(1)), comparesEqualTo(new BigDecimal("2.927")));
        assertThat(handling(new HandlingParams(2)), comparesEqualTo(new BigDecimal("5.854")));
    }

    private static class RoadParams implements Co2CalculationParams.Road {

        private final ContainerState loadingState;
        private final boolean directTruck;

        private RoadParams(ContainerState loadingState, boolean directTruck) {

            this.loadingState = loadingState;
            this.directTruck = directTruck;
        }

        @Override
        public int getDistance() {

            return DISTANCE;
        }


        @Override
        public ContainerState getLoadingState() {

            return loadingState;
        }


        @Override
        public boolean isDirectTruck() {

            return directTruck;
        }
    }

    private static class RailParams implements Co2CalculationParams.Rail {

        private final Direction direction;

        private RailParams(Direction direction) {

            this.direction = direction;
        }

        @Override
        public int getDieselDistance() {

            return DISTANCE;
        }


        @Override
        public int getElectricDistance() {

            return DISTANCE;
        }


        @Override
        public Direction getDirection() {

            return direction;
        }
    }

    private static class WaterParams implements Co2CalculationParams.Water {

        private final Region region;
        private final ContainerState loadingState;
        private final FlowDirection flowDirection;

        private WaterParams(Region region, ContainerState loadingState, FlowDirection flowDirection) {

            this.region = region;
            this.loadingState = loadingState;
            this.flowDirection = flowDirection;
        }

        @Override
        public int getDistance() {

            return DISTANCE;
        }


        @Override
        public Region getRegion() {

            return region;
        }


        @Override
        public ContainerState getLoadingState() {

            return loadingState;
        }


        @Override
        public FlowDirection getFlowDirection() {

            return flowDirection;
        }
    }

    private static class HandlingParams implements Co2CalculationParams.Handling {

        private final int numberOfTerminals;

        private HandlingParams(int numberOfTerminals) {

            this.numberOfTerminals = numberOfTerminals;
        }

        @Override
        public int numberOfTerminals() {

            return numberOfTerminals;
        }
    }
}
