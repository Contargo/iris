package net.contargo.iris.connection.advice;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;


/**
 * Strategy component for building a concrete route.
 *
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 * @see  MainRunAdvisor
 */
public interface MainRunStrategy {

    /**
     * Builds a {@link Route} based on given parameters.
     *
     * @param  seaport  the {@link Route}'s {@link Seaport}
     * @param  destination  the {@link Route}'s destination
     * @param  terminal  the {@link Route}'s {@link Terminal}
     * @param  containerType  the {@link Route}'s {@link ContainerType}
     * @param  routeType  the {@link Route}'s {@link RouteType}
     *
     * @return
     */
    Route getRoute(Seaport seaport, GeoLocation destination, Terminal terminal, ContainerType containerType,
        RouteType routeType);
}
