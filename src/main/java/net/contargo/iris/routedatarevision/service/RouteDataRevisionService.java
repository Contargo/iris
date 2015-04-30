package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;


/**
 * Service to a revision of the received information from a routing service.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface RouteDataRevisionService {

    /**
     * Method to receive the correct {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     * with the shortest distance between the requested {@link Address} and the
     * {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision} saved in the database.
     *
     * @param  terminal  on which the {@link net.contargo.iris.routedatarevision.RouteDataRevision RouteDataRevision}
     *                   information are based on
     * @param  destination  describes the destination {@link Address} on which the {@link RouteDataRevision} information
     *                      are provided for
     *
     * @return  the {@link RouteDataRevision} with the best fit for the given {@link Terminal} and {@link Address}
     */
    RouteDataRevision getRouteDataRevision(Terminal terminal, Address destination);
}
