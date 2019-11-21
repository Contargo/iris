package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.fail;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class ClosestStaticAddressServiceImplTest {

    @InjectMocks
    private ClosestStaticAddressServiceImpl sut;

    @Mock
    private NominatimToStaticAddressMapper nominatimToStaticAddressMapperMock;
    @Mock
    private StaticAddressService staticAddressServiceMock;
    @Mock
    private StaticAddressSelector staticAddressSelectorMock;

    @Test
    public void getNoAddressFoundInLimitRadius() {

        doThrow(new NominatimToStaticAddressMapperException(null)).when(nominatimToStaticAddressMapperMock)
            .map(any(Address.class));

        try {
            sut.get("78654", "city", "CH", BigDecimal.valueOf(47.65468231), BigDecimal.valueOf(8.5464351));
            fail();
        } catch (NominatimToStaticAddressMapperException e) {
            // nothing to do here
        }

        verifyZeroInteractions(staticAddressSelectorMock);
        verify(staticAddressServiceMock, times(5)).getAddressesInBoundingBox(any(GeoLocation.class), anyDouble());
    }


    @Test
    public void getAddressFoundByRadius() {

        doThrow(new NominatimToStaticAddressMapperException(null)).when(nominatimToStaticAddressMapperMock)
            .map(any(Address.class));

        doReturn(emptyList()).when(staticAddressServiceMock)
            .getAddressesInBoundingBox(any(GeoLocation.class), eq(5.0));

        StaticAddress address1 = new StaticAddress();
        StaticAddress address2 = new StaticAddress();

        doReturn(asList(address1, address2)).when(staticAddressServiceMock)
            .getAddressesInBoundingBox(any(GeoLocation.class), eq(10.0));

        doReturn(address2).when(staticAddressSelectorMock).select(eq(asList(address1, address2)), any(Address.class));

        StaticAddress result = sut.get("78654", "city", "CH", BigDecimal.valueOf(47.65468231),
                BigDecimal.valueOf(8.5464351));

        assertThat(result, is(address2));

        verify(staticAddressServiceMock, times(2)).getAddressesInBoundingBox(any(GeoLocation.class), anyDouble());
    }


    @Test
    public void getByDetails() {

        StaticAddress address = new StaticAddress();
        doReturn(address).when(nominatimToStaticAddressMapperMock).map(any(Address.class));

        StaticAddress result = sut.get("78654", "city", "CH", BigDecimal.valueOf(47.65468231),
                BigDecimal.valueOf(8.5464351));

        assertThat(result, is(address));

        verifyZeroInteractions(staticAddressServiceMock);
    }
}
