package net.contargo.iris.route.builder;

import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.TruckRouteService;

import java.math.BigDecimal;


/**
 * Helper class to build a direct truck route from a given route.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class DirectTruckRouteBuilder {

    private final TruckRouteService truckRouteService;

    public DirectTruckRouteBuilder(TruckRouteService truckRouteService) {

        this.truckRouteService = truckRouteService;
    }

    /**
     * Builds the corresponding direct truck route to a given {@link Route} that may have mainrun parts.
     *
     * @param  route {@link Route}
     *
     * @return  corresponding direct truck route to the given {@link Route}
     */
    public Route getCorrespondingDirectTruckRoute(Route route) {

        Route newRoute = new Route();

        boolean skipLoop = false;

        for (int i = 0; i < route.getData().getParts().size(); i++) {
            if (skipLoop) {
                skipLoop = false;

                continue;
            }

            RoutePart currentPart = route.getData().getParts().get(i);
            RoutePart partToAdd = currentPart.copyWithoutData();

            if (!isLastPart(route, i)) {
                RoutePart nextPart = route.getData().getParts().get(i + 1);

                if (stopsOnTerminal(currentPart, nextPart)) {
                    partToAdd = currentPart.copyWithoutData();
                    partToAdd.setDestination(nextPart.getDestination());
                    partToAdd.setRouteType(RouteType.TRUCK);

                    // skip over the next part
                    skipLoop = true;
                }
            }

            enrichRoutePartWithDistance(partToAdd);
            newRoute.getData().getParts().add(partToAdd);
        }

        return newRoute;
    }


    private boolean isLastPart(Route route, int i) {

        return i == route.getData().getParts().size() - 1;
    }


    private void enrichRoutePartWithDistance(RoutePart part) {

        TruckRoute route = truckRouteService.route(part.getOrigin(), part.getDestination());
        BigDecimal distance = route.getDistance();
        part.getData().setDistance(distance);
    }


    /**
     * Checks if {@link Route} has a {@link Terminal} as stopover. (e.g. if {@link Route} has schema: Seaport -->
     * Terminal --> Destination --> Terminal)
     *
     * @param  currentPart {@link RoutePart}
     * @param  nextPart {@link RoutePart}
     *
     * @return  true if {@link Route} has a {@link Terminal} as stopover, false if not (i.e. Terminal is end or start
     *          point, e.g.: Seaport --> Destination --> Terminal)
     */
    private boolean stopsOnTerminal(RoutePart currentPart, RoutePart nextPart) {

        return (currentPart.getDestination() instanceof Terminal)
            && currentPart.getDestination().equals(nextPart.getOrigin());
    }
}
