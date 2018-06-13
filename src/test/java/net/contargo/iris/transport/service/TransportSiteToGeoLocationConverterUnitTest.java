package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;
import net.contargo.iris.transport.api.SiteType;
import net.contargo.iris.transport.api.TransportSite;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

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
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TransportSiteToGeoLocationConverterUnitTest {

    @Mock
    private SeaportService seaportService = mock(SeaportService.class);
    @Mock
    private TerminalService terminalService = mock(TerminalService.class);

    @Test
    public void convertToSeaport() {

        BigInteger uid = new BigInteger("42");
        Seaport seaport = new Seaport();
        when(seaportService.getByUniqueId(uid)).thenReturn(seaport);

        TransportSite node = createTransportSite(SEAPORT);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(seaportService, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(seaport));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToSeaportFailsSeaportNotFound() {

        BigInteger uid = new BigInteger("42");
        when(seaportService.getByUniqueId(uid)).thenReturn(null);

        TransportSite node = createTransportSite(SEAPORT);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(seaportService, null);
        sut.convert(node);
    }


    @Test
    public void convertToTerminal() {

        BigInteger uid = new BigInteger("42");
        Terminal terminal = new Terminal();
        when(terminalService.getByUniqueId(uid)).thenReturn(terminal);

        TransportSite node = createTransportSite(TERMINAL);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(null, terminalService);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(terminal));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToTerminalFailsTerminalNotFound() {

        BigInteger uid = new BigInteger("42");
        when(terminalService.getByUniqueId(uid)).thenReturn(null);

        TransportSite node = createTransportSite(TERMINAL);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(null, terminalService);
        sut.convert(node);
    }


    @Test
    public void convertToGeoLocation() {

        BigDecimal lat = new BigDecimal("49.0");
        BigDecimal lon = new BigDecimal("8.60");

        TransportSite desc = new TransportSite(ADDRESS, null, lat, lon);

        TransportSite node = new TransportSite(desc.type, desc.uuid, desc.lat, desc.lon);

        TransportSiteToGeoLocationConverter sut = new TransportSiteToGeoLocationConverter(null, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(new GeoLocation(lat, lon)));
    }


    private TransportSite createTransportSite(SiteType type) {

        TransportSite desc = new TransportSite(type, "42", null, null);

        return new TransportSite(desc.type, desc.uuid, desc.lat, desc.lon);
    }
}
