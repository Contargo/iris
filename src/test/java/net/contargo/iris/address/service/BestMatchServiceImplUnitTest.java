package net.contargo.iris.address.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.nominatim.service.AddressDetailKey;
import net.contargo.iris.address.staticsearch.StaticAddress;

import org.junit.Test;

import java.math.BigInteger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class BestMatchServiceImplUnitTest {

    private AddressServiceWrapper addressServiceWrapper = mock(AddressServiceWrapper.class);
    private BestMatchServiceImpl sut = new BestMatchServiceImpl(addressServiceWrapper);

    @Test
    public void bestMatchIsStaticAddress() {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setPostalcode("72810");
        staticAddress.setCity("GOMARINGEN");
        staticAddress.setCountry("DE");
        staticAddress.setUniqueId(new BigInteger("1301000000063529"));

        AddressList addressList = new AddressList("parent", singletonList(staticAddress.toAddress()));
        List<AddressList> list = singletonList(addressList);

        when(addressServiceWrapper.getAddressesBasedOnStaticAddressResolution("72810", "Gomaringen", "DE")).thenReturn(
            list);

        Optional<BestMatch> result = sut.bestMatch("72810", "Gomaringen", "DE");

        assertTrue(result.isPresent());
        assertThat(result.get().getHashKey(), is("D5BU1"));
        assertThat(result.get().getPostalcode(), is("72810"));
        assertThat(result.get().getCity(), is("GOMARINGEN"));
        assertThat(result.get().getCountryCode(), is("DE"));

        verify(addressServiceWrapper).getAddressesBasedOnStaticAddressResolution("72810", "Gomaringen", "DE");
    }


    @Test
    public void bestMatchIsNominatimAddress() {

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(AddressDetailKey.POSTAL_CODE.getKey(), "72810");
        addressDetails.put(AddressDetailKey.CITY.getKey(), "Gomaringen");
        addressDetails.put(AddressDetailKey.COUNTRY.getKey(), "DE");

        Address address = new DummyAddress();
        AddressList addressList = new AddressList("parent", singletonList(address));
        List<AddressList> list = singletonList(addressList);

        when(addressServiceWrapper.getAddressesBasedOnNominatimResolution(addressDetails)).thenReturn(list);

        Optional<BestMatch> result = sut.bestMatch("72810", "Gomaringen", "DE");

        assertTrue(result.isPresent());
        assertThat(result.get().getHashKey(), nullValue());
        assertThat(result.get().getPostalcode(), is("72810"));
        assertThat(result.get().getCity(), is("GOMARINGEN"));
        assertThat(result.get().getCountryCode(), is("DE"));

        verify(addressServiceWrapper).getAddressesBasedOnNominatimResolution(addressDetails);
    }


    @Test
    public void noBestMatchFound() {

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(AddressDetailKey.POSTAL_CODE.getKey(), "72810");
        addressDetails.put(AddressDetailKey.CITY.getKey(), "Gomaringen");
        addressDetails.put(AddressDetailKey.COUNTRY.getKey(), "DE");

        when(addressServiceWrapper.getAddressesBasedOnNominatimResolution(addressDetails)).thenReturn(emptyList());
        when(addressServiceWrapper.getAddressesBasedOnStaticAddressResolution("72810", "Gomaringen", "DE")).thenReturn(
            emptyList());

        Optional<BestMatch> bestMatch = sut.bestMatch("72180", "Gomaringen", "DE");

        assertFalse(bestMatch.isPresent());
    }

    private static class DummyAddress extends Address {

        @Override
        public String getCountryCode() {

            return "DE";
        }


        @Override
        public String getPostcode() {

            return "72810";
        }


        @Override
        public String getCity() {

            return "GOMARINGEN";
        }
    }
}
