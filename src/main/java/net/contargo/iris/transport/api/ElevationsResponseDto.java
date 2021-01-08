package net.contargo.iris.transport.api;

import net.contargo.iris.transport.elevation.Elevations;
import net.contargo.iris.transport.elevation.dto.ElevationPoint;
import net.contargo.iris.units.Distance;

import java.util.List;

import static net.contargo.iris.units.LengthUnit.METER;

import static java.util.stream.Collectors.toList;


/**
 * @author  Petra Scherer - scherer@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class ElevationsResponseDto {

    private final List<ElevationPointDto> elevations;

    public ElevationsResponseDto(Elevations elevations) {

        this.elevations = elevations.getElevationProfile().stream()
                .map(e -> new ElevationPointDto(elevationInMeter(e), summedDistanceInMeter(e)))
                .collect(toList());
    }

    private static Distance elevationInMeter(ElevationPoint elevationPoint) {

        return new Distance(elevationPoint.getElevation(), METER);
    }


    private static Distance summedDistanceInMeter(ElevationPoint elevationPoint) {

        return new Distance(elevationPoint.getSummedDistance(), METER);
    }


    public List<ElevationPointDto> getElevations() {

        return elevations;
    }
}
