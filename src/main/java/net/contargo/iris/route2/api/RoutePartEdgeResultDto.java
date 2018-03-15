package net.contargo.iris.route2.api;

import net.contargo.iris.route2.RoutePartEdgeResultStatus;
import net.contargo.iris.route2.service.RoutePartEdgeResult;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class RoutePartEdgeResultDto {

    private final double distance;
    private final double duration;
    private final List<String> geometries;
    private final RoutePartEdgeResultStatus status;

    RoutePartEdgeResultDto(RoutePartEdgeResult result) {

        this.distance = result.getDistance();
        this.duration = result.getDuration();
        this.geometries = result.getGeometries();
        this.status = result.getStatus();
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
