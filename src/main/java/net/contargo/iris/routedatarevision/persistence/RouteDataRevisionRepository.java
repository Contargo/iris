package net.contargo.iris.routedatarevision.persistence;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;
import java.util.Optional;


/**
 * Repository for {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} instances.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface RouteDataRevisionRepository extends JpaRepository<RouteDataRevision, Long> {

    @Query(
        value = "SELECT * "
            + "FROM "
            + "    ("
            + "        SELECT *, "
            + "               (6371000 * acos(cos(radians(:latitude)) * cos(radians(latitude)) "
            + "               * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) "
            + "               * sin(radians(latitude)))) AS distance "
            + "        FROM RouteDataRevision "
            + "        WHERE terminal_id = :terminal "
            + "    ) as dis "
            + "WHERE dis.distance <= dis.radius "
            + "ORDER BY dis.distance ASC "
            + "LIMIT 0, 1", nativeQuery = true
    )
    RouteDataRevision findNearest(@Param("terminal") Terminal terminal,
        @Param("latitude") BigDecimal latitude,
        @Param("longitude") BigDecimal longitude);


    @Query("SELECT r FROM RouteDataRevision r WHERE r.terminal.id = ?1")
    List<RouteDataRevision> findByTerminalId(Long id);


    @Query("SELECT r FROM RouteDataRevision r WHERE r.terminal.uniqueId = ?1 and r.latitude = ?2 and longitude = ?3")
    Optional<RouteDataRevision> findByTerminalAndLatitudeAndLongitude(BigInteger terminalUniqueId, BigDecimal latitude,
        BigDecimal longitude);
}
