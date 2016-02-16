package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteRevisionRequest;
import net.contargo.iris.routedatarevision.ValidityRange;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


/**
 * Delegates to {@link net.contargo.iris.routedatarevision.service.RouteDataRevisionService } and converts
 * {@link net.contargo.iris.routedatarevision.RouteDataRevision} into
 * {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
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
     * Checks if there are any {@link net.contargo.iris.routedatarevision.RouteDataRevision} having the specified
     * terminalUniqueId and geolocation, whose {@link ValidityRange} overlaps with the given {@link ValidityRange}
     * argument.
     *
     * @param  terminalUniqueId  The {@link net.contargo.iris.terminal.Terminal}s unique Id.
     * @param  latitude  The geolocations latitude
     * @param  longitude  The geolocations longitude
     * @param  validityRange  The {@code ValidityRange} to check for overlaps.
     * @param  routeRevisionId  The revision id, may be null if not yet persisted
     *
     * @return  {@code true} if there is an overlap. Otherwise {@code false}
     */

    boolean existsEntry(String terminalUniqueId, BigDecimal latitude, BigDecimal longitude, ValidityRange validityRange,
        Long routeRevisionId);


    /**
     * Method to receive the correct {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     * with the shortest distance between the requested {@link net.contargo.iris.address.Address} and the
     * {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} saved in the database which is
     * valid according to its attributes validFrom and validTo.
     *
     * @param  terminalUniqueId  the {@link net.contargo.iris.terminal.Terminal}'s uid on which the
     *                           {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     *                           information are based on
     * @param  geoLocation  describes the destination {@link GeoLocation} on which the
     *                      {@link net.contargo.iris.routedatarevision.RouteDataRevision} information are provided for
     * @param  date  The date which has to be in the {@link RouteDataRevisionDto}s validity range. If null now will be
     *               used.
     *
     * @return  the {@link net.contargo.iris.routedatarevision.RouteDataRevision} with the best fit for the given
     *          {@link net.contargo.iris.terminal.Terminal} and {@link net.contargo.iris.address.Address}
     */
    RouteDataRevisionDto findNearest(String terminalUniqueId, GeoLocation geoLocation, Date date);


    /**
     * Search {@link RouteDataRevisionDto}s by specified parameters.
     *
     * @param  routeRevisionRequest  search request dto that specifies the search parameters
     *
     * @return  a list of matching {@link RouteDataRevisionDto}s
     */
    List<RouteDataRevisionDto> search(RouteRevisionRequest routeRevisionRequest);
}
