package net.contargo.iris.transport.elevation.smoothing;

import org.junit.jupiter.api.Test;

import static net.contargo.iris.transport.elevation.smoothing.DistanceCalculationUtil.calculateDistance;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.closeTo;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
class DistanceCalculationUtilTest {

    @Test
    void calculateOnEarthDistance() {

        double lat = 24.235;
        double lon = 47.234;

        assertThat(calculateDistance(lat, lon, lat - 0.1, lon + 0.1), closeTo(15051, 0.5));
        assertThat(calculateDistance(lat, lon, lat + 0.1, lon - 0.1), closeTo(15046, 0.5));
        assertThat(calculateDistance(lat, lon, lat - 1, lon + 1), closeTo(150748, 0.5));
        assertThat(calculateDistance(lat, lon, lat + 1, lon - 1), closeTo(150211, 0.5));
        assertThat(calculateDistance(lat, lon, lat - 10, lon + 10), closeTo(1527919, 0.5));
        assertThat(calculateDistance(lat, lon, lat + 10, lon - 10), closeTo(1474016, 0.5));
        assertThat(calculateDistance(lat, lon, lat, lon - 10), closeTo(1013735, 0.5));
        assertThat(calculateDistance(lat, lon, lat + 10, lon), closeTo(1111949, 0.5));
    }
}
