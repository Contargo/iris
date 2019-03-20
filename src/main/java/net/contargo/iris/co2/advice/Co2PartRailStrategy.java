package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;


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

        Co2CalculationParams.Rail params = new Co2CalculationRailParams(routePart);

        return Co2Calculator.rail(params);
    }
}
