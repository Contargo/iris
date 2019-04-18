package net.contargo.iris.transport.elevation.client;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.elevation.dto.Point2D;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public interface RoutingClient {

    /**
     * Returns a list of list of 2D points with osm id for a routing of a route with the given start and end.
     *
     * @param  start  the start geolocation of the route
     * @param  end  the end geolocation of the route
     *
     * @return  a list of 2D points with a corresponding osm id
     */
    List<Point2D> getPoints(GeoLocation start, GeoLocation end);
}
