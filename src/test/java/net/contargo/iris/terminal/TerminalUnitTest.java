package net.contargo.iris.terminal;

import net.contargo.iris.GeoLocation;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * Unit test of {@link Terminal}.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
public class TerminalUnitTest {

    @Test
    public void create() {

        BigDecimal latitude = BigDecimal.ZERO;
        BigDecimal longitude = BigDecimal.ONE;

        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        Terminal sut = new Terminal(geoLocation);

        assertThat(sut.getLongitude().compareTo(longitude), is(0));
        assertThat(sut.getLatitude().compareTo(latitude), is(0));
    }
}
