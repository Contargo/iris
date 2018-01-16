package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;

import org.slf4j.Logger;

import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * An implementation of the {@link AddressService} interface using Nominatim to resolveWithNominatim an address to
 * geocoordinates and jackson to generate Java objects from Json. Nominatim: http://nominatim.openstreetmap.org
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public class NominatimAddressService implements AddressService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private static final int ARGUMENT_4 = 4;
    private static final int ARGUMENT_3 = 3;
    private static final int ARGUMENT_2 = 2;
    private static final int ARGUMENT_1 = 1;
    private static final int ARGUMENT_0 = 0;

    private final NominatimUrlBuilder nominatimUrlBuilder;
    private final NominatimJsonResponseParser nominatimResponder;

    private final AddressSorter addressSorter;
    private final AddressServiceHelper addressHelper;
    private final AddressValidator addressValidator;

    public NominatimAddressService(NominatimUrlBuilder nominatimUrlBuilder,
        NominatimJsonResponseParser nominatimResponder, AddressSorter addressSorter,
        AddressServiceHelper addressHelper, AddressValidator addressValidator) {

        this.nominatimUrlBuilder = nominatimUrlBuilder;
        this.nominatimResponder = nominatimResponder;
        this.addressSorter = addressSorter;
        this.addressHelper = addressHelper;
        this.addressValidator = addressValidator;
    }

    List<Address> resolveWithNominatim(String street, String postalCode, String city, String country, String name) {

        /*
         * Please notice that Nominatim has no search results if query contains name and street.
         * If name should be a search parameter street and streetNumber have to be ignored.
         */

        if (StringUtils.hasText(name) && StringUtils.hasText(street)) {
            return geocodeByName(street, postalCode, city, country, name);
        } else {
            String url = nominatimUrlBuilder.buildUrl(street, postalCode, city, country, name);

            List<Address> addresses = nominatimResponder.getAddresses(url);

            Collections.sort(addresses, addressSorter);

            return addresses;
        }
    }


    @Override
    public List<Address> getAddressesWherePlaceIsIn(Long placeId) {

        return searchSuburbsViaNominatimsDetailPage(placeId, SuburbType.ADDRESSES, new HashSet<>());
    }


    @Override
    public Address getAddressByOsmId(long osmId) {

        // Note that both, OSM Id and OSM Type are required for the Nominatim reverse search
        // first try using OSM Type WAY
        String url = nominatimUrlBuilder.buildOsmUrl(osmId, OsmType.WAY);
        List<Address> addresses = nominatimResponder.getAddressesFromOSMId(url);

        if (addresses.get(0) == null || addresses.get(0).getOsmId() != osmId) {
            // second try using OSM Type NODE
            url = nominatimUrlBuilder.buildOsmUrl(osmId, OsmType.NODE);
            addresses = nominatimResponder.getAddressesFromOSMId(url);
        }

        return addresses.get(0);
    }


    @Override
    public Address getAddressByGeolocation(GeoLocation geoLocation) {

        try {
            String url = nominatimUrlBuilder.buildUrl(geoLocation);

            return nominatimResponder.getAddress(url);
        } catch (IllegalArgumentException e) {
            throw new AddressResolutionException("Failed to resolve address for " + geoLocation, e);
        }
    }


    @Override
    public List<Address> getAddressesByQuery(String query) {

        String url = nominatimUrlBuilder.buildSearchUrl(query);

        return nominatimResponder.getAddresses(url);
    }


    private List<Address> searchSuburbsViaNominatimsDetailPage(Long osmPlaceId, SuburbType suburbType,
        Set<String> suburbGlobalDisplayNames) {

        List<Address> suburbs = new ArrayList<>();

        String suburbUrl = nominatimUrlBuilder.buildSuburbUrl(osmPlaceId, suburbType.getType());
        List<Address> foundSuburbs = nominatimResponder.getAddresses(suburbUrl);

        if (!foundSuburbs.isEmpty()) {
            // check for possible redundant address display names
            for (Address foundSuburb : foundSuburbs) {
                if (!foundSuburb.getDisplayName().contains("No Name")
                        && !suburbGlobalDisplayNames.contains(foundSuburb.getDisplayName())) {
                    // add to suburbs list
                    suburbs.add(foundSuburb);

                    // add to global display names, for next iteration
                    suburbGlobalDisplayNames.add(foundSuburb.getDisplayName());
                }
            }
        }

        return suburbs;
    }


    private List<Address> geocodeByName(String street, String postalCode, String city, String country, String name) {

        // make 2 queries: 1 query for search by name, 1 query for search by street
        String url1 = nominatimUrlBuilder.buildUrl(null, postalCode, city, country, name);
        String url2 = nominatimUrlBuilder.buildUrl(street, postalCode, city, country, null);

        List<Address> one = nominatimResponder.getAddresses(url1);
        List<Address> two = nominatimResponder.getAddresses(url2);

        // avoid duplication of places by osm_id
        List<Address> mergedList = addressHelper.mergeSearchResultsWithoutDuplications(one, two);

        // sort list so that no plausible results (e.g., not in europe) appear last
        Collections.sort(mergedList, addressSorter);

        return mergedList;
    }


    @Override
    public List<Address> getAddressesByDetails(Map<String, String> addressDetails) {

        String[][] resolvingStrategies = createResolvingStrategies(addressDetails.get(POSTAL_CODE.getKey()),
                addressDetails.get(CITY.getKey()), addressDetails.get(COUNTRY.getKey()),
                addressDetails.get(NAME.getKey()),
                addressValidator.validateStreet(addressDetails.get(STREET.getKey())));

        for (String[] args : resolvingStrategies) {
            List<Address> addresses = resolveWithNominatim(args[ARGUMENT_0], args[ARGUMENT_1], args[ARGUMENT_2],
                    args[ARGUMENT_3], args[ARGUMENT_4]);

            if (!addresses.isEmpty()) {
                return addresses;
            }
        }

        LOG.info("No results for city " + addressDetails.get(CITY.getKey()) + " and country "
            + addressDetails.get(COUNTRY.getKey())
            + ". Returning empty result.");

        return Collections.emptyList();
    }


    private static String[][] createResolvingStrategies(String postalCode, String city, String country, String name,
        String internStreet) {

        return new String[][] {
                { internStreet, postalCode, city, country, name },
                { internStreet, null, city, country, null },
                { null, postalCode, city, country, null },
                { null, null, city, country, null }
            };
    }
}
