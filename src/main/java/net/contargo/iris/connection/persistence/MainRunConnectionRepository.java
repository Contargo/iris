package net.contargo.iris.connection.persistence;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<MainRunConnection> findByTerminalAndSeaportAndRouteTypeAndEnabled(Terminal terminal, Seaport seaport,
        RouteType routeType, boolean enabled);


    /**
     * Finds all {@link MainRunConnection}s with the given {@link RouteType} and the specified {@link Seaport}
     * property.
     *
     * @param  seaportUid  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}'s unique id
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     *
     * @return  a list of {@link MainRunConnection}s
     */
    @Query("SELECT c FROM MainRunConnection c WHERE c.seaport.uniqueId = ?1 AND c.routeType = ?2 and c.enabled = true")
    List<MainRunConnection> findBySeaportAndRouteType(BigInteger seaportUid, RouteType routeType);


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
     * {@link RouteType} without the own id exists.
     *
     * @param  seaportId  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}'s id
     * @param  terminalId  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Terminal}'s id
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     * @param  id  the id of the own {@link net.contargo.iris.connection.MainRunConnection}
     *
     * @return  <code>true</code>, if a connection is found
     */
    @Query(
        "select case when count(c) > 0 then true else false end from MainRunConnection c where c.seaport.id = "
        + ":seaportId and c.terminal.id = :terminalId and c.routeType = :routeType and c.id <> :id"
    )
    boolean existsBySeaportAndTerminalAndRouteTypeAndIdNot(@Param("seaportId") Long seaportId,
        @Param("terminalId") Long terminalId,
        @Param("routeType") RouteType routeType,
        @Param("id") Long id);


    /**
     * Checks if a {@link net.contargo.iris.connection.MainRunConnection} by {@link Seaport}, {@link Terminal} and
     * {@link RouteType} already exists.
     *
     * @param  seaportId  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Seaport}'s id
     * @param  terminalId  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link Terminal}'s id
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     *
     * @return  <code>true</code>, if no connection with this attributes are found
     */
    @Query(
        "select case when count(c) > 0 then true else false end from MainRunConnection c where c.seaport.id = "
        + ":seaportId and c.terminal.id = :terminalId and c.routeType = :routeType"
    )
    boolean existsBySeaportAndTerminalAndRouteType(@Param("seaportId") Long seaportId,
        @Param("terminalId") Long terminalId,
        @Param("routeType") RouteType routeType);


    @Query("SELECT c FROM MainRunConnection c WHERE c.terminal.uniqueId = ?1")
    List<MainRunConnection> findConnectionsByTerminalUniqueId(BigInteger terminalUID);


    @Query(
        "SELECT connection FROM MainRunConnection connection WHERE connection.terminal.uniqueId = :terminalUid AND "
        + "connection.seaport.uniqueId = :seaportUid AND connection.routeType = :routeType"
    )
    MainRunConnection findConnectionByTerminalUidAndSeaportUidAndType(@Param("terminalUid") BigInteger terminalUid,
        @Param("seaportUid") BigInteger seaportUid,
        @Param("routeType") RouteType routeType);
}
