package net.contargo.iris.transport.api;

import net.contargo.iris.units.Distance;


/**
 * @author  Petra Scherer - scherer@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class ElevationPointDto {

    private final Distance elevation;
    private final Distance summedDistance;

    public ElevationPointDto(Distance elevation, Distance summedDistance) {

        this.elevation = elevation;
        this.summedDistance = summedDistance;
    }

    public Distance getElevation() {

        return elevation;
    }


    public Distance getSummedDistance() {

        return summedDistance;
    }
}
