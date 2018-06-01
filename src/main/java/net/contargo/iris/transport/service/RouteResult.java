package net.contargo.iris.transport.service;

import java.math.BigDecimal;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
class RouteResult {

    private final BigDecimal distance;
    private final BigDecimal toll;
    private final BigDecimal duration;
    private final List<String> geometries;
    private final RouteStatus status;

    RouteResult(BigDecimal distance, BigDecimal toll, BigDecimal duration, List<String> geometries,
        RouteStatus status) {

        this.distance = distance;
        this.toll = toll;
        this.duration = duration;
        this.geometries = geometries;
        this.status = status;
    }

    BigDecimal getDistance() {

        return distance;
    }


    BigDecimal getToll() {

        return toll;
    }


    BigDecimal getDuration() {

        return duration;
    }


    List<String> getGeometries() {

        return geometries;
    }


    RouteStatus getStatus() {

        return status;
    }
}
