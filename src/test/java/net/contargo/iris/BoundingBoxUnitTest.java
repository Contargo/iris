package net.contargo.iris;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

import static org.hamcrest.number.BigDecimalCloseTo.closeTo;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class BoundingBoxUnitTest {

    private static final double RADIUS_TOO_HIGH = 1000000.0;

    private BoundingBox sut;

    @Test
    public void createBoundingBox() {

        BigDecimal closeToError = new BigDecimal("0.002");

        // The number of kilometers spanned by a longitude range varies based on the current latitude.
        // For example, one degree of longitude spans a distance of approximately 111 kilometers at
        // the equator but shrinks to 0 kilometers at the poles.

        sut = new BoundingBox(new GeoLocation(new BigDecimal("0.0"), new BigDecimal("0.0")), 111.0);

        assertThat(sut.getLowerLeft().getLongitude(), is(closeTo(new BigDecimal("-1.0"), closeToError)));
        assertThat(sut.getUpperRight().getLongitude(), is(closeTo(new BigDecimal("1.0"), closeToError)));

        // Unlike longitudinal distances, which vary based on the latitude, one degree of latitude is
        // always approximately 111 kilometers

        sut = new BoundingBox(new GeoLocation(new BigDecimal("49.0"), new BigDecimal("8.6")), 111.0);

        assertThat(sut.getLowerLeft().getLatitude(), is(closeTo(new BigDecimal("48.0"), closeToError)));
        assertThat(sut.getUpperRight().getLatitude(), is(closeTo(new BigDecimal("50.0"), closeToError)));
    }


    @Test
    public void createBoundingBoxRadiusTooLarge() {

        GeoLocation location = new GeoLocation(new BigDecimal("90.0"), new BigDecimal("7.0"));
        sut = new BoundingBox(location, RADIUS_TOO_HIGH);

        GeoLocation expectedLowerLeft = new GeoLocation(new BigDecimal("-90.0"), new BigDecimal("-180.0"));
        GeoLocation expectedUpperRight = new GeoLocation(new BigDecimal("90.0"), new BigDecimal("180.0"));

        assertThat(sut.getLowerLeft(), is(expectedLowerLeft));
        assertThat(sut.getUpperRight(), is(expectedUpperRight));
    }
}
