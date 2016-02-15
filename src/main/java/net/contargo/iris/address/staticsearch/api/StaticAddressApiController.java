package net.contargo.iris.address.staticsearch.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.api.ListOfAddressListsResponse;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.staticsearch.dto.StaticAddressDtoService;
import net.contargo.iris.address.staticsearch.dto.StaticAddressesUidResponse;
import net.contargo.iris.address.staticsearch.service.StaticAddressNotFoundException;
import net.contargo.iris.api.RestApiErrorDto;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;

import java.util.Collection;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * Controller for the public API of {@link net.contargo.iris.address.dto.AddressDto}s that belong to static addresses.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
@Api(value = "/staticaddresses", description = "API for interaction with static addresses.")
@Controller
@RequestMapping("/staticaddresses")
public class StaticAddressApiController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final String LAT = "lat";
    private static final String LON = "lon";

    private final StaticAddressDtoService staticAddressDtoService;

    @Autowired
    public StaticAddressApiController(StaticAddressDtoService staticAddressDtoService) {

        this.staticAddressDtoService = staticAddressDtoService;
    }

    @ApiOperation(
        value = "Returns the static addresses filtered with the given hashkey",
        notes = "Returns the static addresses filtered with the given hashkey", response = AddressDto.class
    )
    @RequestMapping(method = GET, params = { "hashkey" })
    @ResponseBody
    public AddressDto getByHashKey(@RequestParam(value = "hashkey") String hashKey) {

        return staticAddressDtoService.getStaticAddressByHashKey(hashKey);
    }


    @ApiOperation(
        value = "Returns all static addresses filtered by postalcode, city and country.",
        notes = "Returns all static addresses filtered by postalcode, city and country.", response = AddressDto.class,
        responseContainer = "List"
    )
    @RequestMapping(method = GET, params = { "postalCode", "city", "country" })
    @ResponseBody
    public Collection<AddressDto> getByPostalCodeAndCityAndCountry(
        @RequestParam(value = "postalCode") String postalCode,
        @RequestParam(value = "city") String city,
        @RequestParam(value = "country") String country) {

        return staticAddressDtoService.getAddressesByDetails(postalCode, city, country);
    }


    @ApiOperation(
        value = "Returns all static addresses filtered by the given geolocation.",
        notes = "Returns all static addresses filtered by the given geolocation."
    )
    @ModelAttribute("geoCodeResponse")
    @RequestMapping(method = GET, params = { LAT, LON })
    public ListOfAddressListsResponse getStaticAddressByGeolocation(@RequestParam(LAT) BigDecimal latitude,
        @RequestParam(LON) BigDecimal longitude) {

        GeoLocation location = new GeoLocation(latitude, longitude);
        ListOfAddressListsResponse response = new ListOfAddressListsResponse(
                staticAddressDtoService.getStaticAddressByGeolocation(location));

        LOG.info("API: Responding to geocode-request for geolocation {} with {} Blocks", location.toString(),
            response.getAddresses().size());

        return response;
    }


    @ApiOperation(
        value = "Returns a list of static address uids that are located in a bounding box with a given radius.",
        notes = "Returns a list of static address uids that are located in a bounding box with a given radius."
    )
    @RequestMapping(method = GET, params = { LAT, LON, "distance" })
    @ResponseBody
    public StaticAddressesUidResponse staticAddressesByBoundingBox(@RequestParam(LAT) BigDecimal latitude,
        @RequestParam(LON) BigDecimal longitude,
        @RequestParam("distance") Double distance) {

        GeoLocation location = new GeoLocation(latitude, longitude);

        StaticAddressesUidResponse response = new StaticAddressesUidResponse(
                staticAddressDtoService.getStaticAddressByBoundingBox(location, distance));

        LOG.info("API: Responding with {} items to boundingbox-request for geolocation {} with {} distance",
            response.getUids().size(), location.toString(), distance);

        response.add(linkTo(methodOn(getClass()).staticAddressesByBoundingBox(latitude, longitude, distance))
            .withSelfRel());

        return response;
    }


    @ExceptionHandler(StaticAddressNotFoundException.class)
    ResponseEntity<RestApiErrorDto> handleStaticAddressNotFoundException(StaticAddressNotFoundException e) {

        return new ResponseEntity<>(new RestApiErrorDto(e.getErrorCode(), e.getMessage(), null), NOT_FOUND);
    }
}
