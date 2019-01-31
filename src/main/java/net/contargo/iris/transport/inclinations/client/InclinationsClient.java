package net.contargo.iris.transport.inclinations.client;

import net.contargo.iris.transport.inclinations.dto.Point2D;
import net.contargo.iris.transport.inclinations.dto.Point3D;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public interface InclinationsClient {

    /**
     * Converts a list of 2D points to a list of 3D points. The resulting list may have less entries than the
     *
     * @param  points  a list of 2d points with an osm id
     *
     * @return  a list of 3d points
     */
    List<Point3D> getElevations(List<Point2D> points);
}
