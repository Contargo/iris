package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.util.HttpUtilException;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link NominatimAddressService}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class NominatimAddressServiceUnitTest {

    private static final int OSM_ID = 77;
    private static final String DUMMY_URL = "dummyUrl";
    private static final long OSM_PLACE_ID = 99L;

    private static final String urlNormal = "street, postalCode, city, country";

    private static final String urlByName = "null, postalCode, city, country, name";
    private static final String urlByStreet = "street, postalCode, city, country, null";

    private NominatimAddressService sut;

    @Mock
    private NominatimUrlBuilder nominatimUrlBuilderMock;
    @Mock
    private NominatimJsonResponseParser nominatimResponderMock;
    @Mock
    private AddressSorter addressSorterMock;
    @Mock
    private AddressValidator addressValidatorMock;
    @Mock
    private AddressServiceHelper addressHelperMock;

    private Map<String, String> addressDetails;
    private Address a1;
    private Address a2;
    private Address a3;
    private Address a4;

    @Before
    public void setup() {

        sut = new NominatimAddressService(nominatimUrlBuilderMock, nominatimResponderMock, addressSorterMock,
                addressHelperMock, addressValidatorMock);

        a1 = new Address();
        a1.setOsmId(1L);

        a2 = new Address();
        a2.setOsmId(2L);

        a3 = new Address();
        a3.setOsmId(3L);

        a4 = new Address();
        a4.setOsmId(4L);

        // the simple case
        when(nominatimResponderMock.getAddressesForUrl(urlNormal)).thenReturn(asList(a1, a2, a3, a4));

        // the complex case: searching by name
        when(nominatimResponderMock.getAddressesForUrl(urlByName)).thenReturn(asList(a3, a4));
        when(nominatimResponderMock.getAddressesForUrl(urlByStreet)).thenReturn(asList(a1, a3));

        when(addressSorterMock.compare(any(Address.class), any(Address.class))).thenReturn(1);
        when(addressValidatorMock.validateStreet(anyString())).then(returnsFirstArg());
        when(addressHelperMock.mergeSearchResultsWithoutDuplications(anyListOf(Address.class),
                    anyListOf(Address.class))).then(returnsFirstArg());

        addressDetails = new HashMap<>();
        addressDetails.put(CITY.getKey(), "city");
        addressDetails.put(STREET.getKey(), "street");
        addressDetails.put(POSTAL_CODE.getKey(), "postalcode");
        addressDetails.put(NAME.getKey(), "name");
        addressDetails.put(COUNTRY.getKey(), "country");
    }


    @Test
    public void testResolveSimpleCaseNameIsNull() {

        when(nominatimUrlBuilderMock.buildUrl("karlstrasse 68", "76137", "karlsruhe", "de", null)).thenReturn(
            urlNormal);

        List<Address> addresses = sut.resolveWithNominatim("karlstrasse 68", "76137", "karlsruhe", "de", null);

        assertThat(addresses, hasSize(4));
        assertThat(addresses, hasItems(a1, a2, a3, a4));
    }


    @Test
    public void testResolveSimpleCaseStreetIsNull() {

        when(nominatimUrlBuilderMock.buildUrl(null, "76137", "karlsruhe", "de", "synyx")).thenReturn(urlNormal);

        List<Address> addresses = sut.resolveWithNominatim(null, "76137", "karlsruhe", "de", "synyx");

        assertThat(addresses, hasSize(4));
        assertThat(addresses, hasItems(a1, a2, a3, a4));
    }


    @Test
    public void testResolveComplexCase() {

        when(nominatimUrlBuilderMock.buildUrl("karlstrasse 68", "76137", "karlsruhe", "de", null)).thenReturn(
            urlByStreet);
        when(nominatimUrlBuilderMock.buildUrl(null, "76137", "karlsruhe", "de", "synyx")).thenReturn(urlByName);

        List<Address> addresses = sut.resolveWithNominatim("karlstrasse 68", "76137", "karlsruhe", "de", "synyx");

        // Show that there is no duplication: Address a3 is only one time in List
        assertThat(addresses, hasSize(2));
        assertThat(addresses, hasItems(a3, a4));
    }


    @Test
    public void reverseLookupAddress() {

        BigDecimal lat = new BigDecimal(48.07506);
        BigDecimal lon = new BigDecimal(8.6362987);
        GeoLocation geoLocation = new GeoLocation(lat, lon);

        Address address = new Address(lat, lon);

        when(nominatimUrlBuilderMock.buildUrl(geoLocation)).thenReturn("url");
        when(nominatimResponderMock.getAddressForUrl("url")).thenReturn(address);

        Address result = sut.getAddressByGeolocation(geoLocation);

        assertThat(result.getLatitude(), closeTo(lat, new BigDecimal("0.001")));
        assertThat(result.getLongitude(), closeTo(lon, new BigDecimal("0.001")));

        verify(nominatimUrlBuilderMock).buildUrl(geoLocation);
        verify(nominatimResponderMock).getAddressForUrl("url");
    }


    @Test
    public void testGetAddressByOsmId() {

        Address expectedAddress = new Address();
        expectedAddress.setDisplayName("Alleinstellungsmerkmal für die expectedAddress");

        when(nominatimUrlBuilderMock.buildOsmUrl(OSM_ID)).thenReturn(DUMMY_URL);
        when(nominatimResponderMock.getAddressesForUrl(DUMMY_URL)).thenReturn(asList(expectedAddress));

        Address actualAddress = sut.getAddressByOsmId(OSM_ID);

        assertThat(actualAddress, is(expectedAddress));
    }


    @Test
    public void testGetAdressesWherePlaceIsIn() {

        Address expectedAddress1 = new Address();
        expectedAddress1.setDisplayName("Alleinstellungsmerkmal für die expectedAddress1");

        Address expectedAddress2 = new Address();
        expectedAddress2.setDisplayName("Alleinstellungsmerkmal für die expectedAddress2");

        List<Address> expectedAddresses = asList(expectedAddress1, expectedAddress2);

        when(nominatimUrlBuilderMock.buildSuburbUrl(OSM_PLACE_ID, SuburbType.ADDRESSES.getType())).thenReturn(
            DUMMY_URL);
        when(nominatimResponderMock.getAddressesForUrl(DUMMY_URL)).thenReturn(asList(expectedAddress1,
                expectedAddress2));

        List<Address> actualAddresses = sut.getAdressesWherePlaceIsIn(OSM_PLACE_ID);
        assertThat(actualAddresses, is(expectedAddresses));
    }


    @Test
    public void testResolveWithFallbacks() {

        Address expectedAddress1 = new Address();
        expectedAddress1.setDisplayName("Alleinstellungsmerkmal für die expectedAddress1");

        Address expectedAddress2 = new Address();
        expectedAddress2.setDisplayName("Alleinstellungsmerkmal für die expectedAddress2");

        when(nominatimUrlBuilderMock.buildSuburbUrl(OSM_PLACE_ID, SuburbType.ADDRESSES.getType())).thenReturn(
            DUMMY_URL);
        when(nominatimResponderMock.getAddressesForUrl(anyString())).thenReturn(asList(expectedAddress1,
                expectedAddress2));

        List<Address> actualList = sut.getAddressesByDetails(addressDetails);
        assertThat(actualList, contains(expectedAddress1, expectedAddress2));
    }


    @Test
    public void testResolveWithFallbacksNoAddressesFound() {

        Address expectedAddress1 = new Address();
        expectedAddress1.setDisplayName("Alleinstellungsmerkmal für die expectedAddress1");

        Address expectedAddress2 = new Address();
        expectedAddress2.setDisplayName("Alleinstellungsmerkmal für die expectedAddress2");

        when(nominatimUrlBuilderMock.buildSuburbUrl(OSM_PLACE_ID, SuburbType.ADDRESSES.getType())).thenReturn(
            DUMMY_URL);
        when(nominatimResponderMock.getAddressesForUrl(anyString())).thenReturn(Collections.<Address>emptyList());

        List<Address> actualList = sut.getAddressesByDetails(addressDetails);
        assertThat(actualList, empty());
    }


    @Test(expected = AddressResolutionException.class)
    public void getAddressByGeolocationIllegalArgument() {

        when(nominatimUrlBuilderMock.buildUrl(a1)).thenThrow(new IllegalArgumentException());
        sut.getAddressByGeolocation(a1);
    }


    @Test(expected = AddressResolutionException.class)
    public void getAddressByGeolocationHttpUtilError() {

        when(nominatimUrlBuilderMock.buildUrl(a1)).thenReturn("foo");
        when(nominatimResponderMock.getAddressForUrl("foo")).thenThrow(new HttpUtilException("", new Throwable()));
        sut.getAddressByGeolocation(a1);
    }
}
