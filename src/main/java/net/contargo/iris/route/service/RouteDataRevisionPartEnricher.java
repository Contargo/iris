package net.contargo.iris.route.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.service.AddressListFilter;
import net.contargo.iris.api.NotFoundException;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.terminal.Terminal;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;

import java.util.Map;

import static net.contargo.iris.route.RouteType.TRUCK;
import static net.contargo.iris.route.service.RouteDataRevisionPartEnricher.RouteDataRevisionPolicy.MANDATORY_FOR_SWISS_ADDRESS;

import static org.slf4j.LoggerFactory.getLogger;

import static java.math.BigDecimal.ZERO;

import static java.util.stream.Collectors.toMap;


/**
 * Enriches a {@link net.contargo.iris.route.RoutePart RoutePart} if the following conditions are met: the route parts
 * direction is either terminal -> address or address -> terminal and the route parts type is TRUCK.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class RouteDataRevisionPartEnricher implements RoutePartEnricher {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final String MESSAGE = "Route part isn't Address -> Terminal or Terminal -> Address. "
        + "So not applicable for route data revision.";

    enum RouteDataRevisionPolicy {

        MANDATORY_FOR_SWISS_ADDRESS,
        OPTIONAL
    }

    private final RouteDataRevisionService routeDataRevisionService;
    private final AddressListFilter addressListFilter;
    private final RouteDataRevisionPolicy routeDataRevisionPolicy;

    public RouteDataRevisionPartEnricher(RouteDataRevisionService routeDataRevisionService,
        AddressListFilter addressListFilter, RouteDataRevisionPolicy routeDataRevisionPolicy) {

        this.routeDataRevisionService = routeDataRevisionService;
        this.addressListFilter = addressListFilter;
        this.routeDataRevisionPolicy = routeDataRevisionPolicy;
    }

    @Override
    public void enrich(RoutePart routePart, EnricherContext context) throws CriticalEnricherException {

        if (routePart.getRouteType() == TRUCK) {
            try {
                Terminal terminal = extractTerminal(routePart);
                Address address = extractAddress(routePart);

                RouteDataRevision routeDataRevision = routeDataRevisionService.getRouteDataRevision(terminal, address);

                if (routeDataRevision == null) {
                    if (routeDataRevisionPolicy == MANDATORY_FOR_SWISS_ADDRESS && isSwissAddress(address)) {
                        context.addError("swiss-route", "no route revision available");
                        LOG.info("Routing from {} to CH without route revision", terminal.getName());
                    }
                } else {
                    routePart.getData().setDistance(routeDataRevision.getTruckDistanceOneWayInKilometer());
                    routePart.getData().setTollDistance(routeDataRevision.getTollDistanceOneWayInKilometer());
                    routePart.getData().setAirLineDistance(routeDataRevision.getAirlineDistanceInKilometer());
                    routePart.getData()
                        .setDistancesByCountry(adjust(routePart.getData().getDistancesByCountry(),
                                routeDataRevision.getTruckDistanceOneWayInKilometer()));
                }
            } catch (NotFoundException e) {
                LOG.debug(e.getMessage());
            }
        }
    }


    private Map<String, BigDecimal> adjust(Map<String, BigDecimal> distancesByCountry, BigDecimal finalDistance) {

        String countryMaxDistance = distancesByCountry.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        BigDecimal totalDistanceByCountry = distancesByCountry.values().stream().reduce(ZERO, BigDecimal::add);
        BigDecimal distanceDifference = finalDistance.subtract(totalDistanceByCountry);

        return distancesByCountry.entrySet()
            .stream()
            .collect(toMap(Map.Entry::getKey,
                    e -> {
                        if (e.getKey().equals(countryMaxDistance)) {
                            return e.getValue().add(distanceDifference);
                        }

                        return e.getValue();
                    }));
    }


    private boolean isSwissAddress(Address address) {

        return addressListFilter.isAddressOfCountry(address, "CH");
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
