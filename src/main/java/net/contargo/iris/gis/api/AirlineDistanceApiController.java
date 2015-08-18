package net.contargo.iris.gis.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.gis.dto.AirlineDistanceDto;
import net.contargo.iris.gis.dto.AirlineDistanceDtoService;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * Implements an RESTful geocode interface.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
@Api(value = AirlineDistanceApiController.AIRLINE_DISTANCE, description = "API for calculating airline distances.")
@Controller
@RequestMapping(AirlineDistanceApiController.AIRLINE_DISTANCE)
public class AirlineDistanceApiController {

    static final String AIRLINE_DISTANCE = "airlineDistance";

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final BigDecimal LAT_MIN = new BigDecimal(-90);
    private static final BigDecimal LAT_MAX = new BigDecimal(90);
    private static final BigDecimal LON_MIN = new BigDecimal(-180);
    private static final BigDecimal LON_MAX = new BigDecimal(180);

    private final AirlineDistanceDtoService airlineDistanceDtoService;
    private final AirlineDistanceResponseAssembler responseAssembler;

    @Autowired
    public AirlineDistanceApiController(AirlineDistanceDtoService airlineDistanceDtoService,
        AirlineDistanceResponseAssembler responseAssembler) {

        this.airlineDistanceDtoService = airlineDistanceDtoService;
        this.responseAssembler = responseAssembler;
    }

    @ApiOperation(
        value = "Calculates the airline distance between two geographical points in meters.",
        notes = "Calculates the airline distance between two geographical points in meters."
    )
    @RequestMapping(method = GET)
    @ModelAttribute(AIRLINE_DISTANCE)
    public AirlineDistanceResponse airlineDistance(@RequestParam("alat") String aLatitude,
        @RequestParam("alon") String aLongitude,
        @RequestParam("blat") String bLatitude,
        @RequestParam("blon") String bLongitude) {

        validateGeoCoordinates(aLatitude, aLongitude, bLatitude, bLongitude);

        AirlineDistanceDto airlineDistanceDto = airlineDistanceDtoService.calcAirLineDistInMeters(aLatitude, aLongitude,
                bLatitude, bLongitude);
        LOG.info("API: Responding to request for airline distance between geographic locations");

        return responseAssembler.toResource(airlineDistanceDto);
    }


    private void validateGeoCoordinates(String alat, String alon, String blat, String blon) {

        validateLatitude(alat);
        validateLongitude(alon);
        validateLatitude(blat);
        validateLongitude(blon);
    }


    private void validateLatitude(String lat) {

        // may throw a NumberFormatException
        BigDecimal latitude = new BigDecimal(lat);

        if (latitude.compareTo(LAT_MIN) == -1 || latitude.compareTo(LAT_MAX) == 1) {
            throw new IllegalArgumentException();
        }
    }


    private void validateLongitude(String lon) {

        // may throw a NumberFormatException
        BigDecimal longitude = new BigDecimal(lon);

        if (longitude.compareTo(LON_MIN) == -1 || longitude.compareTo(LON_MAX) == 1) {
            throw new IllegalArgumentException();
        }
    }
}
