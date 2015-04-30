package net.contargo.iris.distance.service;

import net.contargo.iris.truck.TruckRoute;

import java.math.BigDecimal;


/**
 * Extracts specific properties and calculates the correct value with or without rounding.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface DistanceService {

    /**
     * Extracts the distance of a {@link TruckRoute}.
     *
     * @param  truckRoute  to get distance of
     *
     * @return  BigDecimal
     */
    BigDecimal getDistance(TruckRoute truckRoute);


    /**
     * Extracts the toll distance of a {@link TruckRoute}.
     *
     * @param  truckRoute  to get toll of
     *
     * @return  BigDecimal
     */
    BigDecimal getTollDistance(TruckRoute truckRoute);
}
