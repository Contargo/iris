package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;

import static net.contargo.iris.co2.advice.Co2CalculationRoadParams.dTruckParams;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2PartDtruckStrategy implements Co2PartStrategy {

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart, RouteDirection routeDirection) {

        return Co2Calculator.road(dTruckParams(routePart));
    }
}
