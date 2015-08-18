package net.contargo.iris.route.service;

import net.contargo.iris.route.Route;


/**
 * Interface for instances that can enrich {@link Route} with more data.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
interface RouteTotalEnricher extends Enricher {

    /**
     * Enrich the {@link Route} by any data.
     *
     * @param  route  to enrich
     * @param  context  the context
     */
    void enrich(Route route, EnricherContext context) throws CriticalEnricherException;
}
