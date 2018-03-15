package net.contargo.iris.routing;

import java.math.BigDecimal;

import java.util.List;


/**
 * Represents the result of a OSRM Query to the application. So we dont have to use the OSRM Response returned by the
 * OSRM service
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
public final class RoutingQueryResult {

    public static final int STATUS_NO_ROUTE = 207;
    public static final int STATUS_OK = 200;

    private final int status;
    private final double totalDistance;
    private final double totalTime;
    private final BigDecimal toll;
    private final List<String> geometries;

    public RoutingQueryResult(int status, double totalDistance, double totalTime, BigDecimal toll) {

        this.status = status;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.toll = toll;
        geometries = null;
    }


    public RoutingQueryResult(int status, double totalDistance, double totalTime, BigDecimal toll,
        List<String> geometries) {

        this.status = status;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.toll = toll;
        this.geometries = geometries;
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


    public boolean noRoute() {

        return status == STATUS_NO_ROUTE;
    }


    public List<String> getGeometries() {

        return geometries;
    }
}
