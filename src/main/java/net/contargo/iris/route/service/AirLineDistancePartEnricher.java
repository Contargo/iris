package net.contargo.iris.route.service;

import net.contargo.iris.gis.service.GisService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;


/**
 * RoutePartEnricher for Air-Line-Distance.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
class AirLineDistancePartEnricher implements RoutePartEnricher {

    private static final BigDecimal METERS_IN_A_KILOMETER = new BigDecimal(1000);

    private final GisService gisService;

    private final RoundingService roundingService;

    AirLineDistancePartEnricher(GisService gisService, RoundingService roundingService) {

        this.gisService = gisService;
        this.roundingService = roundingService;
    }

    @Override
    public void enrich(RoutePart routePart, EnricherContext context) {

        BigDecimal airLineDistance = gisService.calcAirLineDistInMeters(routePart.getOrigin(),
                routePart.getDestination());
        airLineDistance = airLineDistance.divide(METERS_IN_A_KILOMETER);
        airLineDistance = roundingService.roundDistance(airLineDistance);

        routePart.getData().setAirLineDistance(airLineDistance);
    }
}
