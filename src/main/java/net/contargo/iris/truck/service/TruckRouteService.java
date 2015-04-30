
package net.contargo.iris.truck.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.truck.TruckRoute;


/**
 * Generates routings for Trucks.
 *
 * @author  Sven Mueller - mueller@synyx.de
 */
public interface TruckRouteService {

    /**
     * Generate a truck routing.
     *
     * @param  start  The start coordinates
     * @param  destination  The destination coordinates
     *
     * @return  The calculated routing
     */
    TruckRoute route(GeoLocation start, GeoLocation destination);
}
