package net.contargo.iris.route2.service;

import net.contargo.iris.route2.RoutePartEdgeResultStatus;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class RoutePartEdgeResult {

    private final double distance;
    private final double duration;
    private final List<String> geometries;
    private final RoutePartEdgeResultStatus status;

    public RoutePartEdgeResult(double distance, double duration, List<String> geometries,
        RoutePartEdgeResultStatus status) {

        this.distance = distance;
        this.duration = duration;
        this.geometries = geometries;
        this.status = status;
    }

    public double getDistance() {

        return distance;
    }


    public double getDuration() {

        return duration;
    }


    public List<String> getGeometries() {

        return geometries;
    }


    public RoutePartEdgeResultStatus getStatus() {

        return status;
    }
}
