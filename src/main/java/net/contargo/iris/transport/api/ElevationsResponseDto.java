package net.contargo.iris.transport.api;

import net.contargo.iris.transport.elevation.Elevations;
import net.contargo.iris.units.Distance;
import net.contargo.iris.units.LengthUnit;

import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * @author  Petra Scherer - scherer@synyx.de
 */
public class ElevationsResponseDto {

    private final List<ElevationPointDto> elevations;

    ElevationsResponseDto(Elevations elevations) {

        this.elevations = elevations.getElevationProfile().stream().map(e ->
                        new ElevationPointDto(new Distance(e.getElevation(), LengthUnit.METER),
                            new Distance(e.getSummedDistance(), LengthUnit.METER))).collect(toList());
    }

    public List<ElevationPointDto> getElevations() {

        return elevations;
    }
}

class ElevationPointDto {

    private Distance elevation;
    private Distance summedDistance;

    ElevationPointDto(Distance elevation, Distance summedDistance) {

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
