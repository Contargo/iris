package net.contargo.iris.address.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.nominatim.service.AddressDetailKey;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;

import org.junit.Test;

import java.math.BigInteger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class BestMatchServiceImplTest {

    private StaticAddressService staticAddressService = mock(StaticAddressService.class);
    private AddressService addressService = mock(AddressService.class);
    private BestMatchServiceImpl sut = new BestMatchServiceImpl(staticAddressService, addressService);

    @Test
    public void bestMatchIsStaticAddress() {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setPostalcode("72810");
        staticAddress.setCity("GOMARINGEN");
        staticAddress.setCountry("DE");
        staticAddress.setUniqueId(new BigInteger("1301000000063529"));

        when(staticAddressService.getAddressesByDetails("72810", "Gomaringen", "DE")).thenReturn(singletonList(
                staticAddress));

        Optional<BestMatch> result = sut.bestMatch("72810", "Gomaringen", "DE");
        BestMatch bestMatch = result.get();

        assertThat(bestMatch.getHashKey(), is("D5BU1"));
        assertThat(bestMatch.getPostalcode(), is("72810"));
        assertThat(bestMatch.getCity(), is("GOMARINGEN"));
        assertThat(bestMatch.getCountryCode(), is("DE"));

        verify(staticAddressService).getAddressesByDetails("72810", "Gomaringen", "DE");
        verifyZeroInteractions(addressService);
    }


    @Test
    public void bestMatchIsNominatimAddress() {

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(AddressDetailKey.POSTAL_CODE.getKey(), "72810");
        addressDetails.put(AddressDetailKey.CITY.getKey(), "Gomaringen");
        addressDetails.put(AddressDetailKey.COUNTRY.getKey(), "DE");

        Address address = new DummyAddress();

        when(staticAddressService.getAddressesByDetails("72810", "Gomaringen", "DE")).thenReturn(emptyList());
        when(addressService.getAddressesByDetails(addressDetails)).thenReturn(singletonList(address));

        Optional<BestMatch> result = sut.bestMatch("72810", "Gomaringen", "DE");
        BestMatch bestMatch = result.get();

        assertThat(bestMatch.getHashKey(), nullValue());
        assertThat(bestMatch.getPostalcode(), is("72810"));
        assertThat(bestMatch.getCity(), is("GOMARINGEN"));
        assertThat(bestMatch.getCountryCode(), is("DE"));

        verify(staticAddressService).getAddressesByDetails("72810", "Gomaringen", "DE");
        verify(addressService).getAddressesByDetails(addressDetails);
    }


    @Test
    public void noBestMatchFound() {

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(AddressDetailKey.POSTAL_CODE.getKey(), "72810");
        addressDetails.put(AddressDetailKey.CITY.getKey(), "Gomaringen");
        addressDetails.put(AddressDetailKey.COUNTRY.getKey(), "DE");

        when(staticAddressService.getAddressesByDetails("72810", "Gomaringen", "DE")).thenReturn(emptyList());
        when(addressService.getAddressesByDetails(addressDetails)).thenReturn(emptyList());

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
