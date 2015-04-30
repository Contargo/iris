package net.contargo.iris.connection.advice;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteBuilder;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;


/**
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 * @see  MainRunStrategy
 * @see  MainRunAdvisor
 */
class MainRunOneWayExportStrategy implements MainRunStrategy {

    /**
     * @see  MainRunStrategy#getRoute(Seaport, GeoLocation, Terminal, ContainerType, RouteType)
     */
    @Override
    public Route getRoute(Seaport seaPort, GeoLocation destination, Terminal terminal, ContainerType containerType,
        RouteType mainRunRouteType) {

        RouteBuilder routeBuilder = new RouteBuilder(terminal, containerType, ContainerState.EMPTY);
        routeBuilder.goTo(destination, RouteType.TRUCK);
        routeBuilder.loadContainer();
        routeBuilder.goTo(terminal, RouteType.TRUCK);
        routeBuilder.goTo(seaPort, mainRunRouteType);
        routeBuilder.responsibleTerminal(terminal);

        return routeBuilder.getRoute();
    }
}
