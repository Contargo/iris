package net.contargo.iris.address.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressNotFoundException;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.normalizer.NormalizerService;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


/**
 * Wrapper class to have better control about the resolving methods of {@link AddressService}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class AddressServiceWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(AddressServiceWrapper.class);
    private static final Pattern HASHKEY_MATCHER = Pattern.compile("^[A-Z0-9]{5}$");

    private final AddressService addressService;
    private final StaticAddressService staticAddressService;
    private final AddressCache addressCache;
    private final NormalizerService normalizerService;

    public AddressServiceWrapper(AddressService addressService, StaticAddressService staticAddressService,
        AddressCache cache, NormalizerService normalizerService) {

        this.addressService = addressService;
        this.staticAddressService = staticAddressService;
        this.addressCache = cache;
        this.normalizerService = normalizerService;
    }

    /**
     * Searches an {@link Address} by the given parameters of the {@link GeoLocation} and returns it.
     *
     * @param  geoLocation  basis for the search of the {@link Address}
     *
     * @return  The address for the given {@link GeoLocation}.
     */
    public Address getAddressForGeoLocation(GeoLocation geoLocation) {

        Address address = addressCache.getForLocation(geoLocation);

        if (address != null) {
            return address;
        }

        StaticAddress staticAddress = staticAddressService.getForLocation(geoLocation);

        if (staticAddress != null) {
            return staticAddress.toAddress();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Cache miss: {}", geoLocation);
        }

        address = addressService.getAddressByGeolocation(geoLocation);

        if (address != null) {
            // the geo coordinates in the returned 'address' object differ from
            // the geo coordinates in the 'loc' object
            address.setLatitude(geoLocation.getLatitude());
            address.setLongitude(geoLocation.getLongitude());
            addressCache.cache(getSimpleAddressList(singletonList(address)));
        }

        return address;
    }


    /**
     * Searches with the given parameters from the addressDetails map like street, postalcode and city the
     * geocoordinates and returns a list of {@link AddressList} with their geocoordinates. To improve the result
     * quality of geocoding services, it's sometimes better to make the search not with all the given parameters. (e.g.
     * try to get address using only street and city)
     *
     * @param  addressDetails  keeps the search information
     *
     * @return  list of {@link AddressList}
     */
    public List<AddressList> getAddressesByDetails(Map<String, String> addressDetails) {

        List<AddressList> result = new ArrayList<>();

        String cityNormalized = normalizerService.normalize(addressDetails.get(CITY.getKey()));
        String postalCode = addressDetails.get(POSTAL_CODE.getKey());
        String country = addressDetails.get(COUNTRY.getKey());
        String street = addressDetails.get(STREET.getKey());

        if (StringUtils.isNotEmpty(postalCode) || StringUtils.isNotEmpty(cityNormalized)) {
            result.add(resolveByStaticAddressService(postalCode, cityNormalized, country));
        }

        if (street != null && !"".equals(street)) {
            List<AddressList> nominatimResult = resolveByNominatim(addressDetails);
            result.addAll(nominatimResult);
        }

        addressCache.cache(result);

        return result;
    }


    public Address getByHashKey(String hashKey) {

        return staticAddressService.findByHashKey(hashKey).toAddress();
    }


    List<AddressList> getAddressesBasedOnStaticAddressResolution(String postalCode, String city, String countryCode) {

        List<AddressList> result = new ArrayList<>();

        if (StringUtils.isNotEmpty(postalCode) || StringUtils.isNotEmpty(city)) {
            result.add(resolveByStaticAddressService(postalCode, city, countryCode));
        }

        addressCache.cache(result);

        return result;
    }


    private AddressList resolveByStaticAddressService(String postalCode, String city, String country) {

        return staticAddressService.findAddresses(postalCode, city, country);
    }


    public List<AddressList> getAddressesBasedOnNominatimResolution(Map<String, String> parameters) {

        List<AddressList> result = resolveByNominatim(parameters);
        addressCache.cache(result);

        return result;
    }


    private List<AddressList> resolveByNominatim(Map<String, String> addressDetails) {

        List<Address> addresses = addressService.getAddressesByDetails(addressDetails);

        if (addresses == null || addresses.isEmpty()) {
            LOG.info("No results for city " + addressDetails.get(CITY.getKey()) + " and country "
                + addressDetails.get(COUNTRY.getKey())
                + ". Returning empty result.");

            return Collections.emptyList();
        }

        return getSimpleAddressList(addresses);
    }


    private static List<AddressList> getSimpleAddressList(List<Address> addresses) {

        List<AddressList> addressListList = new ArrayList<>();

        for (Address address : addresses) {
            List<Address> result = new ArrayList<>();
            result.add(address);

            AddressList addressesList = new AddressList(address, result);
            addressListList.add(addressesList);
        }

        return addressListList;
    }


    @SuppressWarnings("squid:S1166")
    public List<Address> getAddressesByQuery(String query) {

        List<Address> addresses = new ArrayList<>();

        if (HASHKEY_MATCHER.matcher(query).matches()) {
            try {
                addresses.add(getByHashKey(query));
            } catch (StaticAddressNotFoundException e) {
                // ignoring the exception as the query could be resolved by nominatim
            }
        }

        if (addresses.isEmpty()) {
            addresses.addAll(addressService.getAddressesByQuery(query));

            List<Address> matchingStaticAddresses = addresses.stream()
                    .map(a ->
                                staticAddressService.findAddresses(a.getPostcode(), a.getCity(), a.getCountryCode())
                                .getAddresses())
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(toList());

            addresses.addAll(matchingStaticAddresses);
        }

        return addresses;
    }
}
