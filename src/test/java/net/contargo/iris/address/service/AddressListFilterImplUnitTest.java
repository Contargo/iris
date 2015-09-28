package net.contargo.iris.address.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.contargo.iris.address.Address.COUNTRY_CODE;

import static org.hamcrest.Matchers.is;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.junit.Assert.assertThat;

import static java.util.Collections.singletonList;


/**
 * Unit test of {@link AddressListFilterImpl}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class AddressListFilterImplUnitTest {

    private AddressListFilterImpl sut;

    private Address swissAddress;
    private Address germanAddress;

    @Before
    public void setUp() {

        sut = new AddressListFilterImpl();
        swissAddress = new Address();

        Map<String, String> swissAddressMap = new HashMap<>();
        swissAddressMap.put(COUNTRY_CODE, "ch");
        swissAddress.setAddress(swissAddressMap);

        germanAddress = new Address();

        Map<String, String> germanAddressMap = new HashMap<>();
        germanAddressMap.put(COUNTRY_CODE, "de");
        germanAddress.setAddress(germanAddressMap);
    }


    @Test
    public void filterByCountryCodeOneEmptyAddressList() {

        AddressList addressList = new AddressList("foo", Collections.emptyList());

        List<AddressList> addressesList = new ArrayList<>();
        addressesList.add(addressList);

        List<AddressList> result = sut.filterOutByCountryCode(addressesList, "CH");

        assertThat(result, hasSize(1));
    }


    @Test
    public void filterByCountryCodeOneCHAddressList() {

        AddressList addressList = new AddressList("foo", singletonList(swissAddress));

        List<AddressList> addressesList = new ArrayList<>();
        addressesList.add(addressList);

        List<AddressList> result = sut.filterOutByCountryCode(addressesList, "CH");

        assertThat(result, hasSize(0));
    }


    @Test
    public void filterByCountryCodeOneDEAddressList() {

        AddressList addressList = new AddressList("foo", singletonList(germanAddress));

        List<AddressList> addressesList = new ArrayList<>();
        addressesList.add(addressList);

        List<AddressList> result = sut.filterOutByCountryCode(addressesList, "CH");

        assertThat(result, hasSize(1));
    }


    @Test
    public void filterByCountryCodeCHAndDEAddress() {

        AddressList germanAddressList = new AddressList("germany", singletonList(germanAddress));

        AddressList swissAddressList = new AddressList("swiss", singletonList(swissAddress));

        List<AddressList> addressList = new ArrayList<>();
        addressList.add(swissAddressList);
        addressList.add(germanAddressList);

        List<AddressList> result = sut.filterOutByCountryCode(addressList, "CH");

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(germanAddressList));
    }


    @Test
    public void filterByCountryCodeCountryCodeInAddressNotSet() {

        AddressList germanAddressList = new AddressList("germany", singletonList(germanAddress));
        germanAddress.getAddress().put(COUNTRY_CODE, null);

        List<AddressList> addressList = new ArrayList<>();
        addressList.add(germanAddressList);

        List<AddressList> result = sut.filterOutByCountryCode(addressList, "CH");

        assertThat(result, hasSize(1));
    }


    @Test
    public void isAddressOfCountryLowerCase() {

        boolean result = sut.isAddressOfCountry(swissAddress, "ch");

        assertThat(result, is(true));
    }


    @Test
    public void isAddressOfCountryUpperCase() {

        boolean result = sut.isAddressOfCountry(swissAddress, "CH");

        assertThat(result, is(true));
    }


    @Test
    public void isAddressOfCountryWithoutCountryCode() {

        Address address = new Address();

        boolean result = sut.isAddressOfCountry(address, "CH");

        assertThat(result, is(false));
    }


    @Test
    public void isAddressOfCountryWrongCountry() {

        boolean result = sut.isAddressOfCountry(swissAddress, "DE");

        assertThat(result, is(false));
    }
}
