package net.contargo.iris.transport.service;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
class RouteResult {

    private final Integer distance;
    private final Integer toll;
    private final Integer duration;
    private final List<String> geometries;
    private final RouteStatus status;

    RouteResult(Integer distance, Integer toll, Integer duration, List<String> geometries, RouteStatus status) {

        this.distance = distance;
        this.toll = toll;
        this.duration = duration;
        this.geometries = geometries;
        this.status = status;
    }

    Integer getDistance() {

        return distance;
    }


    Integer getToll() {

        return toll;
    }


    Integer getDuration() {

        return duration;
    }


    List<String> getGeometries() {

        return geometries;
    }


    RouteStatus getStatus() {

        return status;
    }
}
