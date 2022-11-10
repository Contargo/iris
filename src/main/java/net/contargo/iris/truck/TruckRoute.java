package net.contargo.iris.truck;

import java.math.BigDecimal;

import java.util.Map;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 */
public class TruckRoute {

    private final BigDecimal distance;
    private final BigDecimal duration;
    private final BigDecimal tollDistance;
    private final Map<String, BigDecimal> distancesByCountry;

    public TruckRoute(BigDecimal distance, BigDecimal tollDistance, BigDecimal duration,
        Map<String, BigDecimal> distancesByCountry) {

        this.distance = distance;
        this.tollDistance = tollDistance;
        this.duration = duration;
        this.distancesByCountry = distancesByCountry;
    }

    public BigDecimal getDistance() {

        return distance;
    }


    public BigDecimal getDuration() {

        return duration;
    }


    public BigDecimal getTollDistance() {

        return tollDistance;
    }


    public Map<String, BigDecimal> getDistancesByCountry() {

        return distancesByCountry;
    }
}
