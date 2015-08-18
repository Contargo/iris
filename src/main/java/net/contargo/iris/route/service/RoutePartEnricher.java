package net.contargo.iris.route.service;

import net.contargo.iris.route.RoutePart;


/**
 * Interface for instances that can enrich {@link RoutePart} with more data.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
interface RoutePartEnricher extends Enricher {

    /**
     * Enrich the {@link RoutePart} by any data.
     *
     * @param  routePart  the {@link RoutePart} to enrich
     * @param  context  the {@link EnricherContext}
     */
    void enrich(RoutePart routePart, EnricherContext context) throws CriticalEnricherException;
}
