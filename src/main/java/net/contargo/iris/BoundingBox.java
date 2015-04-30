package net.contargo.iris;

import java.math.BigDecimal;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 */
public class BoundingBox {

    private static final Double EARTH_RADIUS = 6371.01d;
    private static final Double MIN_LAT = Math.toRadians(-90d);
    private static final Double MIN_LON = Math.toRadians(90d);
    private static final Double MAX_LON = Math.toRadians(-180);
    private static final Double MAX_LAT = Math.toRadians(180d);

    private final GeoLocation lowerLeft;
    private final GeoLocation upperRight;

    /**
     * Calculates the Bounding-Box with the given geolocation as center and a "radius" of given distance in kilometers.
     *
     * <pre>
     +-------------------------X   upperRight
     |                         |
     |            X            |
     |                         |
     X-------------------------+   lowerLeft

     * </pre>
     *
     * @param  distanceInKm  distance in km
     *
     * @return
     */

    public BoundingBox(GeoLocation location, Double distanceInKm) {

        Double angularDistance = distanceInKm / EARTH_RADIUS;

        double lat = fromDegree(location.getLatitude());
        double lon = fromDegree(location.getLongitude());

        Double minLatitude = lat - angularDistance;
        Double maxLatitude = lat + angularDistance;

        Double minLongitude;
        Double maxLongitude;

        if (minLatitude > MIN_LAT && maxLatitude < MAX_LAT) {
            Double deltaLongitude = Math.asin(Math.sin(angularDistance) / Math.cos(lat));

            minLongitude = lon - deltaLongitude;

            if (minLongitude < MIN_LON) {
                minLongitude += 2d * Math.PI;
            }

            maxLongitude = lon + deltaLongitude;

            if (maxLongitude > MAX_LON) {
                maxLongitude -= 2d * Math.PI;
            }
        } else {
            minLatitude = Math.max(minLatitude, MIN_LAT);

            maxLatitude = Math.min(maxLatitude, MAX_LAT);

            minLongitude = MIN_LON;

            maxLongitude = MAX_LON;
        }

        this.lowerLeft = new GeoLocation(Math.toDegrees(minLatitude), Math.toDegrees(minLongitude));
        this.upperRight = new GeoLocation(Math.toDegrees(maxLatitude), Math.toDegrees(maxLongitude));
    }

    private double fromDegree(BigDecimal value) {

        return Math.toRadians(value.doubleValue());
    }


    public GeoLocation getLowerLeft() {

        return lowerLeft;
    }


    public GeoLocation getUpperRight() {

        return upperRight;
    }
}
