package net.contargo.iris.co2.service;

import net.contargo.iris.route.Route;

import java.math.BigDecimal;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface Co2Service {

    /**
     * Get Co2 emission for the given route.
     *
     * @param  route  to extract co2 emission
     *
     * @return  Co2 in kg
     */
    BigDecimal getEmission(Route route);


    /**
     * Get Co2 emission for the given route if it would be a direct truck route.
     *
     * @param  route  to extract truck co2 emission
     *
     * @return  Co2 in kg
     */
    BigDecimal getEmissionDirectTruck(Route route);
}
