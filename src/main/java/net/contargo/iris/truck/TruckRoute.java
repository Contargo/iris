package net.contargo.iris.truck;

import java.math.BigDecimal;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 */
public class TruckRoute {

    private final BigDecimal distance;
    private final BigDecimal duration;
    private final BigDecimal tollDistance;

    public TruckRoute(BigDecimal distance, BigDecimal tollDistance, BigDecimal duration) {

        this.distance = distance;
        this.tollDistance = tollDistance;
        this.duration = duration;
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
}
