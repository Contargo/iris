package net.contargo.iris.connection.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
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
 * Unit test for {@link net.contargo.iris.connection.dto.RoutePartDto}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RoutePartDtoUnitTest {

    private GeoLocation geoLocation;
    private Seaport seaport;
    private Terminal terminal;

    @Before
    public void setup() {

        geoLocation = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);
        terminal = new Terminal();
        seaport = new Seaport();
    }


    @Test
    public void constructWithGeolocation() {

        RoutePart part = new RoutePart();
        part.setOrigin(geoLocation);
        part.setDestination(geoLocation);
        part.setRouteType(RouteType.BARGE);

        RoutePartDto routePartDto = new RoutePartDto(part);

        assertThat(routePartDto.getOrigin(), is(instanceOf(GeoLocationDto.class)));
        assertThat(routePartDto.getOrigin(), not(instanceOf(TerminalDto.class)));
        assertThat(routePartDto.getOrigin(), not(instanceOf(SeaportDto.class)));

        assertThat(routePartDto.getDestination(), is(instanceOf(GeoLocationDto.class)));
        assertThat(routePartDto.getDestination(), not(instanceOf(TerminalDto.class)));
        assertThat(routePartDto.getDestination(), not(instanceOf(SeaportDto.class)));
    }


    @Test
    public void constructWithTerminal() {

        RoutePart part = new RoutePart();
        part.setOrigin(terminal);
        part.setDestination(terminal);

        RoutePartDto routePartDto = new RoutePartDto(part);

        assertThat(routePartDto.getOrigin(), is(instanceOf(TerminalDto.class)));
        assertThat(routePartDto.getOrigin(), is(instanceOf(GeoLocationDto.class)));
        assertThat(routePartDto.getOrigin(), not(instanceOf(SeaportDto.class)));

        assertThat(routePartDto.getDestination(), is(instanceOf(TerminalDto.class)));
        assertThat(routePartDto.getDestination(), is(instanceOf(GeoLocationDto.class)));
        assertThat(routePartDto.getDestination(), not(instanceOf(SeaportDto.class)));
    }


    @Test
    public void constructWithSeaport() {

        RoutePart part = new RoutePart();
        part.setOrigin(seaport);
        part.setDestination(seaport);

        RoutePartDto routePartDto = new RoutePartDto(part);

        assertThat(routePartDto.getOrigin(), is(instanceOf(SeaportDto.class)));
        assertThat(routePartDto.getOrigin(), is(instanceOf(GeoLocationDto.class)));
        assertThat(routePartDto.getOrigin(), not(instanceOf(TerminalDto.class)));

        assertThat(routePartDto.getDestination(), is(instanceOf(SeaportDto.class)));
        assertThat(routePartDto.getDestination(), is(instanceOf(GeoLocationDto.class)));
        assertThat(routePartDto.getDestination(), not(instanceOf(TerminalDto.class)));
    }
}
