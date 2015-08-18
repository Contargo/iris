package net.contargo.iris.route.service;

import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.route.RouteType;

import java.math.BigDecimal;

import java.util.List;


/**
 * Enricher that sums up the totalTollDistance of a {@link Route} and set it as {@link RouteData} information.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
class TruckingTotalEnricher implements RouteTotalEnricher {

    private static final BigDecimal DOUBLING = new BigDecimal("2");
    private static final BigDecimal DIVISOR = new BigDecimal("2");

    @Override
    public void enrich(Route route, EnricherContext context) {

        RouteData routeData = route.getData();

        BigDecimal totalTollDistance = BigDecimal.ZERO;
        BigDecimal totalOnewayTruckDistance = BigDecimal.ZERO;

        for (RoutePart truckPart : getRelevantTruckRouteParts(route)) {
            RoutePartData truckPartData = truckPart.getData();

            totalTollDistance = totalTollDistance.add(truckPartData.getTollDistance());
            totalOnewayTruckDistance = totalOnewayTruckDistance.add(truckPartData.getDistance());
        }

        if (route.isTriangle()) {
            totalTollDistance = totalTollDistance.divide(DIVISOR);
            totalOnewayTruckDistance = totalOnewayTruckDistance.divide(DIVISOR);
        }

        // has to be doubled because above you look only at the oneway
        routeData.setTotalTollDistance(totalTollDistance.multiply(DOUBLING));
        routeData.setTotalOnewayTruckDistance(totalOnewayTruckDistance);
    }


    private List<RoutePart> getRelevantTruckRouteParts(Route route) {

        if (route.isTriangle()) {
            return route.getData().getRoutePartsOfType(RouteType.TRUCK);
        }

        return route.getData().getOnewayTruckParts().getTruckRoutePartList();
    }
}
