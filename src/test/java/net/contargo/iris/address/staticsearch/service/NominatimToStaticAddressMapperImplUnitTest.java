package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


@RunWith(MockitoJUnitRunner.class)
public class NominatimToStaticAddressMapperImplUnitTest {

    private NominatimToStaticAddressMapperImpl sut;

    @Mock
    private StaticAddressMappingProcessor mappingProcessor;
    @Mock
    private StaticAddressSelector addressSelector;

    @Before
    public void setUp() {

        sut = new NominatimToStaticAddressMapperImpl(mappingProcessor, addressSelector);
    }


    @Test(expected = NominatimToStaticAddressMapperException.class)
    public void mapNotSuccessful() {

        Address address = new Address();

        when(mappingProcessor.process(address)).thenReturn(emptyList());

        sut.map(address);
    }


    @Test
    public void mapSuccessfulOneResult() {

        Address address = new Address();

        StaticAddress staticAddress = new StaticAddress();
        when(mappingProcessor.process(address)).thenReturn(singletonList(staticAddress));

        StaticAddress result = sut.map(address);
        assertThat(result, is(staticAddress));
    }


    @Test
    public void mapSuccessfulMultipleResults() {

        Address address = new Address();

        StaticAddress staticAddress = new StaticAddress();
        List<StaticAddress> staticAddresses = asList(staticAddress, staticAddress);

        when(mappingProcessor.process(address)).thenReturn(staticAddresses);
        when(addressSelector.select(staticAddresses, address)).thenReturn(staticAddress);

        StaticAddress result = sut.map(address);
        assertThat(result, is(staticAddress));
    }
}
