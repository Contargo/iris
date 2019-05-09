package net.contargo.iris.transport.route;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class RouteResult {

    private final Integer distance;
    private final Integer toll;
    private final Integer duration;
    private final List<String> geometries;
    private final RouteStatus status;

    public RouteResult(Integer distance, Integer toll, Integer duration, List<String> geometries, RouteStatus status) {

        this.distance = distance;
        this.toll = toll;
        this.duration = duration;
        this.geometries = geometries;
        this.status = status;
    }

    public Integer getDistance() {

        return distance;
    }


    public Integer getToll() {

        return toll;
    }


    public Integer getDuration() {

        return duration;
    }


    public List<String> getGeometries() {

        return geometries;
    }


    public RouteStatus getStatus() {

        return status;
    }
}
