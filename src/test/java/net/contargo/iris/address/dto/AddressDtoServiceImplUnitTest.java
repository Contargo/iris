package net.contargo.iris.address.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.service.AddressServiceWrapper;

import org.junit.Before;
import org.junit.Test;

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
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * Unit test of {@link AddressDtoServiceImpl}.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
public class AddressDtoServiceImplUnitTest {

    private static final int OSM_ID = 1;
    private static final GeoLocation GEOLOCATION = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);
    private static final Long PLACE_ID = 1L;

    private final AddressService addressServiceMock = mock(AddressService.class);
    private final AddressServiceWrapper addressServiceWrapperMock = mock(AddressServiceWrapper.class);
    private final AddressDtoServiceImpl sut = new AddressDtoServiceImpl(addressServiceMock, addressServiceWrapperMock);

    private final Address address = new Address(BigDecimal.ONE, BigDecimal.ONE);
    private final Map<String, String> addressDetails = new HashMap<>();

    @Before
    public void setUp() {

        addressDetails.put(CITY.getKey(), "city");
        addressDetails.put(STREET.getKey(), "street");
        addressDetails.put(POSTAL_CODE.getKey(), "postalcode");
        addressDetails.put(NAME.getKey(), "name");
        addressDetails.put(COUNTRY.getKey(), "country");
    }


    @Test
    public void testGetAddressByOsmId() {

        when(addressServiceMock.getAddressByOsmId(OSM_ID)).thenReturn(address);

        AddressDto expectedAddress = new AddressDto(address);
        AddressDto actualAddress = sut.getAddressByOsmId(OSM_ID);
        assertReflectionEquals(actualAddress, expectedAddress);
    }


    @Test
    public void testGetAddressByOsmIdNull() {

        AddressDto actualAddress = sut.getAddressByOsmId(OSM_ID);
        assertThat(actualAddress, nullValue());
    }


    @Test
    public void testWrapInListOfAddressLists() {

        Address address = new Address("Karlsruhe");
        AddressDto addressDto = new AddressDto(address);
        List<AddressListDto> actualListOfLists = sut.wrapInListOfAddressLists(addressDto);
        assertThat(actualListOfLists.get(0).getAddresses().get(0), is(addressDto));
    }


    @Test
    public void testReverseLookupAddress() {

        when(addressServiceWrapperMock.getAddressForGeoLocation(GEOLOCATION)).thenReturn(address);

        AddressDto expectedAddress = new AddressDto(address);
        AddressDto actualAddress = sut.getAddressForGeoLocation(GEOLOCATION);
        assertReflectionEquals(actualAddress, expectedAddress);
    }


    @Test
    public void testReverseLookupAddressNull() {

        AddressDto actualAddress = sut.getAddressForGeoLocation(GEOLOCATION);
        assertThat(actualAddress, nullValue());
    }


    @Test
    public void testGetAddressesByDetails() {

        Address secondAddress = new Address(BigDecimal.TEN, BigDecimal.TEN);
        List<AddressList> addressListList = asList(new AddressList("name1", singletonList(address)),
                new AddressList("name2", singletonList(secondAddress)));
        when(addressServiceWrapperMock.getAddressesByDetails(addressDetails)).thenReturn(addressListList);

        List<AddressListDto> expectedAddressListDtoList = asList(new AddressListDto("name1",
                    singletonList(new AddressDto(address))),
                new AddressListDto("name2", singletonList(new AddressDto(secondAddress))));

        List<AddressListDto> actualAddressListDtoList = sut.getAddressesByDetails(addressDetails);
        assertThat(actualAddressListDtoList.size(), is(expectedAddressListDtoList.size()));
        assertReflectionEquals(actualAddressListDtoList.get(0).getParentAddress(),
            expectedAddressListDtoList.get(0).getParentAddress());
        assertReflectionEquals(actualAddressListDtoList.get(0).getAddresses(),
            expectedAddressListDtoList.get(0).getAddresses());
        assertReflectionEquals(actualAddressListDtoList.get(1).getParentAddress(),
            expectedAddressListDtoList.get(1).getParentAddress());
        assertReflectionEquals(actualAddressListDtoList.get(1).getAddresses(),
            expectedAddressListDtoList.get(1).getAddresses());
    }


    @Test
    public void testGetAddressesByDetailsPlain() {

        Address secondAddress = new Address(BigDecimal.TEN, BigDecimal.TEN);
        List<AddressList> addressListList = asList(new AddressList("name1", singletonList(address)),
                new AddressList("name2", singletonList(secondAddress)));
        when(addressServiceWrapperMock.getAddressesByDetails(addressDetails)).thenReturn(addressListList);

        List<AddressDto> expectedAddressDtoList = asList(new AddressDto(address), new AddressDto(secondAddress));

        List<AddressDto> actualAddressDtoList = sut.getAddressesByDetailsPlain(addressDetails);
        assertReflectionEquals(actualAddressDtoList, expectedAddressDtoList);
    }


    @Test
    public void testGetAddressesWherePlaceIsIn() {

        Address secondAddress = new Address(BigDecimal.TEN, BigDecimal.TEN);
        when(addressServiceMock.getAddressesWherePlaceIsIn(PLACE_ID)).thenReturn(asList(address, secondAddress));

        List<AddressDto> expectedAddressList = asList(new AddressDto(address), new AddressDto(secondAddress));

        List<AddressDto> actualAddressList = sut.getAddressesWherePlaceIsIn(PLACE_ID);
        assertReflectionEquals(actualAddressList, expectedAddressList);
    }


    @Test
    public void getAddressesByHashKey() {

        String hashKey = "ABCDE";
        String displayName = "displayName";
        Address address = new Address(displayName);
        when(addressServiceWrapperMock.getByHashKey(hashKey)).thenReturn(address);

        AddressDto addressDto = sut.getAddressesByHashKey(hashKey);
        assertThat(addressDto.getDisplayName(), is(displayName));
    }


    @Test
    public void getAddressesByQuery() {

        address.setDisplayName("Gartenstr. 67, Karlsruhe (Südweststadt)");

        when(addressServiceWrapperMock.getAddressesByQuery("Gartenstraße 67, Karlsruhe")).thenReturn(singletonList(
                address));

        List<AddressDto> addresses = sut.getAddressesByQuery("Gartenstraße 67, Karlsruhe");

        assertThat(addresses, hasSize(1));
        assertThat(addresses.get(0).getDisplayName(), is("Gartenstr. 67, Karlsruhe (Südweststadt)"));
    }
}
