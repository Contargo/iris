package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;

import static java.math.RoundingMode.UP;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartDtruckStrategy implements Co2PartStrategy {

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        int distance = routePart.getData().getDtruckDistance().setScale(0, UP).intValue();

        return Co2Calculator.road(distance, routePart.getContainerState());
    }
}
