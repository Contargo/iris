package net.contargo.iris.co2;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.terminal.Region;

import java.math.BigDecimal;

import java.util.EnumMap;
import java.util.Map;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.terminal.Region.MAIN;
import static net.contargo.iris.terminal.Region.MITTELRHEIN;
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
 * @author  Bjoern Martin - martin@synyx.de
 */
public class Co2Calculator {

    private static final BigDecimal CO2_TRUCK_FULL = new BigDecimal("1.085");
    private static final BigDecimal CO2_TRUCK_EMPTY = new BigDecimal("0.836");
    private static final BigDecimal CO2_DTRUCK_FULL = new BigDecimal("1.136");
    private static final BigDecimal CO2_DTRUCK_EMPTY = new BigDecimal("0.855");

    private static final BigDecimal CO2_RAIL_IMPORT_DIESEL = new BigDecimal("0.394");
    private static final BigDecimal CO2_RAIL_IMPORT_ELEKTRO = new BigDecimal("0.12");
    private static final BigDecimal CO2_RAIL_EXPORT_DIESEL = new BigDecimal("0.394");
    private static final BigDecimal CO2_RAIL_EXPORT_ELEKTRO = new BigDecimal("0.12");

    // NOTE: reported to us as "Umschlagpauschale" which assumes the 3 handlings of a standard one-way transport
    // hence we divide that "Umschlagpauschale" by 3 to get the CO2 value of a single handling again
    private static final BigDecimal CO2_PER_HANDLING = new BigDecimal("2.927");

    private static final Map<Region, Co2Region> CO2_REGIONS;

    private static final int SCALE = 2;

    static {
        EnumMap<Region, Co2Region> co2Regions = new EnumMap<>(Region.class);

        co2Regions.put(MAIN, new Co2Region("0.862", "0.348", "0.441", "0.195"));
        co2Regions.put(MITTELRHEIN, new Co2Region("0.67", "0.277", "0.362", "0.144"));
        co2Regions.put(NIEDERRHEIN, new Co2Region("0.415", "0.27", "0.276", "0.1"));
        co2Regions.put(NOT_SET, new Co2Region("0.31", "0.27", "0.17", "0.14"));
        co2Regions.put(OBERRHEIN, new Co2Region("0.475", "0.162", "0.218", "0.11"));
        co2Regions.put(SCHELDE, new Co2Region("0.759", "0.108", "0.36", "0.35"));

        CO2_REGIONS = unmodifiableMap(co2Regions);
    }

    private Co2Calculator() {
    }

    public static BigDecimal road(Co2CalculationParams.Road params) {

        BigDecimal factor;

        if (params.isDirectTruck()) {
            factor = params.getLoadingState() == FULL ? CO2_DTRUCK_FULL : CO2_DTRUCK_EMPTY;
        } else {
            factor = params.getLoadingState() == FULL ? CO2_TRUCK_FULL : CO2_TRUCK_EMPTY;
        }

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
