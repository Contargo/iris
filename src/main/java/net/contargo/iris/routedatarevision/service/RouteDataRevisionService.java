package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.RouteRevisionRequest;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Service to a revision of the received information from a routing service.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface RouteDataRevisionService {

    /**
     * Method to receive the correct {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     * with the shortest distance between the requested {@link Terminal} and {@link GeoLocation} which is valid
     * according to its attributes validFrom and validTo.
     *
     * @param  terminal  on which the {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     *                   information are based on
     * @param  destination  describes the destination {@link GeoLocation} on which the {@link RouteDataRevision}
     *                      information are provided for
     *
     * @return  the {@link RouteDataRevision} with the best fit for the given {@link Terminal} and
     *          {@link net.contargo.iris.address.Address}
     */
    RouteDataRevision getRouteDataRevision(Terminal terminal, GeoLocation destination);


    /**
     * Method to receive the correct {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     * with the shortest distance between the requested {@link net.contargo.iris.address.Address} and the
     * {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} saved in the database which is
     * valid according to its attributes validFrom and validTo.
     *
     * @param  terminalUid  the {@link Terminal}'s uid on which the
     *                      {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} information
     *                      are based on
     * @param  destination  describes the destination {@link GeoLocation} on which the {@link RouteDataRevision}
     *                      information are provided for
     * @param  date  The date which has to be in the {@link RouteDataRevision}s validity range. If null now will be
     *               used.
     *
     * @return  the {@link RouteDataRevision} with the best fit for the given {@link Terminal} and
     *          {@link net.contargo.iris.address.Address}
     */
    RouteDataRevision getRouteDataRevision(BigInteger terminalUid, GeoLocation destination, Date date);


    /**
     * Returns the {@link RouteDataRevision} with the shortest distance between the requested {@link Terminal} and
     * {@link GeoLocation} , having a validity range that applies to the current date. If no such RouteDataRevision
     * exists this method returns the empty Optional.
     *
     * @param  terminalUid  a terminal identifier
     * @param  destination  a geolocation
     *
     * @return  an optional RouteDateRevision
     */
    Optional<RouteDataRevision> getRouteDataRevision(BigInteger terminalUid, GeoLocation destination);


    /**
     * Finds all {@link net.contargo.iris.routedatarevision.RouteDataRevision}.
     *
     * @return  a list of all {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     */
    List<RouteDataRevision> getRouteDataRevisions();


    /**
     * Finds all {@link net.contargo.iris.routedatarevision.RouteDataRevision} belonging to the given terminal.
     *
     * @param  terminalId  The parameter to filter the {@link net.contargo.iris.routedatarevision.RouteDataRevision}s.
     *
     * @return  a list of {@link net.contargo.iris.routedatarevision.RouteDataRevision}s.
     */
    List<RouteDataRevision> getRouteDataRevisions(Long terminalId);


    /**
     * Finds the {@link net.contargo.iris.routedatarevision.RouteDataRevision} for the given id.
     *
     * @param  id  param identifying the {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     *
     * @return  the found {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     *
     * @throws  net.contargo.iris.api.NotFoundException  if there is no
     *                                                   {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     *                                                   for the given id.
     */
    RouteDataRevision getRouteDataRevision(Long id);


    /**
     * Saves the given {@link net.contargo.iris.routedatarevision.RouteDataRevision}.
     *
     * @param  routeDataRevision  the entity to save.
     *
     * @return  the saved {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     */
    RouteDataRevision save(RouteDataRevision routeDataRevision);


    /**
     * Checks if there are any {@link RouteDataRevision} having the specified terminalUniqueId and geolocation, whose
     * {@link ValidityRange} overlaps with the given {@link ValidityRange} argument.
     *
     * @param  terminalUniqueId  The {@link Terminal}s unique Id.
     * @param  latitude  The geolocations latitude
     * @param  longitude  The geolocations longitude
     * @param  validityRange  The {@code ValidityRange} to check for overlaps.
     * @param  routeRevisionId  The revision id, may be null if not yet persisted.
     *
     * @return  {@code true} if there is an overlap. Otherwise {@code false}
     */
    boolean overlapsWithExisting(BigInteger terminalUniqueId, BigDecimal latitude, BigDecimal longitude,
        ValidityRange validityRange, Long routeRevisionId);


    /**
     * Search {@link RouteDataRevision}s by specified parameters.
     *
     * @param  routeRevisionRequest  search request dto that specifies the search parameters
     *
     * @return  a list of matching {@link RouteDataRevision}s
     */
    List<RouteDataRevision> search(RouteRevisionRequest routeRevisionRequest);


    /**
     * Enriches all {@link RouteDataRevision}s not having address information with address information.
     */
    void enrichWithAddressInformation();
}
