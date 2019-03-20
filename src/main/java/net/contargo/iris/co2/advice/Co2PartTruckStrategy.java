package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;

import static net.contargo.iris.co2.advice.Co2CalculationRoadParams.truckParams;


/**
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
class Co2PartTruckStrategy implements Co2PartStrategy {

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        return Co2Calculator.road(truckParams(routePart));
    }
}
