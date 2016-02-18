package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


@RunWith(MockitoJUnitRunner.class)
public class PostcodeStaticAddressMappingProcessorUnitTest {

    private PostcodeStaticAddressMappingProcessor sut;

    @Mock
    private StaticAddressService serviceMock;

    @Before
    public void setUp() {

        sut = new PostcodeStaticAddressMappingProcessor(null, serviceMock);
    }


    @Test
    public void map() {

        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("postcode", "12345");
        addressMap.put("country_code", "ch");

        Address address = new Address();
        address.setAddress(addressMap);

        StaticAddress matchingStaticAddress = new StaticAddress();

        List<StaticAddress> addresses = singletonList(matchingStaticAddress);
        when(serviceMock.findByPostalcodeAndCountry("12345", "ch")).thenReturn(addresses);

        List<StaticAddress> result = sut.map(address);

        assertThat(result, is(addresses));
    }
}
