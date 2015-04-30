package net.contargo.iris.seaport;

import net.contargo.iris.GeoLocation;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class SeaportUnitTest {

    @Test
    public void create() {

        BigDecimal latitude = BigDecimal.ZERO;
        BigDecimal longitude = BigDecimal.ONE;

        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        Seaport sut = new Seaport(geoLocation);

        assertThat(sut.getLongitude().compareTo(longitude), is(0));
        assertThat(sut.getLatitude().compareTo(latitude), is(0));
    }
}
