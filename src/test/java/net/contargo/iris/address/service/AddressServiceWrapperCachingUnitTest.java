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

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


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
    private AddressListFilter addressListFilterMock;

    // object to test
    private AddressServiceWrapper sut;
    private AddressCache addressCache;

    @Before
    public void setup() {

        addressService = mock(AddressService.class);
        staticAddressServiceMock = mock(StaticAddressService.class);
        addressCache = mock(AddressCache.class);
        addressListFilterMock = mock(AddressListFilter.class);

        NormalizerService normalizerServiceMock = mock(NormalizerService.class);
        when(normalizerServiceMock.normalize(CITY_NAME)).thenReturn(CITY_NAME_NORMALIZED);

        sut = new AddressServiceWrapper(addressService, staticAddressServiceMock, addressCache, normalizerServiceMock,
                addressListFilterMock);
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

        List<Address> addresses = singletonList(a);
        when(addressService.getAddressesByDetails(addressDetails)).thenReturn(addresses);
        when(addressListFilterMock.filterOutByCountryCode(any(), eq("CH"))).thenAnswer(invocation -> invocation.getArguments()[0]);

        List<AddressList> expectedList = new ArrayList<>();
        AddressList expectedAddressList = new AddressList(a, addresses);
        expectedList.add(null);
        expectedList.add(expectedAddressList);

        sut.getAddressesByDetails(addressDetails);

        verify(addressCache).cache(expectedList);
    }


    @Test
    public void testCaching() {

        Address a = new Address();
        a.setOsmId(123L);
        a.setLatitude(new BigDecimal(49.01542167885));
        a.setLongitude(new BigDecimal(8.425742155221));

        GeoLocation loc = new GeoLocation(a.getLatitude(), a.getLongitude());
        when(addressCache.getForLocation(loc)).thenReturn(a);
        when(addressListFilterMock.filterOutByCountryCode(any(), eq("CH"))).thenAnswer(invocation -> invocation.getArguments()[0]);

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(CITY.getKey(), CITY_NAME);
        addressDetails.put(COUNTRY.getKey(), "Deutschland");
        addressDetails.put(STREET.getKey(), "foo");
        addressDetails.put(POSTAL_CODE.getKey(), "42");
        addressDetails.put(NAME.getKey(), "bla");
        sut.getAddressesByDetails(addressDetails);

        Address b = sut.getAddressForGeoLocation(loc);

        Assert.assertEquals(a, b);
    }


    @Test
    public void testAlsoChecksStaticAddressesBeforeCache() {

        Address a = new Address();
        a.setOsmId(123L);
        a.setLatitude(new BigDecimal(49.01542167885));
        a.setLongitude(new BigDecimal(8.425742155221));

        StaticAddress sa = mock(StaticAddress.class);

        when(sa.toAddress()).thenReturn(a);

        GeoLocation loc = new GeoLocation(a.getLatitude(), a.getLongitude());

        when(addressCache.getForLocation(loc)).thenReturn(null);
        when(staticAddressServiceMock.getForLocation(loc)).thenReturn(sa);

        Address b = sut.getAddressForGeoLocation(loc);

        Assert.assertEquals(a, b);
    }
}
