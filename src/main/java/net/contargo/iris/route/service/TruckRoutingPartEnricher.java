
package net.contargo.iris.route.service;

import net.contargo.iris.distance.service.DistanceService;
import net.contargo.iris.duration.service.DurationService;
import net.contargo.iris.osrm.service.RoutingException;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.TruckRouteService;

import java.math.BigDecimal;


/**
 * Enricher to set information about distance and duration on a Truck {@link RoutePart}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
class TruckRoutingPartEnricher implements RoutePartEnricher {

    private final TruckRouteService truckRouteService;
    private final DistanceService distanceService;
    private final DurationService durationService;

    TruckRoutingPartEnricher(TruckRouteService truckRouteService, DistanceService distanceService,
        DurationService durationService) {

        this.truckRouteService = truckRouteService;
        this.distanceService = distanceService;
        this.durationService = durationService;
    }

    @Override
    public void enrich(RoutePart routePart, EnricherContext context) throws CriticalEnricherException {

        if (routePart.isOfType(RouteType.TRUCK)) {
            RoutePartData routePartData = routePart.getData();

            TruckRoute truckRoute;

            try {
                truckRoute = truckRouteService.route(routePart.getOrigin(), routePart.getDestination());
            } catch (RoutingException e) {
                throw new CriticalEnricherException("Truck routing failed", e);
            }

            BigDecimal partDistance = distanceService.getDistance(truckRoute);

            routePartData.setTollDistance(distanceService.getTollDistance(truckRoute));
            routePartData.setDistance(partDistance);
            routePartData.setDieselDistance(partDistance);
            routePartData.setElectricDistance(BigDecimal.ZERO);
            routePartData.setBargeDieselDistance(BigDecimal.ZERO);
            routePartData.setRailDieselDistance(BigDecimal.ZERO);
            routePartData.setDuration(durationService.getDuration(truckRoute));
        }
    }
}
