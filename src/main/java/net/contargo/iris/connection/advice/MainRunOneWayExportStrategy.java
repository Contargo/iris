package net.contargo.iris.connection.advice;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteBuilder;
import net.contargo.iris.route.RouteType;


/**
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 * @see  MainRunStrategy
 * @see  MainRunAdvisor
 */
class MainRunOneWayExportStrategy implements MainRunStrategy {

    @Override
    public Route getRoute(MainRunConnection connection, GeoLocation destination, ContainerType containerType,
        RouteType mainRunRouteType) {

        RouteBuilder routeBuilder = new RouteBuilder(connection.getTerminal(), containerType, ContainerState.EMPTY);
        routeBuilder.goTo(destination, RouteType.TRUCK);
        routeBuilder.loadContainer();
        routeBuilder.goTo(connection.getTerminal(), RouteType.TRUCK);
        routeBuilder.goTo(connection.getSeaport(), mainRunRouteType);
        routeBuilder.responsibleTerminal(connection.getTerminal());

        return routeBuilder.getRoute();
    }
}
