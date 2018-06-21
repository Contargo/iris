package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;
import net.contargo.iris.transport.api.StopType;
import net.contargo.iris.transport.api.TransportStop;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.SEAPORT;
import static net.contargo.iris.transport.api.StopType.TERMINAL;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author  Ben Antony antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TransportStopToGeoLocationConverterUnitTest {

    @Mock
    private SeaportService seaportService = mock(SeaportService.class);
    @Mock
    private TerminalService terminalService = mock(TerminalService.class);

    @Test
    public void convertToSeaport() {

        BigInteger uid = new BigInteger("42");
        Seaport seaport = new Seaport();
        when(seaportService.getByUniqueId(uid)).thenReturn(seaport);

        TransportStop node = createTransportStop(SEAPORT);

        TransportStopToGeoLocationConverter sut = new TransportStopToGeoLocationConverter(seaportService, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(seaport));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToSeaportFailsSeaportNotFound() {

        BigInteger uid = new BigInteger("42");
        when(seaportService.getByUniqueId(uid)).thenReturn(null);

        TransportStop node = createTransportStop(SEAPORT);

        TransportStopToGeoLocationConverter sut = new TransportStopToGeoLocationConverter(seaportService, null);
        sut.convert(node);
    }


    @Test
    public void convertToTerminal() {

        BigInteger uid = new BigInteger("42");
        Terminal terminal = new Terminal();
        when(terminalService.getByUniqueId(uid)).thenReturn(terminal);

        TransportStop node = createTransportStop(TERMINAL);

        TransportStopToGeoLocationConverter sut = new TransportStopToGeoLocationConverter(null, terminalService);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(terminal));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToTerminalFailsTerminalNotFound() {

        BigInteger uid = new BigInteger("42");
        when(terminalService.getByUniqueId(uid)).thenReturn(null);

        TransportStop node = createTransportStop(TERMINAL);

        TransportStopToGeoLocationConverter sut = new TransportStopToGeoLocationConverter(null, terminalService);
        sut.convert(node);
    }


    @Test
    public void convertToGeoLocation() {

        BigDecimal lat = new BigDecimal("49.0");
        BigDecimal lon = new BigDecimal("8.60");

        TransportStop desc = new TransportStop(ADDRESS, null, lat, lon);

        TransportStop node = new TransportStop(desc.type, desc.uuid, desc.lat, desc.lon);

        TransportStopToGeoLocationConverter sut = new TransportStopToGeoLocationConverter(null, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(new GeoLocation(lat, lon)));
    }


    private TransportStop createTransportStop(StopType type) {

        TransportStop desc = new TransportStop(type, "42", null, null);

        return new TransportStop(desc.type, desc.uuid, desc.lat, desc.lon);
    }
}
