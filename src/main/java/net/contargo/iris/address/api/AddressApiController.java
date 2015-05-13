package net.contargo.iris.address.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressDtoService;
import net.contargo.iris.api.AbstractController;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


/**
 * Controller for {@link net.contargo.iris.address.dto.AddressDto}s.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Controller
@Api(description = "API for querying addresses.", value = "")
public class AddressApiController extends AbstractController {

    public static final String METHOD_ADDRESS_BY_GEOLOCATION = "addressByGeolocation";
    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final AddressDtoService addressDtoService;

    @Autowired
    public AddressApiController(AddressDtoService addressDtoService) {

        this.addressDtoService = addressDtoService;
    }

    @ApiOperation(
        value = "Get addresses for a given OpenStreetMap-ID.", notes = "Get addresses for a given OpenStreetMap-ID."
    )
    @ModelAttribute("geoCodeResponse")
    @RequestMapping(value = OSM_ADDRESSES + SLASH + ID_PARAM, method = RequestMethod.GET)
    public ListOfAddressListsResponse addressByOsmId(
        @ApiParam(value = "ID identifying a single osm-address.", required = true)
        @PathVariable(ID)
        long osmId) {

        AddressDto address = addressDtoService.getAddressByOsmId(osmId);

        ListOfAddressListsResponse response = new ListOfAddressListsResponse(addressDtoService.wrapInListOfAddressLists(
                    address));

        LOG.info("API: Responding to geocode-request for OSM ID {}  with {} Blocks", osmId,
            response.getAddresses().size());

        return response;
    }


    // do not remove the last slash of the url, or the longitude decimals will be cut off
    @ApiOperation(value = "Get address for the given geolocation.", notes = "Get address for the given geolocation.")
    @ModelAttribute("reverseGeocodeResponse")
    @RequestMapping(
        value = REVERSE_GEOCODE + SLASH + PARAM_LATITUDE + COLON + PARAM_LONGITUDE + SLASH, method = RequestMethod.GET
    )
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
    @RequestMapping(value = GEOCODES, method = RequestMethod.GET)
    public ListOfAddressListsResponse addressesByAddressDetails(@RequestParam(required = false) String street,
        @RequestParam(required = false, value = "postalcode") String postalCode,
        @RequestParam(required = false, value = "city") String city,
        @RequestParam(required = false, value = "country") String country,
        @RequestParam(required = false, value = "name") String name, HttpServletRequest request) {

        Map<String, String> addressDetails = putRequestParamsToMap(street, postalCode, city, country, name);

        ListOfAddressListsResponse response = new ListOfAddressListsResponse(addressDtoService.getAddressesByDetails(
                    addressDetails));

        response.add(linkTo(getClass()).slash(GEOCODES + "?" + request.getQueryString()).withSelfRel());

        LOG.info("API: Responding to request for address by address details: {}", addressDetails.toString());

        return response;
    }


    @ApiOperation(value = "Search addresses by address details.", notes = "Search addresses by address details.")
    @ModelAttribute("simpleGeoCodeResponse")
    @RequestMapping(value = SIMPLE_GEOCODES, method = RequestMethod.GET)
    public AddressListResponse addressesByAddressDetailsPlain(@RequestParam(required = false) String street,
        @RequestParam(required = false, value = "postalcode") String postalCode,
        @RequestParam(required = false, value = "city") String city,
        @RequestParam(required = false, value = "country") String country,
        @RequestParam(required = false, value = "name") String name, HttpServletRequest request) {

        Map<String, String> addressDetails = putRequestParamsToMap(street, postalCode, city, country, name);

        List<AddressDto> addressList = addressDtoService.getAddressesByDetailsPlain(addressDetails);

        AddressListResponse response = new AddressListResponse(addressList);

        response.add(linkTo(getClass()).slash(SIMPLE_GEOCODES + "?" + request.getQueryString()).withSelfRel());

        LOG.info("API: Responding with " + addressList.size() + " addresses to request " + addressDetails.toString());

        return response;
    }


    @ApiOperation(
        value = "Get all addresses belonging to the given OpenStreetMap-Place-ID.",
        notes = "Get all addresses belonging to the given OpenStreetMap-Place-ID."
    )
    @ModelAttribute("addresses")
    @RequestMapping(value = PLACES + SLASH + ID_PARAM + SLASH + ADDRESSES, method = RequestMethod.GET)
    public List<AddressDto> addressesWherePlaceIsIn(
        @ApiParam(value = "ID identifying a osm-place-id.", required = true)
        @PathVariable(ID)
        long placeId) {

        LOG.info("API: Responding to request for addresses by place id {}", placeId);

        return addressDtoService.getAddressesWherePlaceIsIn(placeId);
    }


    private Map<String, String> putRequestParamsToMap(String street, String postalCode, String city, String country,
        String name) {

        Map<String, String> addressDetails = new HashMap<>();

        if (city != null) {
            addressDetails.put(CITY.getKey(), city);
        }

        if (country != null) {
            addressDetails.put(COUNTRY.getKey(), country);
        }

        if (postalCode != null) {
            addressDetails.put(POSTAL_CODE.getKey(), postalCode);
        }

        if (street != null) {
            addressDetails.put(STREET.getKey(), street);
        }

        if (name != null) {
            addressDetails.put(NAME.getKey(), name);
        }

        return addressDetails;
    }
}
