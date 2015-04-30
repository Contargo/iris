package net.contargo.iris.duration.service;

import net.contargo.iris.truck.TruckRoute;

import java.math.BigDecimal;


/**
 * Interface for implementation of duration services..
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface DurationService {

    /**
     * Returns the duration.
     *
     * @param  truckRoute  to extract the duration
     *
     * @return  the rounded duration of a {@link TruckRoute}
     */
    BigDecimal getDuration(TruckRoute truckRoute);
}
