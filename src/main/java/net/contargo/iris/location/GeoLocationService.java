package net.contargo.iris.location;

import net.contargo.iris.GeoLocation;


/**
 * Provides services related to {@link net.contargo.iris.GeoLocation} entities.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface GeoLocationService {

    /**
     * Tries to find a detailed geolocation by a geolocation with only the gps data.
     *
     * @param  location {@link GeoLocation} with only longitude and latitude
     *
     * @return  Full {@link GeoLocation} with all information about this place
     */
    GeoLocation getDetailedGeoLocation(GeoLocation location);
}
