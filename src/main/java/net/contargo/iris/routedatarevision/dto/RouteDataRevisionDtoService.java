package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;

import java.util.List;


/**
 * Delegates to {@link net.contargo.iris.routedatarevision.service.RouteDataRevisionService } and converts
 * {@link net.contargo.iris.routedatarevision.RouteDataRevision} into
 * {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public interface RouteDataRevisionDtoService {

    /**
     * Finds all {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}.
     *
     * @return  a list of all {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}
     */
    List<RouteDataRevisionDto> getRouteDataRevisions();


    /**
     * Finds all {@link net.contargo.iris.routedatarevision.RouteDataRevision} belonging to the given terminal.
     *
     * @param  terminalId  The parameter to filter the
     *                     {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}s.
     *
     * @return  a list of {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}s.
     */
    List<RouteDataRevisionDto> getRouteDataRevisions(Long terminalId);


    /**
     * Finds the {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto} for the given id.
     *
     * @param  id  param identifying the {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}
     *
     * @return  the found {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}
     *
     * @throws  net.contargo.iris.api.NotFoundException  if there is no {@link RouteDataRevisionDto}
     *                                                   RouteDataRevisionDto for the given id.
     */
    RouteDataRevisionDto getRouteDataRevision(Long id);


    /**
     * Saves the given {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}.
     *
     * @param  routeDataRevision  the entity to save.
     *
     * @return  the saved {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}
     */

    RouteDataRevisionDto save(RouteDataRevisionDto routeDataRevision);


    /**
     * Checks if there is already an entry of type {@link net.contargo.iris.routedatarevision.RouteDataRevision} with
     * the given values.
     *
     * @param  terminalUniqueId  the corresponding {@link net.contargo.iris.terminal.Terminal} for a
     *                           {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     * @param  latitude  the corresponding Latitude for a {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     * @param  longitude  the corresponding Longitude for a
     *                    {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     *
     * @return  true if there already exists an entry with the passed values, otherwise false.
     */
    boolean existsEntry(String terminalUniqueId, BigDecimal latitude, BigDecimal longitude);


    /**
     * Method to receive the correct {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     * with the shortest distance between the requested {@link net.contargo.iris.address.Address} and the
     * {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} saved in the database.
     *
     * @param  terminalUniqueId  the {@link Terminal}'s uid on which the
     *                           {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     *                           information are based on
     * @param  geoLocation  describes the destination {@link GeoLocation} on which the {@link RouteDataRevision}
     *                      information are provided for
     *
     * @return  the {@link RouteDataRevision} with the best fit for the given {@link Terminal} and
     *          {@link net.contargo.iris.address.Address}
     */
    RouteDataRevisionDto findNearest(String terminalUniqueId, GeoLocation geoLocation);
}
