package net.contargo.iris.address.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.normalizer.NormalizerService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Tests Caching-Aspects of AddressServiceWrapper.
 *
 * @author  Sven Mueller - mueller@synyx.de
 */
public class AddressServiceWrapperCachingUnitTest {

    public static final String CITY_NAME = "bar";
    public static final String CITY_NAME_NORMALIZED = "BAR";

    // mocked
    private AddressService addressService;

    private StaticAddressService staticAddressServiceMock;

    // object to test
    private AddressServiceWrapper addressServiceWrapper;
    private AddressCache addressCache;

    @Before
    public void setup() {

        addressService = Mockito.mock(AddressService.class);
        staticAddressServiceMock = Mockito.mock(StaticAddressService.class);
        addressCache = Mockito.mock(AddressCache.class);

        NormalizerService normalizerServiceMock = mock(NormalizerService.class);
        when(normalizerServiceMock.normalize(CITY_NAME)).thenReturn(CITY_NAME_NORMALIZED);

        addressServiceWrapper = new AddressServiceWrapper(addressService, staticAddressServiceMock, addressCache,
                normalizerServiceMock);
    }


    @Test
    public void cachesOnRetrieve() {

        Address a = new Address();
        a.setLatitude(new BigDecimal(49.01542167885));
        a.setLongitude(new BigDecimal(8.425742155221));

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(CITY.getKey(), CITY_NAME);
        addressDetails.put(COUNTRY.getKey(), "Deutschland");
        addressDetails.put(STREET.getKey(), "foo");
        addressDetails.put(POSTAL_CODE.getKey(), "42");
        addressDetails.put(NAME.getKey(), "bla");

        List<Address> addresses = Arrays.asList(a);
        Mockito.when(addressService.getAddressesByDetails(addressDetails)).thenReturn(addresses);

        List<AddressList> expectedList = new ArrayList<>();
        AddressList expectedAddressList = new AddressList(a, addresses);
        expectedList.add(null);
        expectedList.add(expectedAddressList);

        addressServiceWrapper.getAddressesByDetails(addressDetails);

        Mockito.verify(addressCache).cache(expectedList);
    }


    @Test
    public void testCaching() {

        Address a = new Address();
        a.setOsmId(123L);
        a.setLatitude(new BigDecimal(49.01542167885));
        a.setLongitude(new BigDecimal(8.425742155221));

        GeoLocation loc = new GeoLocation(a.getLatitude(), a.getLongitude());
        Mockito.when(addressCache.getForLocation(loc)).thenReturn(a);

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(CITY.getKey(), CITY_NAME);
        addressDetails.put(COUNTRY.getKey(), "Deutschland");
        addressDetails.put(STREET.getKey(), "foo");
        addressDetails.put(POSTAL_CODE.getKey(), "42");
        addressDetails.put(NAME.getKey(), "bla");
        addressServiceWrapper.getAddressesByDetails(addressDetails);

        Address b = addressServiceWrapper.getAddressForGeoLocation(loc);

        Assert.assertEquals(a, b);
    }


    @Test
    public void testAlsoChecksStaticAddressesBeforeCache() {

        Address a = new Address();
        a.setOsmId(123L);
        a.setLatitude(new BigDecimal(49.01542167885));
        a.setLongitude(new BigDecimal(8.425742155221));

        StaticAddress sa = Mockito.mock(StaticAddress.class);

        Mockito.when(sa.toAddress()).thenReturn(a);

        GeoLocation loc = new GeoLocation(a.getLatitude(), a.getLongitude());

        Mockito.when(addressCache.getForLocation(loc)).thenReturn(null);
        Mockito.when(staticAddressServiceMock.getForLocation(loc)).thenReturn(sa);

        Address b = addressServiceWrapper.getAddressForGeoLocation(loc);

        Assert.assertEquals(a, b);
    }
}
