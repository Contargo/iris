package net.contargo.iris.address.staticsearch.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressListDto;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import static java.util.Arrays.asList;


/**
 * Unit test for the {@link net.contargo.iris.address.staticsearch.dto.StaticAddressDtoServiceImpl}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticAddressDtoServiceImplUnitTest {

    private static final String CITY = "city";
    private static final String POSTALCODE = "postalcode";
    private static final String COUNTRY = "country";

    @Mock
    private StaticAddressService staticAddressServiceMock;
    private StaticAddressDtoServiceImpl sut;
    private StaticAddress staticAddress;
    private StaticAddress staticAddress2;
    private Address address;

    @Before
    public void setUp() throws Exception {

        sut = new StaticAddressDtoServiceImpl(staticAddressServiceMock);
        staticAddress = new StaticAddress();
        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(BigDecimal.ONE);
        staticAddress.setCity(CITY);
        staticAddress.setPostalcode(POSTALCODE);
        staticAddress.setCountry(COUNTRY);
        staticAddress.setUniqueId(BigInteger.TEN);
        staticAddress2 = new StaticAddress();
        staticAddress2.setUniqueId(BigInteger.ONE);
        address = new Address();
        address.setLatitude(staticAddress.getLatitude());
        address.setLongitude(staticAddress.getLongitude());
    }


    @Test
    public void testGetAddressesByDetails() {

        when(staticAddressServiceMock.getAddressesByDetails(POSTALCODE, CITY, COUNTRY)).thenReturn(asList(
                staticAddress));

        List<AddressDto> actualList = sut.getAddressesByDetails(POSTALCODE, CITY, COUNTRY);

        assertThat(actualList.size(), is(1));
        assertThat(actualList.get(0).getAddress().get("city"), is(CITY));
        assertThat(actualList.get(0).getAddress().get("postcode"), is(POSTALCODE));
        assertThat(actualList.get(0).getCountryCode(), is(COUNTRY));
    }


    @Test
    public void testGetStaticAddressByGeolocation() {

        GeoLocation location = new GeoLocation(BigDecimal.ONE, BigDecimal.TEN);

        when(staticAddressServiceMock.getAddressListListForGeolocation(location)).thenReturn(asList(
                new AddressList("name1", asList(address))));

        List<AddressListDto> expected = asList(new AddressListDto("name1", asList(new AddressDto(address))));
        assertReflectionEquals(sut.getStaticAddressByGeolocation(location), expected);
    }


    @Test
    public void getStaticAddressByBoundingBox() {

        when(staticAddressServiceMock.getAddressesInBoundingBox(address, 20d)).thenReturn(asList(staticAddress,
                staticAddress2));

        List<BigInteger> uids = sut.getStaticAddressByBoundingBox(address, 20d);

        assertThat(uids, contains(BigInteger.TEN, BigInteger.ONE));
    }
}
