
package net.contargo.iris.route.service;

import net.contargo.iris.distance.service.DistanceService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.routing.RoutingException;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.TruckRouteService;

import java.math.BigDecimal;

import java.util.Map;

import static java.math.BigDecimal.ZERO;


/**
 * Enricher to set information about distance and duration on a Truck {@link RoutePart}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
class TruckRoutingPartEnricher implements RoutePartEnricher {

    private final TruckRouteService truckRouteService;
    private final DistanceService distanceService;

    TruckRoutingPartEnricher(TruckRouteService truckRouteService, DistanceService distanceService) {

        this.truckRouteService = truckRouteService;
        this.distanceService = distanceService;
    }

    @Override
    public void enrich(RoutePart routePart, EnricherContext context) throws CriticalEnricherException {

        if (routePart.isOfType(RouteType.TRUCK) || routePart.isOfType(RouteType.DTRUCK)) {
            RoutePartData routePartData = routePart.getData();

            TruckRoute truckRoute;

            try {
                truckRoute = truckRouteService.route(routePart.getOrigin(), routePart.getDestination());
            } catch (RoutingException e) {
                throw new CriticalEnricherException("Truck routing failed", e);
            }

            BigDecimal partDistance = distanceService.getDistance(truckRoute);
            Map<String, BigDecimal> distancesByCountry = extractDistancesByCountry(partDistance, truckRoute);

            routePartData.setTollDistance(distanceService.getTollDistance(truckRoute));
            routePartData.setDistance(partDistance);
            routePartData.setDieselDistance(partDistance);
            routePartData.setDistancesByCountry(distancesByCountry);
            routePartData.setElectricDistance(ZERO);
            routePartData.setBargeDieselDistance(ZERO);
            routePartData.setRailDieselDistance(ZERO);
            routePartData.setDtruckDistance(routePart.isOfType(RouteType.DTRUCK) ? partDistance : ZERO);
            routePartData.setDuration(RoundingService.roundDuration(truckRoute.getDuration()));
        }
    }


    private Map<String, BigDecimal> extractDistancesByCountry(BigDecimal totalOnewayDistance, TruckRoute truckRoute) {

        Map<String, BigDecimal> result = distanceService.getDistancesByCountry(truckRoute);

        String countryMaxDistance = result.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        BigDecimal totalDistanceByCountry = result.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal distanceDifference = totalOnewayDistance.subtract(totalDistanceByCountry);

        BigDecimal newDistanceForMaxCountry = result.get(countryMaxDistance).add(distanceDifference);
        result.put(countryMaxDistance, newDistanceForMaxCountry);

        return result;
    }
}
