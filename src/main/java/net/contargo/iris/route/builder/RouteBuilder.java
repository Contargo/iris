package net.contargo.iris.route.builder;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;


/**
 * Builder class to create a {@link Route} and enrich its {@link RoutePart}s only with important information like
 * origin, destination, {@link ContainerType}, etc.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class RouteBuilder {

    private final Route route;
    private GeoLocation currentLocation;
    private ContainerType containerType;
    private ContainerState containerState;

    public RouteBuilder(GeoLocation startLocation, ContainerType containerType, ContainerState containerState) {

        this.route = new Route();

        this.currentLocation = startLocation;
        this.containerType = containerType;
        this.containerState = containerState;
    }

    public void unloadContainer() {

        containerState = EMPTY;
    }


    public void loadContainer() {

        containerState = FULL;
    }


    /**
     * Adds a {@link RoutePart} to {@link Route} setting information about origin, destination, route type, container
     * type, container state (full or empty) and direction (if barge route).
     *
     * @param  destination  as next destination
     * @param  routeType  type of the next part with destination
     */
    public RouteBuilder goTo(GeoLocation destination, RouteType routeType) {

        RoutePart part = new RoutePart(currentLocation, destination, routeType);

        part.setContainerType(containerType);
        part.setContainerState(containerState);

        route.getData().getParts().add(part);

        part.setData(null);

        currentLocation = destination;

        return this;
    }


    public void responsibleTerminal(Terminal terminal) {

        route.setResponsibleTerminal(terminal);
    }


    public Route getRoute() {

        return route;
    }


    public void changeContainerType(ContainerType type) {

        this.containerType = type;
    }
}
