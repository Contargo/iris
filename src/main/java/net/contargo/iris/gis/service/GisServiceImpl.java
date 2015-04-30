package net.contargo.iris.gis.service;

import net.contargo.iris.GeoLocation;

import org.geotools.referencing.GeodeticCalculator;

import java.math.BigDecimal;


/**
 * @author  Sven Mueller - mueller@synyx.de
 */
public class GisServiceImpl implements GisService {

    @Override
    public BigDecimal calcAirLineDistInMeters(GeoLocation a, GeoLocation b) {

        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(a.getLongitude().doubleValue(), a.getLatitude().doubleValue());
        calculator.setDestinationGeographicPoint(b.getLongitude().doubleValue(), b.getLatitude().doubleValue());

        return new BigDecimal(calculator.getOrthodromicDistance());
    }
}
