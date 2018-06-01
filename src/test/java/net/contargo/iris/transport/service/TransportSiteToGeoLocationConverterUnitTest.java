package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;
import net.contargo.iris.transport.api.SiteType;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.transport.api.SiteType.ADDRESS;
import static net.contargo.iris.transport.api.SiteType.SEAPORT;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author  Ben Antony antony@synyx.de
 */
public class TransportSiteToGeoLocationConverterUnitTest {

    private SeaportService seaportService = mock(SeaportService.class);
    private TerminalService terminalService = mock(TerminalService.class);
    private AddressServiceWrapper addressServiceWrapper = mock(AddressServiceWrapper.class);

    @Test
    public void convertToSeaport() {

        BigInteger uid = new BigInteger("42");
        Seaport seaport = new Seaport();
        when(seaportService.getByUniqueId(uid)).thenReturn(seaport);

        TransportResponseDto.TransportSite node = createTransportSite(SEAPORT);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(seaportService, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(seaport));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToSeaportFailsSeaportNotFound() {

        BigInteger uid = new BigInteger("42");
        when(seaportService.getByUniqueId(uid)).thenReturn(null);

        TransportResponseDto.TransportSite node = createTransportSite(SEAPORT);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(seaportService, null);
        sut.convert(node);
    }


    @Test
    public void convertToTerminal() {

        BigInteger uid = new BigInteger("42");
        Terminal terminal = new Terminal();
        when(terminalService.getByUniqueId(uid)).thenReturn(terminal);

        TransportResponseDto.TransportSite node = createTransportSite(TERMINAL);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(null, terminalService);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(terminal));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToTerminalFailsTerminalNotFound() {

        BigInteger uid = new BigInteger("42");
        when(terminalService.getByUniqueId(uid)).thenReturn(null);

        TransportResponseDto.TransportSite node = createTransportSite(TERMINAL);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(null, terminalService);
        sut.convert(node);
    }


    @Test
    public void convertToGeoLocation() {

        BigDecimal lat = new BigDecimal("49.0");
        BigDecimal lon = new BigDecimal("8.60");

        TransportDescriptionDto.TransportSite desc = new TransportDescriptionDto.TransportSite(ADDRESS, null, lon,
                lat);

        TransportResponseDto.TransportSite node = new TransportResponseDto.TransportSite(desc);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(null, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(new GeoLocation(lat, lon)));
    }


    private TransportResponseDto.TransportSite createTransportSite(SiteType type) {

        TransportDescriptionDto.TransportSite desc = new TransportDescriptionDto.TransportSite(type, "42", null, null);

        return new TransportResponseDto.TransportSite(desc);
    }
}
