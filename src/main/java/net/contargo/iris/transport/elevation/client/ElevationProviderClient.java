package net.contargo.iris.transport.elevation.client;

import net.contargo.iris.transport.elevation.dto.Point2D;
import net.contargo.iris.transport.elevation.dto.Point3D;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public interface ElevationProviderClient {

    /**
     * Converts a list of 2D points to a list of 3D points. The resulting list may have less entries than the given
     * list of 2D Points, due to unavailability of elevation data (e.g. tunnels and bridges).
     *
     * @param  points  a list of 2D points
     *
     * @return  a list of 3D points
     */
    List<Point3D> getElevations(List<Point2D> points);
}
