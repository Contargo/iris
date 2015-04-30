package net.contargo.iris.connection.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


/**
 * Unit test for {@link net.contargo.iris.connection.dto.GeolocationDtoFactory}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class GeolocationDtoFactoryUnitTest {

    private GeoLocation geoLocation;
    private Seaport seaport;
    private Terminal terminal;
    private Address address;

    @Before
    public void setup() {

        geoLocation = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);
        terminal = new Terminal();
        seaport = new Seaport();
        address = new Address();
    }


    @Test
    public void constructWithGeolocation() {

        GeoLocationDto dto = GeolocationDtoFactory.createGeolocationDto(geoLocation);

        assertThat(dto, is(instanceOf(GeoLocationDto.class)));
        assertThat(dto, not(instanceOf(TerminalDto.class)));
        assertThat(dto, not(instanceOf(SeaportDto.class)));
    }


    @Test
    public void constructWithTerminal() {

        GeoLocationDto dto = GeolocationDtoFactory.createGeolocationDto(terminal);

        assertThat(dto, is(instanceOf(TerminalDto.class)));
        assertThat(dto, is(instanceOf(GeoLocationDto.class)));
        assertThat(dto, not(instanceOf(SeaportDto.class)));
    }


    @Test
    public void constructWithSeaport() {

        GeoLocationDto dto = GeolocationDtoFactory.createGeolocationDto(seaport);

        assertThat(dto, is(instanceOf(SeaportDto.class)));
        assertThat(dto, is(instanceOf(GeoLocationDto.class)));
        assertThat(dto, not(instanceOf(TerminalDto.class)));
    }


    @Test
    public void constructWithAddress() {

        GeoLocationDto dto = GeolocationDtoFactory.createGeolocationDto(address);

        assertThat(dto, is(instanceOf(AddressDto.class)));
        assertThat(dto, is(instanceOf(GeoLocationDto.class)));
        assertThat(dto, not(instanceOf(TerminalDto.class)));
    }
}
