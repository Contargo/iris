package net.contargo.iris.routedatarevision.persistence;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;


/**
 * Repository for {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} instances.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */

public interface RouteDataRevisionRepository extends JpaRepository<RouteDataRevision, Long>,
    JpaSpecificationExecutor<RouteDataRevision> {

    @Query(
        value = "SELECT * "
            + "FROM "
            + "    ("
            + "        SELECT *, "
            + "               (6371000 * acos( "
            + "                 GREATEST(-1, LEAST(1, "
            + "                     cos(radians(:latitude)) * cos(radians(latitude)) "
            + "                     * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) "
            + "                     * sin(radians(latitude))"
            + "                 ))"
            + "                )) AS distance "
            + "        FROM RouteDataRevision "
            + "        WHERE terminal_id = :terminal AND "
            + "              validFrom <= :date AND "
            + "                 (validTo >= :date OR validTo IS NULL) "
            + "    ) as dis "
            + "WHERE dis.distance <= dis.radius "
            + "ORDER BY dis.distance ASC "
            + "LIMIT 0, 1", nativeQuery = true
    )
    RouteDataRevision findNearest(@Param("terminal") Terminal terminal,
        @Param("latitude") BigDecimal latitude,
        @Param("longitude") BigDecimal longitude,
        @Param("date") Date date);


    @Query("SELECT r FROM RouteDataRevision r WHERE r.terminal.id = ?1")
    List<RouteDataRevision> findByTerminalId(Long id);


    @Query("SELECT r FROM RouteDataRevision r WHERE r.terminal.uniqueId = ?1 and r.latitude = ?2 and longitude = ?3")
    List<RouteDataRevision> findByTerminalAndLatitudeAndLongitude(BigInteger terminalUniqueId, BigDecimal latitude,
        BigDecimal longitude);


    List<RouteDataRevision> findByCityIsNullAndPostalCodeIsNull();


    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1"))
    @Query("SELECT r FROM RouteDataRevision r WHERE validFrom <= :date AND (validTo >= :date OR validTo IS NULL)")
    Stream<RouteDataRevision> findValid(@Param("date") Date date);
}
