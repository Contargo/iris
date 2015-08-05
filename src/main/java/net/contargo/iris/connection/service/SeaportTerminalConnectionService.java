package net.contargo.iris.connection.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import java.util.List;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface SeaportTerminalConnectionService {

    /**
     * Finds all {@link net.contargo.iris.seaport.Seaport}s that have a
     * {@link net.contargo.iris.connection.MainRunConnection} of type {@code type}.
     *
     * @param  type  the {@link net.contargo.iris.connection.MainRunConnection}'s
     *               {@link net.contargo.iris.route.RouteType}
     *
     * @return  all matching {@link net.contargo.iris.seaport.Seaport}s
     */
    List<Seaport> findSeaPortsConnectedByRouteType(RouteType type);


    /**
     * Finds all {@link net.contargo.iris.terminal.Terminal}s that are part of a
     * {@link net.contargo.iris.connection.MainRunConnection} with the given {@link RouteType} and the specified
     * {@link Seaport} property.
     *
     * @param  seaPort  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     *
     * @return  all matching {@link net.contargo.iris.terminal.Terminal}s
     */
    List<Terminal> getTerminalsConnectedToSeaPortByRouteType(Seaport seaPort, RouteType routeType);


    /**
     * Finds all {@link MainRunConnection}s with the given {@link RouteType} and the specified {@link Seaport} property.
     *
     * @param  seaPort  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     *
     * @return  all matching {@link MainRunConnection}s
     */
    List<MainRunConnection> getConnectionsToSeaPortByRouteType(Seaport seaPort, RouteType routeType);
}
