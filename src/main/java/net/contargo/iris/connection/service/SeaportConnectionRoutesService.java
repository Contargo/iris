package net.contargo.iris.connection.service;

import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.seaport.Seaport;

import java.util.List;


/**
 * Service that calculates {@link Route}s between {@link Seaport}s and some destination.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface SeaportConnectionRoutesService {

    /**
     * Returns a list of all possible connection routes between a seaport and a destination address.
     *
     * @param  origin  The origin seaport
     * @param  routeInformation
     *
     * @return  List of possible connections (empty if none found)
     */
    List<Route> getAvailableSeaportConnectionRoutes(Seaport origin, RouteInformation routeInformation);
}
