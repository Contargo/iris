package net.contargo.iris.transport.inclinations.smoothing;

import org.junit.jupiter.api.Test;

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
        double expected;

        expected = 15051;
        assertThat(DistanceCalculationUtil.calculateDistance(lat, lon, lat - 0.1, lon + 0.1), closeTo(expected, 0.5));

        expected = 15046;
        assertThat(DistanceCalculationUtil.calculateDistance(lat, lon, lat + 0.1, lon - 0.1), closeTo(expected, 0.5));

        expected = 150748;
        assertThat(DistanceCalculationUtil.calculateDistance(lat, lon, lat - 1, lon + 1), closeTo(expected, 0.5));

        expected = 150211;
        assertThat(DistanceCalculationUtil.calculateDistance(lat, lon, lat + 1, lon - 1), closeTo(expected, 0.5));

        expected = 1527919;
        assertThat(DistanceCalculationUtil.calculateDistance(lat, lon, lat - 10, lon + 10), closeTo(expected, 0.5));

        expected = 1474016;
        assertThat(DistanceCalculationUtil.calculateDistance(lat, lon, lat + 10, lon - 10), closeTo(expected, 0.5));

        expected = 1013735;
        assertThat(DistanceCalculationUtil.calculateDistance(lat, lon, lat, lon - 10), closeTo(expected, 0.5));

        expected = 1111949;
        assertThat(DistanceCalculationUtil.calculateDistance(lat, lon, lat + 10, lon), closeTo(expected, 0.5));
    }
}
