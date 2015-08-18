package net.contargo.iris.route.service;

import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;

import java.math.BigDecimal;


/**
 * Enricher that sums up the distances and total distances of a {@link Route} and set it as {@link RouteData}
 * information.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
class TotalEnricher implements RouteTotalEnricher {

    @Override
    public void enrich(Route route, EnricherContext context) {

        RouteData routeData = route.getData();

        BigDecimal totalDistance = null;
        BigDecimal totalRealTollDistance = null;
        BigDecimal totalDuration = null;

        for (RoutePart part : routeData.getParts()) {
            RoutePartData data = part.getData();

            totalDistance = add(totalDistance, data.getDistance());
            totalRealTollDistance = add(totalRealTollDistance, data.getTollDistance());
            totalDuration = add(totalDuration, data.getDuration());
        }

        routeData.setTotalDistance(totalDistance);
        routeData.setTotalRealTollDistance(totalRealTollDistance);
        routeData.setTotalDuration(totalDuration);
    }


    private BigDecimal add(BigDecimal sum, BigDecimal summand) {

        BigDecimal result = sum;

        if (notNull(summand)) {
            if (sum == null) {
                result = BigDecimal.ZERO;
            }

            result = result.add(summand);
        }

        return result;
    }


    private boolean notNull(BigDecimal subject) {

        return subject != null;
    }
}
