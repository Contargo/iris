package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;

import java.math.BigDecimal;

import static java.math.RoundingMode.UP;


/**
 * Co2 strategy for main run connections with route type rail.
 *
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
class Co2PartRailStrategy implements Co2PartStrategy {

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        RoutePartData routePartData = routePart.getData();

        int dieselDistance = routePartData.getRailDieselDistance().setScale(0, UP).intValue();
        int electricDistance = routePartData.getElectricDistance().setScale(0, UP).intValue();

        return Co2Calculator.rail(dieselDistance, electricDistance, routePart.getContainerState());
    }
}
