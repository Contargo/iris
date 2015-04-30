package net.contargo.iris.osrm.service;

import net.contargo.iris.GeoLocation;


/**
 * Interface of the osrm services.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface OSRMQueryService {

    /**
     * Returns the information from osrm of the route between the start and the destination.
     *
     * @param  start {@link GeoLocation }of the start point
     * @param  destination {@link GeoLocation } of the destination point
     *
     * @return  {@link OSRMQueryResult} with all information given by the osrm server
     */
    OSRMQueryResult getOSRMXmlRoute(GeoLocation start, GeoLocation destination);
}
