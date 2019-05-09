package net.contargo.iris.transport.elevation;

import net.contargo.iris.transport.elevation.dto.ElevationPoint;

import java.util.List;


/**
 * @author  Petra Scherer - scherer@synyx.de
 */
public class Elevations {

    private final List<ElevationPoint> elevationProfile;

    public Elevations(List<ElevationPoint> elevationPoints) {

        elevationProfile = elevationPoints;
    }

    public List<ElevationPoint> getElevationProfile() {

        return elevationProfile;
    }
}
