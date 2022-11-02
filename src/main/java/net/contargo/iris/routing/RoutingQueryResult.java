package net.contargo.iris.routing;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private final Map<String, Double> distancesByCountry;

    public RoutingQueryResult(int status, double totalDistance, double totalTime, BigDecimal toll) {

        this.status = status;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.toll = toll;
        geometries = null;
        distancesByCountry = new HashMap<>();
    }


    public RoutingQueryResult(int status, double totalDistance, double totalTime, BigDecimal toll,
        List<String> geometries, Map<String, Double> distancesByCountry) {

        this.status = status;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.toll = toll;
        this.geometries = geometries;
        this.distancesByCountry = distancesByCountry;
    }

    public int getStatus() {

        return status;
    }


    public double getTotalDistance() {

        return totalDistance;
    }


    public Map<String, Double> getDistancesByCountry() {

        return distancesByCountry;
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
