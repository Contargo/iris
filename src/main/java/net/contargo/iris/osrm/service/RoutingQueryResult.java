package net.contargo.iris.osrm.service;

import java.math.BigDecimal;


/**
 * Represents the result of a OSRM Query to the application. So we dont have to use the OSRM Response returned by the
 * OSRM service
 *
 * @author  Arnold Franke - franke@synyx.de
 */
public final class RoutingQueryResult {

    private final int status;
    private final double totalDistance;
    private final double totalTime;
    private final BigDecimal toll;

    public RoutingQueryResult(int status, double totalDistance, double totalTime, BigDecimal toll) {

        this.status = status;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.toll = toll;
    }

    public int getStatus() {

        return status;
    }


    public double getTotalDistance() {

        return totalDistance;
    }


    public double getTotalTime() {

        return totalTime;
    }


    public BigDecimal getToll() {

        return toll;
    }
}
