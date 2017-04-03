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

import org.mockito.runners.MockitoJUnitRunner;

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

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


/**
 * Tests Caching-Aspects of {@link AddressServiceWrapper}.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressServiceWrapperCachingUnitTest {

    public static final String CITY_NAME = "bar";
    public static final String CITY_NAME_NORMALIZED = "BAR";

    private AddressServiceWrapper sut;

    @Mock
    private AddressService addressServiceMock;
    @Mock
    private StaticAddressService staticAddressServiceMock;
    @Mock
    private AddressCache addressCacheMock;

    @Before
    public void setup() {

        NormalizerService normalizerServiceMock = mock(NormalizerService.class);
        when(normalizerServiceMock.normalize(CITY_NAME)).thenReturn(CITY_NAME_NORMALIZED);

        sut = new AddressServiceWrapper(addressServiceMock, staticAddressServiceMock, addressCacheMock,
                normalizerServiceMock);
    }


    @Test
    public void getAddressForGeoLocationCachesOnRetrieve() {

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
        when(addressServiceMock.getAddressesByDetails(addressDetails)).thenReturn(addresses);

        List<AddressList> expectedList = new ArrayList<>();
        AddressList expectedAddressList = new AddressList(a, addresses);
        expectedList.add(null);
        expectedList.add(expectedAddressList);

        sut.getAddressesByDetails(addressDetails);

        verify(addressCacheMock).cache(expectedList);
    }


    @Test
    public void getAddressForGeoLocationCaching() {

        Address expectedAddress = new Address();
        expectedAddress.setOsmId(123L);
        expectedAddress.setLatitude(new BigDecimal(49.01542167885));
        expectedAddress.setLongitude(new BigDecimal(8.425742155221));

        GeoLocation loc = new GeoLocation(expectedAddress.getLatitude(), expectedAddress.getLongitude());
        when(addressCacheMock.getForLocation(loc)).thenReturn(expectedAddress);

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(CITY.getKey(), CITY_NAME);
        addressDetails.put(COUNTRY.getKey(), "Deutschland");
        addressDetails.put(STREET.getKey(), "foo");
        addressDetails.put(POSTAL_CODE.getKey(), "42");
        addressDetails.put(NAME.getKey(), "bla");
        sut.getAddressesByDetails(addressDetails);

        Address addressForGeoLocation = sut.getAddressForGeoLocation(loc);

        assertThat(addressForGeoLocation, is(expectedAddress));
    }


    @Test
    public void getAddressForGeoLocationAlsoChecksStaticAddressesBeforeCache() {

        Address expectedAddress = new Address();
        expectedAddress.setOsmId(123L);
        expectedAddress.setLatitude(new BigDecimal(49.01542167885));
        expectedAddress.setLongitude(new BigDecimal(8.425742155221));

        StaticAddress staticAddressMock = mock(StaticAddress.class);
        when(staticAddressMock.toAddress()).thenReturn(expectedAddress);

        GeoLocation loc = new GeoLocation(expectedAddress.getLatitude(), expectedAddress.getLongitude());
        when(addressCacheMock.getForLocation(loc)).thenReturn(null);

        when(staticAddressServiceMock.getForLocation(loc)).thenReturn(staticAddressMock);

        Address addressForGeoLocation = sut.getAddressForGeoLocation(loc);

        assertThat(addressForGeoLocation, is(expectedAddress));
    }
}
