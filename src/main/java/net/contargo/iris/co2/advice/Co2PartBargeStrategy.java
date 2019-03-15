package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;


/**
 * Co2 strategy for main run connections with route type barge.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class Co2PartBargeStrategy implements Co2PartStrategy {

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart, RouteDirection routeDirection) {

        Co2CalculationParams.Water params = new Co2CalculationWaterParams(routePart);

        return Co2Calculator.water(params);
    }
}
