package net.contargo.iris.route.service;

import net.contargo.iris.route.Route;


/**
 * This enricher checks if a 'swiss-route' error does exists and if the route is:
 *
 * <ul>
 * <li>a direct truck route (DTR), then it removes the error because DTR are allowed without
 * {@link net.contargo.iris.routedatarevision.RouteDataRevision}s</li>
 * <li>not a DTR, throw an {@link CriticalEnricherException}</li>
 * </ul>
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class RouteDataRevisionEnricher implements RouteTotalEnricher {

    @Override
    public void enrich(Route route, EnricherContext context) throws CriticalEnricherException {

        if (context.getErrors().containsKey("swiss-route")) {
            if (route.isDirectTruckRoute()) {
                context.getErrors().remove("swiss-route");
            } else {
                throw new CriticalEnricherException(
                    "A none direct truck routing to Swiss locations without route revisions is impossible");
            }
        }
    }
}
