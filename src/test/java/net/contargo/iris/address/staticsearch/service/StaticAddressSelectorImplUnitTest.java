package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.gis.service.GisService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


@RunWith(MockitoJUnitRunner.class)
public class StaticAddressSelectorImplUnitTest {

    private StaticAddressSelectorImpl sut;

    @Mock
    private GisService gisService;

    @Before
    public void setUp() {

        sut = new StaticAddressSelectorImpl(gisService);
    }


    @Test
    public void select() {

        StaticAddress selected = new StaticAddress();
        selected.setLatitude(BigDecimal.ONE);
        selected.setLongitude(BigDecimal.ONE);

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setLatitude(BigDecimal.TEN);
        staticAddress.setLongitude(BigDecimal.TEN);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setLatitude(BigDecimal.ZERO);
        staticAddress2.setLongitude(BigDecimal.ZERO);

        List<StaticAddress> staticAddresses = asList(staticAddress, selected, staticAddress2);

        Address address = new Address();
        address.setLatitude(BigDecimal.ONE);
        address.setLongitude(BigDecimal.ONE);

        when(gisService.calcAirLineDistInMeters(new GeoLocation(BigDecimal.ONE, BigDecimal.ONE),
                    new GeoLocation(BigDecimal.ONE, BigDecimal.ONE))).thenReturn(BigDecimal.ONE);

        when(gisService.calcAirLineDistInMeters(new GeoLocation(BigDecimal.ONE, BigDecimal.ONE),
                    new GeoLocation(BigDecimal.TEN, BigDecimal.TEN))).thenReturn(BigDecimal.TEN);

        when(gisService.calcAirLineDistInMeters(new GeoLocation(BigDecimal.ONE, BigDecimal.ONE),
                    new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO))).thenReturn(new BigDecimal("2"));

        StaticAddress result = sut.select(staticAddresses, address);
        assertThat(result, is(selected));
    }
}
