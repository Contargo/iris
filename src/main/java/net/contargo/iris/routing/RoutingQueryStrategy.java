package net.contargo.iris.routing;

import net.contargo.iris.GeoLocation;


/**
 * Interface of the osrm services.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
public interface RoutingQueryStrategy {

    /**
     * Returns the routing results of the route between the start and the destination.
     *
     * @param  start  {@link GeoLocation }of the start point
     * @param  destination  {@link GeoLocation } of the destination point
     *
     * @return  {@link RoutingQueryResult}
     */
    RoutingQueryResult route(GeoLocation start, GeoLocation destination);
}
