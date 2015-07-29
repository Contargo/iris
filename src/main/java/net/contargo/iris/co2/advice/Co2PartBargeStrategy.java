package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;


/**
 * Co2 strategy for main run connections with route type barge.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
class Co2PartBargeStrategy implements Co2PartStrategy {

    private final Co2BargeRegionMap co2BargeRegionMap;

    public Co2PartBargeStrategy(Co2BargeRegionMap co2BargeRegionMap) {

        this.co2BargeRegionMap = co2BargeRegionMap;
    }

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        RoutePartData routePartData = routePart.getData();
        Terminal terminal = routePart.findTerminal();

        BigDecimal distance1 = routePartData.getBargeDieselDistance();

        BigDecimal co2Factor = co2BargeRegionMap.getCo2Factor(terminal.getRegion(), routePart.getDirection(),
                routePart.getContainerState());

        return distance1.multiply(co2Factor);
    }
}
