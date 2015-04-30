package net.contargo.iris.duration.service;

import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.truck.TruckRoute;

import java.math.BigDecimal;


/**
 * Service implementation of {@link DurationService} to receive (rounded) duration information from entities.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class DurationServiceImpl implements DurationService {

    private final RoundingService roundingService;

    public DurationServiceImpl(RoundingService roundingService) {

        this.roundingService = roundingService;
    }

    @Override
    public BigDecimal getDuration(TruckRoute truckRoute) {

        return roundingService.roundDuration(truckRoute.getDuration());
    }
}
