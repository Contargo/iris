package net.contargo.iris.gis.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.gis.service.GisService;

import java.math.BigDecimal;

import static net.contargo.iris.gis.dto.AirlineDistanceUnit.METER;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class AirlineDistanceDtoServiceImpl implements AirlineDistanceDtoService {

    private GisService gisService;

    AirlineDistanceDtoServiceImpl() {

        // a nor-arg constructor must be available
    }


    public AirlineDistanceDtoServiceImpl(GisService gisService) {

        this.gisService = gisService;
    }

    @Override
    public AirlineDistanceDto calcAirLineDistInMeters(String alat, String alon, String blat, String blon) {

        // the scope identifies the distance that gets calculated
        String scope = "?alat=" + alat + "&alon=" + alon
            + "&blat=" + blat + "&blon=" + blon;

        GeoLocation a = new GeoLocation(new BigDecimal(alat), new BigDecimal(alon));
        GeoLocation b = new GeoLocation(new BigDecimal(blat), new BigDecimal(blon));

        return new AirlineDistanceDto(gisService.calcAirLineDistInMeters(a, b), METER, scope);
    }
}
