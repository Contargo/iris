package net.contargo.iris.gis.service;

import net.contargo.iris.GeoLocation;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.closeTo;


/**
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class GisServiceImplUnitTest {

    @Test
    public void calcAirLineDistInMeters() {

        GeoLocation a = new GeoLocation(new BigDecimal(49.015), new BigDecimal(8.42));
        GeoLocation b = new GeoLocation(new BigDecimal(50.015), new BigDecimal(9.42));
        GisService sut = new GisServiceImpl();

        BigDecimal expected = new BigDecimal(132713.636546);
        assertThat(sut.calcAirLineDistInMeters(a, b), closeTo(expected, new BigDecimal("0.000001")));
    }
}
