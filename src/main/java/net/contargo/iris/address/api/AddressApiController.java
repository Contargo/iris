package net.contargo.iris.address.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressDtoService;
import net.contargo.iris.address.dto.AddressListDto;
import net.contargo.iris.address.nominatim.NominatimUtil;
import net.contargo.iris.address.staticsearch.service.StaticAddressNotFoundException;
import net.contargo.iris.address.staticsearch.validator.HashKeyValidator;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import static java.util.Collections.singletonList;


/**
 * Controller for {@link net.contargo.iris.address.dto.AddressDto}s.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@Controller
@Api(description = "API for querying addresses.", value = "")
public class AddressApiController {

    private static final String METHOD_ADDRESS_BY_GEOLOCATION = "addressByGeolocation";
    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final AddressDtoService addressDtoService;
    private final HashKeyValidator hashKeyValidator;

    @Autowired
    public AddressApiController(AddressDtoService addressDtoService, HashKeyValidator hashKeyValidator) {

        this.addressDtoService = addressDtoService;
        this.hashKeyValidator = hashKeyValidator;
    }

    @ApiOperation(
        value = "Get addresses for a given OpenStreetMap-ID.", notes = "Get addresses for a given OpenStreetMap-ID."
    )
    @ModelAttribute("geoCodeResponse")
    @RequestMapping(value = "osmaddresses/{id}", method = RequestMethod.GET)
    public ListOfAddressListsResponse addressByOsmId(
        @ApiParam(value = "ID identifying a single osm-address.", required = true)
        @PathVariable("id")
        long osmId) {

        AddressDto address = addressDtoService.getAddressByOsmId(osmId);

        ListOfAddressListsResponse response = new ListOfAddressListsResponse(
                addressDtoService.wrapInListOfAddressLists(address));

        LOG.info("API: Responding to geocode-request for OSM ID {}  with {} Blocks", osmId,
            response.getAddresses().size());

        return response;
    }


    // do not remove the last slash of the url, or the longitude decimals will be cut off
    @ApiOperation(value = "Get address for the given geolocation.", notes = "Get address for the given geolocation.")
    @ModelAttribute("reverseGeocodeResponse")
    @RequestMapping(value = "reversegeocode/{lat}:{lon}/", method = RequestMethod.GET)
    public AddressResponse addressByGeolocation(@PathVariable("lat") BigDecimal latitude,
        @PathVariable("lon") BigDecimal longitude) throws NoSuchMethodException {

        AddressDto address = addressDtoService.getAddressForGeoLocation(new GeoLocation(latitude, longitude));
        AddressResponse response = new AddressResponse(address);

        Method method = AddressApiController.class.getMethod(METHOD_ADDRESS_BY_GEOLOCATION, BigDecimal.class,
                BigDecimal.class);

        response.add(linkTo(method, latitude, longitude).slash(".").withSelfRel());

        LOG.info("API: Responding to request for address by geolocation with latitude {} and longitude {}", latitude,
            longitude);

        return response;
    }


    @ApiOperation(
        value = "Search list of addresses by address details.", notes = "Search list of addresses by address details."
    )
    @ModelAttribute("geoCodeResponse")
    @RequestMapping(value = "geocodes", method = RequestMethod.GET)
    public ListOfAddressListsResponse addressesByAddressDetails(@RequestParam(required = false) String street,
        @RequestParam(required = false, value = "postalcode") String postalCode,
        @RequestParam(required = false, value = "city") String city,
        @RequestParam(required = false, value = "country") String country,
        @RequestParam(required = false, value = "name") String name, HttpServletRequest request) {

        List<AddressListDto> addressListDtos = new ArrayList<>();

        if (hashKeyValidator.validate(street)) {
            try {
                List<AddressDto> address = singletonList(addressDtoService.getAddressesByHashKey(street));
                addressListDtos = singletonList(new AddressListDto("City and Suburb Results for hash key " + street,
                            address));
            } catch (StaticAddressNotFoundException e) {
                street = "";
                LOG.info("IRIS could not provide a static address with the hash key {}", street);
            }
        }

        Map<String, String> addressDetails = NominatimUtil.parameterMap(street, postalCode, city, country, name);

        if (addressListDtos.isEmpty()) {
            addressListDtos = addressDtoService.getAddressesByDetails(addressDetails);
        }

        ListOfAddressListsResponse response = new ListOfAddressListsResponse(addressListDtos);
        response.add(linkTo(getClass()).slash("geocodes?" + request.getQueryString()).withSelfRel());

        LOG.info("API: Responding to request for address by address details: {}", addressDetails.toString());

        return response;
    }


    @ApiOperation(value = "Search addresses by address details.", notes = "Search addresses by address details.")
    @ModelAttribute("simpleGeoCodeResponse")
    @RequestMapping(value = "simplegeocodes", method = RequestMethod.GET)
    public AddressListResponse addressesByAddressDetailsPlain(@RequestParam(required = false) String street,
        @RequestParam(required = false, value = "postalcode") String postalCode,
        @RequestParam(required = false, value = "city") String city,
        @RequestParam(required = false, value = "country") String country,
        @RequestParam(required = false, value = "name") String name, HttpServletRequest request) {

        Map<String, String> addressDetails = NominatimUtil.parameterMap(street, postalCode, city, country, name);

        List<AddressDto> addressList = addressDtoService.getAddressesByDetailsPlain(addressDetails);

        AddressListResponse response = new AddressListResponse(addressList);

        response.add(linkTo(getClass()).slash("simplegeocodes?" + request.getQueryString()).withSelfRel());

        LOG.info("API: Responding with " + addressList.size() + " addresses to request " + addressDetails.toString());

        return response;
    }


    @ApiOperation(
        value = "Get all addresses belonging to the given OpenStreetMap-Place-ID.",
        notes = "Get all addresses belonging to the given OpenStreetMap-Place-ID."
    )
    @ModelAttribute("addresses")
    @RequestMapping(value = "places/{id}/addresses", method = RequestMethod.GET)
    public List<AddressDto> addressesWherePlaceIsIn(
        @ApiParam(value = "ID identifying a osm-place-id.", required = true)
        @PathVariable("id")
        long placeId) {

        LOG.info("API: Responding to request for addresses by place id {}", placeId);

        return addressDtoService.getAddressesWherePlaceIsIn(placeId);
    }


    @ApiOperation(
        value = "Returns a list of matching addresses.",
        notes = "Can be static addresses or nominatim resolved addresses."
    )
    @ModelAttribute("addresses")
    @RequestMapping(value = "/addresses", method = GET, params = { "query" })
    public List<AddressDto> getAddresses(@RequestParam("query") String query) {

        return addressDtoService.getAddressesByQuery(query);
    }
}
