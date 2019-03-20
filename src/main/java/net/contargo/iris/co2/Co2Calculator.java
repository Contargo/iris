package net.contargo.iris.co2;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.terminal.Region;

import java.math.BigDecimal;

import java.util.EnumMap;
import java.util.Map;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.terminal.Region.NIEDERRHEIN;
import static net.contargo.iris.terminal.Region.NOT_SET;
import static net.contargo.iris.terminal.Region.OBERRHEIN;
import static net.contargo.iris.terminal.Region.SCHELDE;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.UP;

import static java.util.Collections.unmodifiableMap;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2Calculator {

    private static final BigDecimal CO2_TRUCK_FULL = new BigDecimal("0.851");
    private static final BigDecimal CO2_TRUCK_EMPTY = new BigDecimal("0.713");

    private static final BigDecimal CO2_RAIL_IMPORT_DIESEL = new BigDecimal("0.48");
    private static final BigDecimal CO2_RAIL_IMPORT_ELEKTRO = new BigDecimal("0.322");
    private static final BigDecimal CO2_RAIL_EXPORT_DIESEL = new BigDecimal("0.417");
    private static final BigDecimal CO2_RAIL_EXPORT_ELEKTRO = new BigDecimal("0.279");

    private static final BigDecimal CO2_PER_HANDLING = new BigDecimal("3.6");

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

    public static BigDecimal road(Co2CalculationParams.Road params) {

        BigDecimal factor = params.getLoadingState() == FULL ? CO2_TRUCK_FULL : CO2_TRUCK_EMPTY;

        return calculate(params.getDistance(), factor);
    }


    public static BigDecimal rail(Co2CalculationParams.Rail params) {

        BigDecimal dieselFactor;
        BigDecimal electricFactor;

        Co2CalculationParams.Rail.Direction direction = params.getDirection();

        switch (direction) {
            case IMPORT:
                dieselFactor = CO2_RAIL_IMPORT_DIESEL;
                electricFactor = CO2_RAIL_IMPORT_ELEKTRO;
                break;

            case EXPORT:
                dieselFactor = CO2_RAIL_EXPORT_DIESEL;
                electricFactor = CO2_RAIL_EXPORT_ELEKTRO;
                break;

            default:
                throw new IllegalArgumentException("Illegal direction: " + direction);
        }

        Integer dieselDistance = params.getDieselDistance();
        Integer electricDistance = params.getElectricDistance();

        return calculate(dieselDistance, dieselFactor).add(calculate(electricDistance, electricFactor));
    }


    public static BigDecimal water(Co2CalculationParams.Water params) {

        BigDecimal factor = CO2_REGIONS.get(params.getRegion())
                .getFactorFor(params.getLoadingState())
                .and(params.getFlowDirection());

        return calculate(params.getDistance(), factor);
    }


    public static BigDecimal handling(Co2CalculationParams.Handling params) {

        return CO2_PER_HANDLING.multiply(valueOf(params.numberOfTerminals()));
    }


    private static BigDecimal calculate(Integer distance, BigDecimal multiplier) {

        return new BigDecimal(distance).multiply(multiplier).setScale(SCALE, UP);
    }

    /**
     * Represents the co2 multipliers for a region in a convenient format.
     */
    private static class Co2Region {

        private final Map<ContainerState, WaterCo2Factors> loadingStateDirectionCo2Map = new EnumMap<>(
                ContainerState.class);

        private Co2Region(String upFull, String upEmpty, String downFull, String downEmpty) {

            loadingStateDirectionCo2Map.put(FULL, new WaterCo2Factors(upFull, downFull));
            loadingStateDirectionCo2Map.put(EMPTY, new WaterCo2Factors(upEmpty, downEmpty));
        }

        private WaterCo2Factors getFactorFor(ContainerState containerState) {

            return loadingStateDirectionCo2Map.get(containerState);
        }

        private static class WaterCo2Factors {

            private final Map<FlowDirection, BigDecimal> co2Factors = new EnumMap<>(FlowDirection.class);

            private WaterCo2Factors(String up, String down) {

                co2Factors.put(FlowDirection.UPSTREAM, new BigDecimal(up));
                co2Factors.put(FlowDirection.DOWNSTREAM, new BigDecimal(down));
            }

            private BigDecimal and(FlowDirection flowDirection) {

                return co2Factors.get(flowDirection);
            }
        }
    }
}
