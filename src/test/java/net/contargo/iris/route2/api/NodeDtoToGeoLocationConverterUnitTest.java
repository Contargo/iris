package net.contargo.iris.route2.api;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class NodeDtoToGeoLocationConverterUnitTest {

    private SeaportService seaportService = mock(SeaportService.class);
    private TerminalService terminalService = mock(TerminalService.class);
    private AddressServiceWrapper addressServiceWrapper = mock(AddressServiceWrapper.class);

    @Test
    public void convertToSeaport() {

        BigInteger uid = new BigInteger("42");
        Seaport seaport = new Seaport();
        when(seaportService.getByUniqueId(uid)).thenReturn(seaport);

        RoutePartNodeDto node = new RoutePartNodeDto(RoutePartNodeDtoType.SEAPORT, uid, null, null, null);

        NodeDtoToGeoLocationConverter sut = new NodeDtoToGeoLocationConverter(seaportService, null, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(seaport));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToSeaportFailsSeaportNotFound() {

        BigInteger uid = new BigInteger("42");
        when(seaportService.getByUniqueId(uid)).thenReturn(null);

        RoutePartNodeDto node = new RoutePartNodeDto(RoutePartNodeDtoType.SEAPORT, uid, null, null, null);

        NodeDtoToGeoLocationConverter sut = new NodeDtoToGeoLocationConverter(seaportService, null, null);
        sut.convert(node);
    }


    @Test
    public void convertToTerminal() {

        BigInteger uid = new BigInteger("42");
        Terminal terminal = new Terminal();
        when(terminalService.getByUniqueId(uid)).thenReturn(terminal);

        RoutePartNodeDto node = new RoutePartNodeDto(RoutePartNodeDtoType.TERMINAL, uid, null, null, null);

        NodeDtoToGeoLocationConverter sut = new NodeDtoToGeoLocationConverter(null, terminalService, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(terminal));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToTerminalFailsTerminalNotFound() {

        BigInteger uid = new BigInteger("42");
        when(terminalService.getByUniqueId(uid)).thenReturn(null);

        RoutePartNodeDto node = new RoutePartNodeDto(RoutePartNodeDtoType.TERMINAL, uid, null, null, null);

        NodeDtoToGeoLocationConverter sut = new NodeDtoToGeoLocationConverter(null, terminalService, null);
        sut.convert(node);
    }


    @Test
    public void convertToAddress() {

        String hashKey = "key";
        Address address = new Address();
        when(addressServiceWrapper.getByHashKey(hashKey)).thenReturn(address);

        RoutePartNodeDto node = new RoutePartNodeDto(RoutePartNodeDtoType.ADDRESS, null, hashKey, null, null);

        NodeDtoToGeoLocationConverter sut = new NodeDtoToGeoLocationConverter(null, null, addressServiceWrapper);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(address));
    }


    @Test(expected = IllegalArgumentException.class)
    public void convertToAddressFailsAddressNotFound() {

        String hashKey = "key";
        when(addressServiceWrapper.getByHashKey(hashKey)).thenReturn(null);

        RoutePartNodeDto node = new RoutePartNodeDto(RoutePartNodeDtoType.ADDRESS, null, hashKey, null, null);

        NodeDtoToGeoLocationConverter sut = new NodeDtoToGeoLocationConverter(null, null, addressServiceWrapper);
        sut.convert(node);
    }


    @Test
    public void convertToGeoLocation() {

        BigDecimal lat = new BigDecimal("49.0");
        BigDecimal lon = new BigDecimal("8.60");

        RoutePartNodeDto node = new RoutePartNodeDto(RoutePartNodeDtoType.GEOLOCATION, null, null, lat, lon);

        NodeDtoToGeoLocationConverter sut = new NodeDtoToGeoLocationConverter(null, null, null);
        GeoLocation geoLocation = sut.convert(node);

        assertThat(geoLocation, is(new GeoLocation(lat, lon)));
    }
}
