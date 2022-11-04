package net.contargo.iris.distance.service;

import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.truck.TruckRoute;

import java.math.BigDecimal;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * Handles the extraction and calculation of the distances.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class DistanceServiceImpl implements DistanceService {

    private final RoundingService roundingService;

    public DistanceServiceImpl(RoundingService roundingService) {

        this.roundingService = roundingService;
    }

    @Override
    public BigDecimal getDistance(TruckRoute truckRoute) {

        return roundingService.roundDistance(truckRoute.getDistance());
    }


    @Override
    public Map<String, BigDecimal> getDistancesByCountry(TruckRoute truckRoute) {

        return truckRoute.getDistancesByCountry().entrySet()
            .stream()
            .collect(Collectors.toMap(Entry::getKey, e -> roundingService.roundDistance(e.getValue())));
    }


    @Override
    public BigDecimal getTollDistance(TruckRoute truckRoute) {

        return roundingService.roundDistance(truckRoute.getTollDistance());
    }
}
