package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;


/**
 * Service to a revision of the received information from a routing service.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface RouteDataRevisionService {

    /**
     * Method to receive the correct {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     * with the shortest distance between the requested {@link net.contargo.iris.address.Address} and the
     * {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} saved in the database.
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
     * {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} saved in the database.
     *
     * @param  terminalUid  the {@link Terminal}'s uid on which the
     *                      {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} information
     *                      are based on
     * @param  destination  describes the destination {@link GeoLocation} on which the {@link RouteDataRevision}
     *                      information are provided for
     *
     * @return  the {@link RouteDataRevision} with the best fit for the given {@link Terminal} and
     *          {@link net.contargo.iris.address.Address}
     */
    RouteDataRevision getRouteDataRevision(BigInteger terminalUid, GeoLocation destination);


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
     * Checks if there is already an entry of type {@link net.contargo.iris.routedatarevision.RouteDataRevision} with
     * the given values.
     *
     * @param  terminalUniqueId  the corresponding {@link net.contargo.iris.terminal.Terminal}'s unique id for a
     *                           {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     * @param  latitude  the corresponding Latitude for a {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     * @param  longitude  the corresponding Longitude for a
     *                    {@link net.contargo.iris.routedatarevision.RouteDataRevision}
     *
     * @return  true if there already exists an entry with the passed values, otherwise false.
     */
    boolean existsEntry(BigInteger terminalUniqueId, BigDecimal latitude, BigDecimal longitude);
}
