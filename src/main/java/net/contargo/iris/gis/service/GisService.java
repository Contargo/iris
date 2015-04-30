package net.contargo.iris.gis.service;

import net.contargo.iris.GeoLocation;

import java.math.BigDecimal;


/**
 * Provides geocode-related services.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface GisService {

    GeoLocation CENTER_OF_THE_EUROPEAN_UNION = new GeoLocation(new BigDecimal("50.1725"), new BigDecimal("9.15"));

    /**
     * Calculates the airline distance between two geographical points in meters.
     *
     * @param  a  The first geographic location
     * @param  b  The second geographic location
     *
     * @return  The distance between a and b, in meters
     */
    BigDecimal calcAirLineDistInMeters(GeoLocation a, GeoLocation b);
}
