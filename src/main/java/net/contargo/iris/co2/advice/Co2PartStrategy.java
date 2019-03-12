package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public interface Co2PartStrategy {

    /**
     * Calculates the co2 emission of a specific {@link RoutePart}.
     *
     * @param  routePart  to calculate co2 emission for
     * @param  routeDirection  the direction of the route
     *
     * @return  co2 emission
     */
    BigDecimal getEmissionForRoutePart(RoutePart routePart, RouteDirection routeDirection);
}
