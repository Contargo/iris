package net.contargo.iris.transport.elevation.dto;

/**
 * @author  Petra Scherer - scherer@synyx.de
 */
public class ElevationPoint {

    private final Integer elevation;
    private final Integer summedDistance;

    public ElevationPoint(Integer elevation, Integer summedDistance) {

        this.elevation = elevation;
        this.summedDistance = summedDistance;
    }

    public Integer getElevation() {

        return elevation;
    }


    public Integer getSummedDistance() {

        return summedDistance;
    }
}
