package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;

import static java.math.RoundingMode.UP;


/**
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
class Co2PartTruckStrategy implements Co2PartStrategy {

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        int distance = routePart.getData().getDistance().setScale(0, UP).intValue();

        return Co2Calculator.road(distance, routePart.getContainerState());
    }
}
