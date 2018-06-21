package net.contargo.iris.co2.advice;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.terminal.Region;

import java.math.BigDecimal;

import static java.math.RoundingMode.UP;


/**
 * Co2 strategy for main run connections with route type barge.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
class Co2PartBargeStrategy implements Co2PartStrategy {

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        RoutePartData routePartData = routePart.getData();
        Region region = routePart.findTerminal().getRegion();

        int distance = routePartData.getBargeDieselDistance().setScale(0, UP).intValue();

        FlowDirection flowDirection = FlowDirection.from(routePart.getDirection());

        return Co2Calculator.water(distance, region, routePart.getContainerState(), flowDirection);
    }
}
