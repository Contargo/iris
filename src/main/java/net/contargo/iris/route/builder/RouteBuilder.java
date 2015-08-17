package net.contargo.iris.route.builder;

import com.google.common.collect.Lists;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import java.util.ArrayList;
import java.util.List;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.route.RouteType.BARGE_RAIL;


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


    /**
     * Adds a barge-rail {@link RoutePart} to {@link Route} setting information about origin, route type, container
     * type, container state (full or empty), direction and {@link SubRoutePart}s. The destination will be the specified
     * {@link Seaport}, that's why the subconnections will be traversed in reverse order to create the
     * {@link SubRoutePart}s.
     *
     * @param  seaport  the {@link RoutePart}'s destination seaport
     * @param  subConnections  the {@link AbstractSubConnection}s the resulting {@link SubRoutePart}s are based on
     */
    public void goToSeaportViaSubConnections(Seaport seaport, List<AbstractSubConnection> subConnections) {

        List<SubRoutePart> subs = new ArrayList<>();

        for (AbstractSubConnection subConnection : Lists.reverse(subConnections)) {
            SubRoutePart sub = new SubRoutePart();

            if (subConnection instanceof TerminalSubConnection) {
                sub.setOrigin(((TerminalSubConnection) subConnection).getTerminal2());
                sub.setDestination(subConnection.getTerminal());
            } else {
                sub.setOrigin(subConnection.getTerminal());
                sub.setDestination(((SeaportSubConnection) subConnection).getSeaport());
            }

            sub.setRouteType(subConnection.getRouteType());
            subs.add(sub);
        }

        RoutePart part = new RoutePart(currentLocation, seaport, BARGE_RAIL);

        part.setContainerType(containerType);
        part.setContainerState(containerState);
        part.setSubRouteParts(subs);

        route.getData().getParts().add(part);

        part.setData(null);

        currentLocation = seaport;
    }


    /**
     * Adds a barge-rail {@link RoutePart} to {@link Route} setting information about origin, route type, container
     * type, container state (full or empty), direction and {@link SubRoutePart}s. The destination will be the specified
     * {@link Terminal}, that's why the subconnections will be traversed in original order to create the
     * {@link SubRoutePart}s.
     *
     * @param  terminal  the {@link RoutePart}'s destination terminal
     * @param  subConnections  the {@link AbstractSubConnection}s the resulting {@link SubRoutePart}s are based on
     */
    public void goToTerminalViaSubConnections(Terminal terminal, List<AbstractSubConnection> subConnections) {

        List<SubRoutePart> subs = new ArrayList<>();

        for (AbstractSubConnection subConnection : subConnections) {
            SubRoutePart sub = new SubRoutePart();

            if (subConnection instanceof TerminalSubConnection) {
                sub.setOrigin(subConnection.getTerminal());
                sub.setDestination(((TerminalSubConnection) subConnection).getTerminal2());
            } else {
                sub.setOrigin(((SeaportSubConnection) subConnection).getSeaport());
                sub.setDestination(subConnection.getTerminal());
            }

            sub.setRouteType(subConnection.getRouteType());
            subs.add(sub);
        }

        RoutePart part = new RoutePart(currentLocation, terminal, BARGE_RAIL);

        part.setContainerType(containerType);
        part.setContainerState(containerState);
        part.setSubRouteParts(subs);

        route.getData().getParts().add(part);

        part.setData(null);

        currentLocation = terminal;
    }
}
