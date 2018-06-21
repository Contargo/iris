package net.contargo.iris.co2;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.terminal.Region;

import java.math.BigDecimal;

import java.util.EnumMap;
import java.util.Map;

import static net.contargo.iris.FlowDirection.DOWNSTREAM;
import static net.contargo.iris.FlowDirection.UPSTREAM;
import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.terminal.Region.NIEDERRHEIN;
import static net.contargo.iris.terminal.Region.NOT_SET;
import static net.contargo.iris.terminal.Region.OBERRHEIN;
import static net.contargo.iris.terminal.Region.SCHELDE;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.UP;

import static java.util.Collections.unmodifiableMap;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2Calculator {

    private static final BigDecimal CO2_TRUCK_FULL = new BigDecimal("0.88");
    private static final BigDecimal CO2_TRUCK_EMPTY = new BigDecimal("0.73");

    private static final BigDecimal CO2_RAIL_FULL_DIESEL = new BigDecimal("0.5");
    private static final BigDecimal CO2_RAIL_EMPTY_DIESEL = new BigDecimal("0.4");
    private static final BigDecimal CO2_RAIL_FULL_ELEKTRO = new BigDecimal("0.34");
    private static final BigDecimal CO2_RAIL_EMPTY_ELEKTRO = new BigDecimal("0.27");

    private static final BigDecimal CO2_PER_HANDLING = new BigDecimal("4");

    private static final Map<Region, Co2Region> CO2_REGIONS = unmodifiableMap(new EnumMap<Region, Co2Region>(
                Region.class) {

                {
                    put(NIEDERRHEIN, new Co2Region("0.31", "0.27", "0.17", "0.14"));
                    put(NOT_SET, new Co2Region("0.31", "0.27", "0.17", "0.14"));
                    put(OBERRHEIN, new Co2Region("0.43", "0.4", "0.23", "0.21"));
                    put(SCHELDE, new Co2Region("0.427", "0.375", "0.427", "0.375"));
                }
            });

    private static final int SCALE = 2;

    private Co2Calculator() {
    }

    public static BigDecimal road(Integer distance, ContainerState loadingState) {

        BigDecimal multiplier = loadingState == FULL ? CO2_TRUCK_FULL : CO2_TRUCK_EMPTY;

        return calculate(distance, multiplier);
    }


    public static BigDecimal rail(Integer dieselDistance, Integer electricDistance, ContainerState loadingState) {

        BigDecimal multiplierDiesel;
        BigDecimal multiplierElectric;

        if (loadingState == FULL) {
            multiplierDiesel = CO2_RAIL_FULL_DIESEL;
            multiplierElectric = CO2_RAIL_FULL_ELEKTRO;
        } else if (loadingState == EMPTY) {
            multiplierDiesel = CO2_RAIL_EMPTY_DIESEL;
            multiplierElectric = CO2_RAIL_EMPTY_ELEKTRO;
        } else {
            throw new IllegalArgumentException("Unknown loading state: " + loadingState);
        }

        return calculate(dieselDistance, multiplierDiesel).add(calculate(electricDistance, multiplierElectric));
    }


    public static BigDecimal water(Integer distance, Region region, ContainerState loadingState,
        FlowDirection flowDirection) {

        BigDecimal multiplier = CO2_REGIONS.get(region).getMultiplierFor(loadingState).and(flowDirection);

        return calculate(distance, multiplier);
    }


    public static BigDecimal handling(boolean fromIsTerminal, boolean toIsTerminal) {

        BigDecimal result = ZERO;

        if (fromIsTerminal) {
            result = result.add(CO2_PER_HANDLING);
        }

        if (toIsTerminal) {
            result = result.add(CO2_PER_HANDLING);
        }

        return result;
    }


    private static BigDecimal calculate(Integer distance, BigDecimal multiplier) {

        return new BigDecimal(distance).multiply(multiplier).setScale(SCALE, UP);
    }

    /**
     * Represents the co2 multipliers for a region in a convenient format.
     */
    private static class Co2Region {

        private final Map<ContainerState, DirectionCo2> loadingStateDirectionCo2Map = new EnumMap<>(
                ContainerState.class);

        Co2Region(String upFull, String upEmpty, String downFull, String downEmpty) {

            loadingStateDirectionCo2Map.put(FULL, new DirectionCo2(upFull, downFull));
            loadingStateDirectionCo2Map.put(EMPTY, new DirectionCo2(upEmpty, downEmpty));
        }

        DirectionCo2 getMultiplierFor(ContainerState containerState) {

            return loadingStateDirectionCo2Map.get(containerState);
        }

        private static class DirectionCo2 {

            private final Map<FlowDirection, BigDecimal> directionCo2Map = new EnumMap<>(FlowDirection.class);

            DirectionCo2(String up, String down) {

                directionCo2Map.put(UPSTREAM, new BigDecimal(up));
                directionCo2Map.put(DOWNSTREAM, new BigDecimal(down));
            }

            public BigDecimal and(FlowDirection direction) {

                return directionCo2Map.get(direction);
            }
        }
    }
}
