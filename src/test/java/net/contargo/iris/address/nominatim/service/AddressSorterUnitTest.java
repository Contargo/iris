package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.gis.service.GisService;
import net.contargo.iris.gis.service.GisServiceImpl;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class AddressSorterUnitTest {

    private GisService gisServiceMock;

    private AddressSorter sut;

    @Before
    public void setUp() {

        gisServiceMock = mock(GisServiceImpl.class);
        sut = new AddressSorter(gisServiceMock);
    }


    @Test
    public void testCompare() {

        Address addressFar = new Address();
        addressFar.setLatitude(BigDecimal.TEN);
        addressFar.setLongitude(BigDecimal.TEN);

        Address addressNear = new Address();
        addressNear.setLatitude(BigDecimal.ONE);
        addressNear.setLongitude(BigDecimal.ONE);

        when(gisServiceMock.calcAirLineDistInMeters(addressFar, GisService.CENTER_OF_THE_EUROPEAN_UNION)).thenReturn(
            BigDecimal.TEN);
        when(gisServiceMock.calcAirLineDistInMeters(addressNear, GisService.CENTER_OF_THE_EUROPEAN_UNION)).thenReturn(
            BigDecimal.ONE);

        assertThat(sut.compare(addressFar, addressNear), is(1));
    }
}
