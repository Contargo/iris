package net.contargo.iris.transport.inclinations.smoothing;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
class DistanceCalculationUtil {

    private static final double EARTH_RADIUS = 6371000; // meter

    private DistanceCalculationUtil() {
    }

    /*
     * Calculates the distance in meter between the given coordinate pairs.
     * See http://en.wikipedia.org/wiki/Haversine_formula
     */
    static double calculateDistance(double fromLat, double fromLon, double toLat, double toLon) {

        double normedDist = normalizedDistance(fromLat, fromLon, toLat, toLon);

        return EARTH_RADIUS * 2 * asin(sqrt(normedDist));
    }


    private static double normalizedDistance(double fromLat, double fromLon, double toLat, double toLon) {

        double sinDeltaLat = sin(toRadians(toLat - fromLat) / 2);
        double sinDeltaLon = sin(toRadians(toLon - fromLon) / 2);

        return sinDeltaLat * sinDeltaLat + sinDeltaLon * sinDeltaLon * cos(toRadians(fromLat)) * cos(toRadians(toLat));
    }
}
