package net.contargo.iris.route.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.api.NotFoundException;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.terminal.Terminal;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Enriches a {@link net.contargo.iris.route.RoutePart RoutePart} if the following conditions are met: the route parts
 * direction is either terminal -> address or address -> terminal and the route parts type is TRUCK
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class RouteDataRevisionPartEnricher implements RoutePartEnricher {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final String MESSAGE = "Route part isn't Address -> Terminal or Terminal -> Address. "
        + "So not applicable for route data revision.";

    private final RouteDataRevisionService routeDataRevisionService;

    public RouteDataRevisionPartEnricher(RouteDataRevisionService routeDataRevisionService) {

        this.routeDataRevisionService = routeDataRevisionService;
    }

    @Override
    public void enrich(RoutePart routePart, EnricherContext context) throws CriticalEnricherException {

        if (routePart.getRouteType() == RouteType.TRUCK) {
            try {
                Terminal terminal = extractTerminal(routePart);
                Address address = extractAddress(routePart);

                RouteDataRevision routeDataRevision = routeDataRevisionService.getRouteDataRevision(terminal, address);

                if (routeDataRevision != null) {
                    routePart.getData().setDistance(routeDataRevision.getTruckDistanceOneWayInMeter());
                    routePart.getData().setTollDistance(routeDataRevision.getTollDistanceOneWayInMeter());
                    routePart.getData().setAirLineDistance(routeDataRevision.getAirlineDistanceInMeter());
                }
            } catch (NotFoundException e) {
                LOG.debug(e.getMessage());
            }
        }
    }


    private Terminal extractTerminal(RoutePart routePart) {

        Terminal terminal = null;

        if (routePart.getOrigin() instanceof Terminal) {
            terminal = (Terminal) routePart.getOrigin();
        } else if (routePart.getDestination() instanceof Terminal) {
            terminal = (Terminal) routePart.getDestination();
        }

        if (terminal == null) {
            throw new NotFoundException(MESSAGE);
        }

        return terminal;
    }


    private Address extractAddress(RoutePart routePart) {

        Address address = null;

        if (routePart.getOrigin() instanceof Address) {
            address = (Address) routePart.getOrigin();
        } else if (routePart.getDestination() instanceof Address) {
            address = (Address) routePart.getDestination();
        }

        if (address == null) {
            throw new NotFoundException(MESSAGE);
        }

        return address;
    }
}
