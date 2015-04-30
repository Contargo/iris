package net.contargo.iris.enricher.service;

import net.contargo.iris.route.Route;


/**
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface EnricherService {

    /**
     * Enriches a {@link Route}.
     *
     * @param  route  to be enriched
     *
     * @return  enriched {@link Route}
     */
    Route enrich(Route route);
}
