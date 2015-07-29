package net.contargo.iris.co2.advice;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;


/**
 * Co2 strategy for main run connections with route type barge-rail.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartBargeRailStrategy implements Co2PartStrategy {

    private static final BigDecimal CO2_RAIL_FULL_DIESEL = BigDecimal.valueOf(0.5);
    private static final BigDecimal CO2_RAIL_EMPTY_DIESEL = BigDecimal.valueOf(0.4);
    private static final BigDecimal CO2_RAIL_FULL_ELEKTRO = BigDecimal.valueOf(0.34);
    private static final BigDecimal CO2_RAIL_EMPTY_ELEKTRO = BigDecimal.valueOf(0.27);
    private static final BigDecimal CO2_HANDLING = BigDecimal.valueOf(8);

    private final Co2BargeRegionMap co2BargeRegionMap;

    public Co2PartBargeRailStrategy(Co2BargeRegionMap co2BargeRegionMap) {

        this.co2BargeRegionMap = co2BargeRegionMap;
    }

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        RoutePart.Direction direction = routePart.getDirection();

        BigDecimal totalEmission = BigDecimal.ZERO;

        for (SubRoutePart subRoutePart : routePart.getSubRouteParts()) {
            BigDecimal emission;

            if (subRoutePart.getRouteType() == RouteType.BARGE) {
                emission = getEmissionForBargeSubRoutePart(subRoutePart, direction, routePart.getContainerState());
            } else {
                emission = getEmissionForRailSubRoutePart(subRoutePart, routePart.getContainerState());
            }

            subRoutePart.setCo2(emission);
            totalEmission = totalEmission.add(emission);
        }

        totalEmission = totalEmission.add(CO2_HANDLING.multiply(
                    new BigDecimal(routePart.getSubRouteParts().size() - 1)));

        return totalEmission;
    }


    private BigDecimal getEmissionForBargeSubRoutePart(SubRoutePart subRoutePart, RoutePart.Direction direction,
        ContainerState state) {

        Region region;

        if (subRoutePart.getOrigin() instanceof Terminal) {
            region = ((Terminal) subRoutePart.getOrigin()).getRegion();
        } else {
            region = ((Terminal) subRoutePart.getDestination()).getRegion();
        }

        BigDecimal distance = subRoutePart.getBargeDieselDistance();

        BigDecimal co2Factor = co2BargeRegionMap.getCo2Factor(region, direction, state);

        return distance.multiply(co2Factor);
    }


    private BigDecimal getEmissionForRailSubRoutePart(SubRoutePart subRoutePart, ContainerState state) {

        BigDecimal co2 = BigDecimal.ZERO;

        BigDecimal railDieselDistance = subRoutePart.getRailDieselDistance();
        BigDecimal railElectricDistance = subRoutePart.getElectricDistance();

        BigDecimal co2DieselFactor;
        BigDecimal co2ElectricFactor;

        if (ContainerState.FULL == state) {
            co2DieselFactor = CO2_RAIL_FULL_DIESEL;
            co2ElectricFactor = CO2_RAIL_FULL_ELEKTRO;
        } else {
            co2DieselFactor = CO2_RAIL_EMPTY_DIESEL;
            co2ElectricFactor = CO2_RAIL_EMPTY_ELEKTRO;
        }

        co2 = co2.add(railDieselDistance.multiply(co2DieselFactor));
        co2 = co2.add(railElectricDistance.multiply(co2ElectricFactor));

        subRoutePart.setCo2(co2);

        return co2;
    }
}
