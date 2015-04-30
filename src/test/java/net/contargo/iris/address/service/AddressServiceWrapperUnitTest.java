package net.contargo.iris.address.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.normalizer.NormalizerService;

import org.hamcrest.Matchers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


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
    private AddressServiceWrapper addressServiceWrapper;
    @Mock
    private AddressService addressServiceMock;
    @Mock
    private StaticAddressService staticAddressServiceMock;
    @Mock
    private AddressCache cache;
    @Mock
    private NormalizerService normalizerServiceMock;
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

        addressServiceWrapper = new AddressServiceWrapper(addressServiceMock, staticAddressServiceMock, cache,
                normalizerServiceMock);
    }


    /**
     * Test of getAddressesByDetailsWithFallbacks method, of class AddressServiceWrapper.
     */
    @Test
    public void testResolveReturnsStaticAddressesAsFirstElement() {

        AddressList value = Mockito.mock(AddressList.class);
        Mockito.when(staticAddressServiceMock.findAddresses(addressDetails.get(POSTAL_CODE.getKey()),
                CITYNAME_KARLSRUHE_NORMALIZED, addressDetails.get(COUNTRY.getKey()))).thenReturn(value);

        addressDetails.put(CITY.getKey(), CITYNAME_KARLSRUHE);

        List<AddressList> result = addressServiceWrapper.getAddressesByDetails(addressDetails);

        Mockito.verify(staticAddressServiceMock).findAddresses(addressDetails.get(POSTAL_CODE.getKey()),
            CITYNAME_KARLSRUHE_NORMALIZED, addressDetails.get(COUNTRY.getKey()));

        Assert.assertEquals(result.get(0), value);
    }


    @Test
    public void testGetAddressForGeoLocationAddressReturnedByAddressService() {

        GeoLocation loc = new GeoLocation();
        loc.setLatitude(BigDecimal.valueOf(LATITUDE));
        loc.setLongitude(BigDecimal.valueOf(LONGITUDE));

        Mockito.when(addressServiceMock.getAddressByGeolocation(Mockito.any(GeoLocation.class))).thenReturn(
            new Address());
        Mockito.when(staticAddressServiceMock.getForLocation(Mockito.any(GeoLocation.class))).thenReturn(null);
        Mockito.when(cache.getForLocation(loc)).thenReturn(null);

        Address expectedAddress = new Address();
        expectedAddress.setLongitude(loc.getLongitude());
        expectedAddress.setLatitude(loc.getLatitude());

        Assert.assertEquals(expectedAddress, addressServiceWrapper.getAddressForGeoLocation(loc));
    }


    /**
     * Test of getAddressesByDetailsWithFallbacks method, of class AddressServiceWrapper.
     */
    @Test
    public void resolvesAddressAndThenSuburbsWithStreet() {

        Address a = new Address();
        a.setOsmId(23L);

        Mockito.when(addressServiceMock.getAddressesByDetails(addressDetails)).thenReturn(Arrays.asList(a));

        Address suburb = new Address();
        suburb.setDisplayName("suburb of address");
        suburb.setOsmId(42L);

        List<AddressList> result = addressServiceWrapper.getAddressesByDetails(addressDetails);

        Assert.assertThat(result.size(), Matchers.equalTo(2));
        Assert.assertThat(result.get(1).getAddresses().size(), Matchers.equalTo(1)); // only address
        Assert.assertThat(result.get(1).getAddresses().get(0), Matchers.equalTo(a));

        Mockito.verify(cache).cache(result);
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

        Mockito.when(addressServiceMock.getAddressesByDetails(detailsNeustadt)).thenReturn(Arrays.asList(neustadtHessen,
                neustadtSachsen));
        when(normalizerServiceMock.normalize(CITYNAME_NEUSTADT)).thenReturn(CITYNAME_NEUSTADT_NORMALIZED);

        List<AddressList> addresses = addressServiceWrapper.getAddressesByDetails(detailsNeustadt);

        Assert.assertEquals(3, addresses.size());
        Assert.assertThat(addresses.get(1).getParentAddress(), Matchers.equalTo(neustadtHessen));
        Assert.assertThat(addresses.get(2).getParentAddress(), Matchers.equalTo(neustadtSachsen));
    }


    @Test
    public void getAddressForGeoLocation() {

        Address address = new Address();
        GeoLocation geoLocation = new GeoLocation(new BigDecimal(48.07506), new BigDecimal(8.6362987));
        when(cache.getForLocation(geoLocation)).thenReturn(address);

        assertThat(addressServiceWrapper.getAddressForGeoLocation(geoLocation), is(address));
        verifyZeroInteractions(addressServiceMock);
    }


    @Test
    public void getAddressForGeoLocationCacheMiss() {

        GeoLocation geoLocation = new GeoLocation(new BigDecimal(48.07506), new BigDecimal(8.6362987));
        when(cache.getForLocation(geoLocation)).thenReturn(null); // cache miss

        addressServiceWrapper.getAddressForGeoLocation(geoLocation);
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

        addressServiceWrapper.getAddressesByDetails(details);

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

        addressServiceWrapper.getAddressesByDetails(details);

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
        addressServiceWrapper.getAddressesByDetails(details);

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
        addressServiceWrapper.getAddressesByDetails(details);

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
        addressServiceWrapper.getAddressesByDetails(details);

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
        addressServiceWrapper.getAddressesByDetails(details);

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
        addressServiceWrapper.getAddressesByDetails(details);

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
        addressServiceWrapper.getAddressesByDetails(details);

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
        addressServiceWrapper.getAddressesByDetails(details);
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
        addressServiceWrapper.getAddressesByDetails(details);
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
        addressServiceWrapper.getAddressesByDetails(details);
        verifyZeroInteractions(addressServiceMock);
    }
}
