package net.contargo.iris.co2.advice;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;

import static java.math.RoundingMode.UP;


/**
 * Co2 strategy for main run connections with route type barge-rail.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public class Co2PartBargeRailStrategy implements Co2PartStrategy {

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

            boolean fromIsTerminal = subRoutePart.getOrigin() instanceof Terminal;
            boolean toIsTerminal = subRoutePart.getDestination() instanceof Terminal;

            BigDecimal handlingEmission = Co2Calculator.handling(fromIsTerminal, toIsTerminal);

            emission = emission.add(handlingEmission);

            subRoutePart.setCo2(emission);

            totalEmission = totalEmission.add(emission);
        }

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

        int distance = subRoutePart.getBargeDieselDistance().setScale(0, UP).intValue();
        FlowDirection flowDirection = FlowDirection.from(direction);

        return Co2Calculator.water(distance, region, state, flowDirection);
    }


    private BigDecimal getEmissionForRailSubRoutePart(SubRoutePart subRoutePart, ContainerState state) {

        int railDieselDistance = subRoutePart.getRailDieselDistance().setScale(0, UP).intValue();
        int railElectricDistance = subRoutePart.getElectricDistance().setScale(0, UP).intValue();

        return Co2Calculator.rail(railDieselDistance, railElectricDistance, state);
    }
}
