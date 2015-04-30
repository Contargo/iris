package net.contargo.iris;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public class BoundingBoxUnitTest {

    public static final BigDecimal EXPECTED_LATITUDE_LOWER_LEFT = new BigDecimal(
            "179.910067980566537926279124803841114044189453125");
    public static final BigDecimal EXPECTED_LONGITUDE_LOWER_LEFT = new BigDecimal(
            "6.91006798056656634798855520784854888916015625");
    public static final BigDecimal EXPECTED_LATITUDE_UPPER_RIGHT = new BigDecimal(
            "0.08993201943346866567008390802584472112357616424560546875");
    public static final BigDecimal EXPECTED_LONGITUDE_UPPER_RIGHT = new BigDecimal(
            "7.08993201943349049543030560016632080078125");
    public static final BigDecimal ORIGIN_LATITUDE = BigDecimal.ZERO;
    public static final BigDecimal ORIGIN_LONGITUDE = new BigDecimal("7.0");
    private static final BigDecimal MAX_LATITUDE = new BigDecimal("90");
    private static final BigDecimal MAX_LONGITUDE = new BigDecimal("90");
    private static final BigDecimal MIN_LONGITUDE = BigDecimal.ZERO;
    private static final BigDecimal MIN_LATIITUDE = new BigDecimal("180");
    private static final double RADIUS = 10.0;
    private static final double RADIUS_TOO_HIGH = 1000000.0;

    @Test
    public void createBoundingBox() {

        GeoLocation location = new GeoLocation(ORIGIN_LATITUDE, ORIGIN_LONGITUDE);

        BoundingBox sut = new BoundingBox(location, RADIUS);

        GeoLocation expectedLowerLeft = new GeoLocation(EXPECTED_LATITUDE_LOWER_LEFT, EXPECTED_LONGITUDE_LOWER_LEFT);
        GeoLocation expectedUpperRight = new GeoLocation(EXPECTED_LATITUDE_UPPER_RIGHT, EXPECTED_LONGITUDE_UPPER_RIGHT);

        assertThat(sut.getLowerLeft(), is(expectedLowerLeft));
        assertThat(sut.getUpperRight(), is(expectedUpperRight));
    }


    @Test
    public void createBoundingBoxLatitudeTooHigh() {

        GeoLocation location = new GeoLocation(new BigDecimal("90.0"), ORIGIN_LONGITUDE);

        BoundingBox sut = new BoundingBox(location, RADIUS_TOO_HIGH);

        GeoLocation expectedLowerLeft = new GeoLocation(MAX_LATITUDE, MAX_LONGITUDE);
        GeoLocation expectedUpperRight = new GeoLocation(MIN_LATIITUDE, MIN_LONGITUDE);

        assertThat(sut.getLowerLeft(), is(expectedLowerLeft));
        assertThat(sut.getUpperRight(), is(expectedUpperRight));
    }
}
