package net.contargo.iris.route2.service;

import net.contargo.iris.route2.RoutePartEdgeResultStatus;

import java.math.BigDecimal;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class RoutePartEdgeResult {

    private final BigDecimal distance;
    private final BigDecimal toll;
    private final BigDecimal duration;
    private final List<String> geometries;
    private final RoutePartEdgeResultStatus status;

    public RoutePartEdgeResult(BigDecimal distance, BigDecimal toll, BigDecimal duration, List<String> geometries,
        RoutePartEdgeResultStatus status) {

        this.distance = distance;
        this.toll = toll;
        this.duration = duration;
        this.geometries = geometries;
        this.status = status;
    }

    public BigDecimal getDistance() {

        return distance;
    }


    public BigDecimal getToll() {

        return toll;
    }


    public BigDecimal getDuration() {

        return duration;
    }


    public List<String> getGeometries() {

        return geometries;
    }


    public RoutePartEdgeResultStatus getStatus() {

        return status;
    }
}
