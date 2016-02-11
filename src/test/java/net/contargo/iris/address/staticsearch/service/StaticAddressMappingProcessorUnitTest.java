package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


@RunWith(MockitoJUnitRunner.class)
public class StaticAddressMappingProcessorUnitTest {

    private SutMappingProcessor sut;

    @Mock
    private StaticAddressMappingProcessor nextMock;

    @Before
    public void setUp() {
    }


    @Test
    public void processWithEmptyResult() {

        Address address = new Address();
        address.setShortName("foo");

        sut = new SutMappingProcessor(null);

        List<StaticAddress> result = sut.process(address);

        assertThat(result, empty());
    }


    @Test
    public void processWithNext() {

        Address address = new Address();
        address.setShortName("foo");

        sut = new SutMappingProcessor(nextMock);

        List<StaticAddress> staticAddresses = singletonList(new StaticAddress());
        when(nextMock.process(address)).thenReturn(staticAddresses);

        List<StaticAddress> result = sut.process(address);

        assertThat(result, is(staticAddresses));
    }


    @Test
    public void processWithResultWithoutNext() {

        Address address = new Address();
        address.setShortName("bar");

        sut = new SutMappingProcessor(null);

        List<StaticAddress> result = sut.process(address);

        assertThat(result, is(sut.staticAddresses));
    }

    class SutMappingProcessor extends StaticAddressMappingProcessor {

        private List<StaticAddress> staticAddresses = singletonList(new StaticAddress());

        public SutMappingProcessor(StaticAddressMappingProcessor next) {

            super(next);
        }

        @Override
        List<StaticAddress> map(Address address) {

            if (address.getShortName().equals("foo")) {
                return emptyList();
            } else {
                return staticAddresses;
            }
        }
    }
}
