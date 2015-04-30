package net.contargo.iris.connection.persistence;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

import java.util.List;


/**
 * Repository interface for {@link net.contargo.iris.connection.MainRunConnection}s that grants access to the database.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface MainRunConnectionRepository extends JpaRepository<MainRunConnection, Long> {

    /**
     * Finds all {@link net.contargo.iris.connection.MainRunConnection}s with the specified {@code enabled} value.
     *
     * @param  enabled  indicates whether enabled or non-enabled {@link net.contargo.iris.connection.MainRunConnection}s
     *                  should be returned
     *
     * @return  a list of {@link net.contargo.iris.connection.MainRunConnection}s
     */
    List<MainRunConnection> findByEnabled(boolean enabled);


    /**
     * Finds the specified {@link net.contargo.iris.connection.MainRunConnection}.
     *
     * @param  terminal  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Terminal}
     * @param  seaport  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     * @param  enabled  indicates whether the connection should be enabled or not
     *
     * @return  a {@link net.contargo.iris.connection.MainRunConnection} with the specified properties
     */
    MainRunConnection findByTerminalAndSeaportAndRouteTypeAndEnabled(Terminal terminal, Seaport seaport,
        RouteType routeType, boolean enabled);


    /**
     * Finds all {@link Seaport}s that are part of a {@link net.contargo.iris.connection.MainRunConnection} with the
     * given {@link RouteType}.
     *
     * @param  type  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     *
     * @return  a list of {@link Seaport}s
     */
    @Query(
        "SELECT DISTINCT p FROM MainRunConnection c JOIN c.seaport p WHERE c.routeType = ?1 and p.enabled = true "
        + "and c.enabled = true"
    )
    List<Seaport> findSeaportsConnectedByRouteType(RouteType type);


    /**
     * Finds all {@link Terminal}s that are part of a {@link net.contargo.iris.connection.MainRunConnection} with the
     * given {@link RouteType} and the specified {@link Seaport} property.
     *
     * @param  seaportUid  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}'s unique id
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     *
     * @return  a list of {@link Terminal}s
     */
    @Query(
        "SELECT t FROM MainRunConnection c JOIN c.terminal t JOIN c.seaport s WHERE c.seaport.uniqueId = ?1 "
        + "AND c.routeType = ?2 and t.enabled = true and s.enabled = true and c.enabled = true"
    )
    List<Terminal> getTerminalsConnectedToSeaPortByRouteType(BigInteger seaportUid, RouteType routeType);


    /**
     * Checks if a {@link net.contargo.iris.connection.MainRunConnection} by {@link Seaport}, {@link Terminal} and
     * {@link RouteType} without the own id is already applied.
     *
     * @param  seaport  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}
     * @param  terminal  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Terminal}
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     * @param  id  the id of the own {@link net.contargo.iris.connection.MainRunConnection}
     *
     * @return  <code>true</code>, if a connection is not found or is found but with the given id
     */
    MainRunConnection findBySeaportAndTerminalAndRouteTypeAndIdNot(Seaport seaport, Terminal terminal,
        RouteType routeType, Long id);


    /**
     * Checks if a {@link net.contargo.iris.connection.MainRunConnection} by {@link Seaport}, {@link Terminal} and
     * {@link RouteType}is already applied.
     *
     * @param  seaport  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}
     * @param  terminal  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Terminal}
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     *
     * @return  <code>true</code>, if no connection with this attributes are found
     */
    MainRunConnection findBySeaportAndTerminalAndRouteType(Seaport seaport, Terminal terminal, RouteType routeType);


    @Query("SELECT c FROM MainRunConnection c WHERE c.terminal.uniqueId = ?1")
    List<MainRunConnection> findConnectionsByTerminalUniqueId(BigInteger terminalUID);
}
