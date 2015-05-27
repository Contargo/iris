package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.address.Address;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;


/**
 * Unit test for {@link NominatimJsonResponseParser} using constant file with address entries in Json format.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class NominatimJsonResponseParserUnitTest {

    private static final String DISPLAY_NAME = "displayName";
    private static final long OSM_ID = 90085697L;

    private NominatimJsonResponseParser sut;

    @Mock
    private RestTemplate nominatimRestClientMock;

    private Address responseAddress;

    @Before
    public void setup() {

        responseAddress = new Address(ONE, TEN);
        responseAddress.setDisplayName(DISPLAY_NAME);
        responseAddress.setOsmId(OSM_ID);

        sut = new NominatimJsonResponseParser(nominatimRestClientMock);
    }


    @Test
    public void getAddresses() {

        ResponseEntity<Address[]> response = new ResponseEntity<>(new Address[] { responseAddress }, OK);

        when(nominatimRestClientMock.exchange(anyString(), eq(GET), any(HttpEntity.class), eq(Address[].class)))
            .thenReturn(response);

        List<Address> addresses = sut.getAddresses("aUrl");
        assertThat(addresses.size(), is(1));

        Address firstAddress = addresses.get(0);
        assertThat(firstAddress.getDisplayName(), is(DISPLAY_NAME));
        assertThat(firstAddress.getOsmId(), is(OSM_ID));
        assertThat(firstAddress.getLatitude(), is(ONE.setScale(10)));
        assertThat(firstAddress.getLongitude(), is(TEN.setScale(10)));
    }


    @Test
    public void getAddressesRestClientException() {

        doThrow(RestClientException.class).when(nominatimRestClientMock)
            .exchange(anyString(), eq(GET), any(HttpEntity.class), eq(Address[].class));

        List<Address> addresses = sut.getAddresses("aUrl");
        assertThat(addresses, nullValue());
    }


    @Test
    public void getAddressesWithOSMId() {

        ResponseEntity<Address> response = new ResponseEntity<>(responseAddress, OK);

        when(nominatimRestClientMock.exchange(anyString(), eq(GET), any(HttpEntity.class), eq(Address.class)))
            .thenReturn(response);

        List<Address> addresses = sut.getAddressesFromOSMId("aUrl");
        assertThat(addresses.size(), is(1));

        Address firstAddress = addresses.get(0);
        assertThat(firstAddress.getDisplayName(), is(DISPLAY_NAME));
        assertThat(firstAddress.getOsmId(), is(OSM_ID));
        assertThat(firstAddress.getLatitude(), is(ONE.setScale(10)));
        assertThat(firstAddress.getLongitude(), is(TEN.setScale(10)));
    }


    @Test
    public void getAddressesWithOSMIdRestClientException() {

        doThrow(RestClientException.class).when(nominatimRestClientMock)
            .exchange(anyString(), eq(GET), any(HttpEntity.class), eq(Address.class));

        List<Address> addresses = sut.getAddressesFromOSMId("aUrl");
        assertThat(addresses, nullValue());
    }


    @Test
    public void getAddressForUrl() {

        ResponseEntity<Address> response = new ResponseEntity<>(responseAddress, OK);

        when(nominatimRestClientMock.exchange(anyString(), eq(GET), any(HttpEntity.class), eq(Address.class)))
            .thenReturn(response);

        Address address = sut.getAddress("aUrl");

        assertThat(address.getDisplayName(), is(DISPLAY_NAME));
        assertThat(address.getOsmId(), is(OSM_ID));
        assertThat(address.getLatitude(), is(ONE.setScale(10)));
        assertThat(address.getLongitude(), is(TEN.setScale(10)));
    }


    @Test
    public void getAddressForUrlRestClientException() {

        doThrow(RestClientException.class).when(nominatimRestClientMock)
            .exchange(anyString(), eq(GET), any(HttpEntity.class), eq(Address.class));

        Address address = sut.getAddress("aUrl");
        assertThat(address, nullValue());
    }
}
