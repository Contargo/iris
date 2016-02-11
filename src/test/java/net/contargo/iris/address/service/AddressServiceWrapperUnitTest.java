package net.contargo.iris.address.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.normalizer.NormalizerService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * Unit test for {@link net.contargo.iris.address.service.AddressServiceWrapper}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressServiceWrapperUnitTest {

    private static final double LATITUDE = 11.0;
    private static final double LONGITUDE = 12.0;
    public static final String CITYNAME_NEUSTADT = "Neustadt";
    public static final String CITYNAME_NEUSTADT_NORMALIZED = "NEUSTADT";
    public static final String CITYNAME_KARLSRUHE = "Karlsruhe";
    public static final String CITYNAME_KARLSRUHE_NORMALIZED = "KARLSRUHE";
    public static final String CITYNAME_NOT_NORMALIZED_AT_ALL = "%4/()&32";
    public static final String STREETNAME_KARLSTRASSE = "Karlstrasse";
    public static final String POSTAL_CODE_76133 = "76133";
    public static final String POSTAL_CODE_76137 = "76137";

    private AddressServiceWrapper sut;

    @Mock
    private AddressService addressServiceMock;
    @Mock
    private StaticAddressService staticAddressServiceMock;
    @Mock
    private AddressCache cacheMock;
    @Mock
    private NormalizerService normalizerServiceMock;
    @Mock
    private AddressListFilter addressListFilterMock;

    private Map<String, String> addressDetails;

    @Before
    public void setUp() {

        addressDetails = new HashMap<>();
        addressDetails.put(CITY.getKey(), "city");
        addressDetails.put(COUNTRY.getKey(), "country");
        addressDetails.put(STREET.getKey(), "street");
        addressDetails.put(POSTAL_CODE.getKey(), "postalCode");
        addressDetails.put(NAME.getKey(), "name");

        when(normalizerServiceMock.normalize(CITYNAME_KARLSRUHE)).thenReturn(CITYNAME_KARLSRUHE_NORMALIZED);

        sut = new AddressServiceWrapper(addressServiceMock, staticAddressServiceMock, cacheMock, normalizerServiceMock,
                addressListFilterMock);
    }


    @Test
    public void resolveReturnsStaticAddressesAsFirstElement() {

        AddressList value = Mockito.mock(AddressList.class);
        when(staticAddressServiceMock.findAddresses(addressDetails.get(POSTAL_CODE.getKey()),
                    CITYNAME_KARLSRUHE_NORMALIZED, addressDetails.get(COUNTRY.getKey()))).thenReturn(value);

        addressDetails.put(CITY.getKey(), CITYNAME_KARLSRUHE);

        List<AddressList> result = sut.getAddressesByDetails(addressDetails);

        verify(staticAddressServiceMock).findAddresses(addressDetails.get(POSTAL_CODE.getKey()),
            CITYNAME_KARLSRUHE_NORMALIZED, addressDetails.get(COUNTRY.getKey()));

        assertThat(result.get(0), is(value));
    }


    @Test
    public void getAddressForGeoLocationAddressReturnedByAddressService() {

        GeoLocation loc = new GeoLocation();
        loc.setLatitude(BigDecimal.valueOf(LATITUDE));
        loc.setLongitude(BigDecimal.valueOf(LONGITUDE));

        when(addressServiceMock.getAddressByGeolocation(any(GeoLocation.class))).thenReturn(new Address());
        when(staticAddressServiceMock.getForLocation(any(GeoLocation.class))).thenReturn(null);
        when(cacheMock.getForLocation(loc)).thenReturn(null);

        Address expectedAddress = new Address();
        expectedAddress.setLongitude(loc.getLongitude());
        expectedAddress.setLatitude(loc.getLatitude());

        assertThat(sut.getAddressForGeoLocation(loc), is(expectedAddress));
    }


    @Test
    public void resolvesAddressAndThenSuburbsWithStreet() {

        Address a = new Address();
        a.setOsmId(23L);

        when(addressServiceMock.getAddressesByDetails(addressDetails)).thenReturn(singletonList(a));
        when(addressListFilterMock.filterOutByCountryCode(any(), eq("CH"))).thenAnswer(invocation ->
                invocation.getArguments()[0]);

        Address suburb = new Address();
        suburb.setDisplayName("suburb of address");
        suburb.setOsmId(42L);

        List<AddressList> result = sut.getAddressesByDetails(addressDetails);

        assertThat(result.size(), is(2));
        assertThat(result.get(1).getAddresses(), hasSize(1));
        assertThat(result.get(1).getAddresses().get(0), is(a));

        verify(cacheMock).cache(result);
    }


    @Test
    public void testSuburbSearchWithStreet() {

        Address neustadtHessen = new Address();
        neustadtHessen.setPlaceId(23L);
        neustadtHessen.setOsmId(23L);
        neustadtHessen.setDisplayName(
            "Neustadt, Marburg-Biedenkopf, Regierungsbezirk Gießen, Hessen, 35279, Deutschland, Europa");
        neustadtHessen.setShortName(CITYNAME_NEUSTADT);

        Address neustadtSachsen = new Address();
        neustadtSachsen.setPlaceId(42L);
        neustadtSachsen.setOsmId(42L);
        neustadtSachsen.setDisplayName("Neustadt, Dresden, Sachsen, Deutschland");
        neustadtSachsen.setShortName(CITYNAME_NEUSTADT);

        Map<String, String> detailsNeustadt = new HashMap<>();
        detailsNeustadt.put(CITY.getKey(), CITYNAME_NEUSTADT);
        detailsNeustadt.put(STREET.getKey(), STREETNAME_KARLSTRASSE);
        detailsNeustadt.put(POSTAL_CODE.getKey(), null);
        detailsNeustadt.put(COUNTRY.getKey(), "DE");
        detailsNeustadt.put(NAME.getKey(), null);

        List<Address> nominatimAddresses = asList(neustadtHessen, neustadtSachsen);
        when(addressServiceMock.getAddressesByDetails(detailsNeustadt)).thenReturn(nominatimAddresses);
        when(addressListFilterMock.filterOutByCountryCode(any(), eq("CH"))).thenAnswer(invocation ->
                invocation.getArguments()[0]);
        when(normalizerServiceMock.normalize(CITYNAME_NEUSTADT)).thenReturn(CITYNAME_NEUSTADT_NORMALIZED);

        List<AddressList> addresses = sut.getAddressesByDetails(detailsNeustadt);

        assertThat(addresses, hasSize(3));
        assertThat(addresses.get(1).getParentAddress(), is(neustadtHessen));
        assertThat(addresses.get(2).getParentAddress(), is(neustadtSachsen));
    }


    @Test
    public void getAddressForGeoLocation() {

        Address address = new Address();
        GeoLocation geoLocation = new GeoLocation(new BigDecimal(48.07506), new BigDecimal(8.6362987));
        when(cacheMock.getForLocation(geoLocation)).thenReturn(address);

        Address addressForGeoLocation = sut.getAddressForGeoLocation(geoLocation);

        assertThat(addressForGeoLocation, is(address));
        verifyZeroInteractions(addressServiceMock);
    }


    @Test
    public void getAddressForGeoLocationCacheMiss() {

        GeoLocation geoLocation = new GeoLocation(new BigDecimal(48.07506), new BigDecimal(8.6362987));
        when(cacheMock.getForLocation(geoLocation)).thenReturn(null); // cache miss

        sut.getAddressForGeoLocation(geoLocation);

        verify(addressServiceMock).getAddressByGeolocation(geoLocation);
    }


    @Test
    public void testDoesNotExecuteStaticAddressSearchIfOnlyStreetIsGiven() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), null);
        details.put(STREET.getKey(), STREETNAME_KARLSTRASSE);
        details.put(POSTAL_CODE.getKey(), null);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verifyZeroInteractions(staticAddressServiceMock);
    }


    @Test
    public void testDoesExecuteStaticAddressSearchIfPostalcodeIsGiven() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), null);
        details.put(STREET.getKey(), null);
        details.put(POSTAL_CODE.getKey(), POSTAL_CODE_76133);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verify(staticAddressServiceMock).findAddresses(POSTAL_CODE_76133, null, null);
    }


    @Test
    public void testDoesExecuteStaticAddressSearchIfStreetAndPostalcodeIsGiven() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), null);
        details.put(STREET.getKey(), STREETNAME_KARLSTRASSE);
        details.put(POSTAL_CODE.getKey(), POSTAL_CODE_76133);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verify(staticAddressServiceMock).findAddresses(POSTAL_CODE_76133, null, null);
    }


    @Test
    public void testDoesExecuteStaticAddressSearchIfCityIsGiven() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), CITYNAME_KARLSRUHE);
        details.put(STREET.getKey(), null);
        details.put(POSTAL_CODE.getKey(), null);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verify(staticAddressServiceMock).findAddresses(null, CITYNAME_KARLSRUHE_NORMALIZED, null);
    }


    @Test
    public void testDoesExecuteStaticAddressSearchIfStreetAndCityIsGiven() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), CITYNAME_KARLSRUHE);
        details.put(STREET.getKey(), STREETNAME_KARLSTRASSE);
        details.put(POSTAL_CODE.getKey(), null);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verify(staticAddressServiceMock).findAddresses(null, CITYNAME_KARLSRUHE_NORMALIZED, null);
    }


    @Test
    public void testDoesExecuteStaticAddressSearchIfPostalcodeAndCityIsGiven() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), CITYNAME_KARLSRUHE);
        details.put(STREET.getKey(), null);
        details.put(POSTAL_CODE.getKey(), POSTAL_CODE_76133);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verify(staticAddressServiceMock).findAddresses(POSTAL_CODE_76133, CITYNAME_KARLSRUHE_NORMALIZED, null);
    }


    @Test
    public void testDoesExecuteStaticAddressSearchAndNominatimSearchIfStreetAndPostalcodeIsGiven() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), null);
        details.put(STREET.getKey(), STREETNAME_KARLSTRASSE);
        details.put(POSTAL_CODE.getKey(), POSTAL_CODE_76137);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verify(staticAddressServiceMock).findAddresses(POSTAL_CODE_76137, null, null);
        verify(addressServiceMock).getAddressesByDetails(details);
    }


    @Test
    public void testDoesExecuteStaticAddressSearchAndNominatimSearchIfStreetAndPostalcodeAndCityIsGiven() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), CITYNAME_KARLSRUHE);
        details.put(STREET.getKey(), STREETNAME_KARLSTRASSE);
        details.put(POSTAL_CODE.getKey(), POSTAL_CODE_76137);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verify(staticAddressServiceMock).findAddresses(POSTAL_CODE_76137, CITYNAME_KARLSRUHE_NORMALIZED, null);
        verify(addressServiceMock).getAddressesByDetails(details);
    }


    @Test
    public void testDoesNotExecuteStaticAddressSearchIfCityGetsNormalizedToNull() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), CITYNAME_NOT_NORMALIZED_AT_ALL);
        details.put(STREET.getKey(), STREETNAME_KARLSTRASSE);
        details.put(POSTAL_CODE.getKey(), null);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        when(normalizerServiceMock.normalize(CITYNAME_NOT_NORMALIZED_AT_ALL)).thenReturn("");

        sut.getAddressesByDetails(details);

        verifyZeroInteractions(staticAddressServiceMock);
    }


    @Test
    public void testDoesNotExecuteStaticAddressSearchIfStreetIsNull() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), null);
        details.put(STREET.getKey(), null);
        details.put(POSTAL_CODE.getKey(), POSTAL_CODE_76137);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verifyZeroInteractions(addressServiceMock);
    }


    @Test
    public void testDoesNotExecuteStaticAddressSearchIfStreetIsEmpty() {

        Map<String, String> details = new HashMap<>();
        details.put(CITY.getKey(), null);
        details.put(STREET.getKey(), "");
        details.put(POSTAL_CODE.getKey(), POSTAL_CODE_76137);
        details.put(COUNTRY.getKey(), null);
        details.put(NAME.getKey(), null);

        sut.getAddressesByDetails(details);

        verifyZeroInteractions(addressServiceMock);
    }


    @Test
    public void getByHashKey() {

        String hashkey = "ABCDE";
        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setCountry("DE");
        when(staticAddressServiceMock.findByHashKey(hashkey)).thenReturn(staticAddress);

        Address address = sut.getByHashKey(hashkey);
        assertThat(address.getCountryCode(), is("DE"));
    }
}
