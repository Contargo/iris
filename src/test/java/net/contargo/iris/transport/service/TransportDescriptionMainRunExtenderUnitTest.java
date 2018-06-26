package net.contargo.iris.transport.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.api.TransportStop;
import net.contargo.iris.units.Duration;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.SEAPORT;
import static net.contargo.iris.transport.api.StopType.TERMINAL;
import static net.contargo.iris.units.LengthUnit.KILOMETRE;
import static net.contargo.iris.units.MassUnit.KILOGRAM;
import static net.contargo.iris.units.TimeUnit.MINUTE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TransportDescriptionMainRunExtenderUnitTest {

    @InjectMocks
    private TransportDescriptionMainRunExtender sut;

    @Mock
    private MainRunConnectionService mainRunConnectionServiceMock;

    @Test
    public void calculateDuration() {

        assertDuration("12.0", 720);
        assertDuration("12.1", 726);
        assertDuration("12.2", 732);
    }


    private void assertDuration(String distance, int expected) {

        Duration result = TransportDescriptionMainRunExtender.calculateDuration(new BigDecimal(distance),
                new BigDecimal("1"));
        assertThat(result.value, is(expected));
        assertThat(result.unit, is(MINUTE));
    }


    @Test
    public void withWaterDownstreamSegment() {

        TransportStop from = new TransportStop(TERMINAL, "123456789", null, null);
        TransportStop to = new TransportStop(SEAPORT, "111", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, FULL, null, ModeOfTransport.WATER);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger("123456789"));

        MainRunConnection connection = new MainRunConnection();
        connection.setBargeDieselDistance(new BigDecimal("400"));
        connection.setTerminal(terminal);

        when(mainRunConnectionServiceMock.getConnectionByTerminalUidAndSeaportUidAndType(new BigInteger("123456789"),
                    new BigInteger("111"), RouteType.BARGE)).thenReturn(connection);

        sut.with(segment);

        assertThat(segment.distance.value, is(400));
        assertThat(segment.distance.unit, is(KILOMETRE));
        assertThat(segment.duration.value, is(1334));
        assertThat(segment.duration.unit, is(MINUTE));
        assertThat(segment.co2.value, comparesEqualTo(new BigDecimal("68.00")));
        assertThat(segment.co2.unit, is(KILOGRAM));
    }


    @Test
    public void withWaterUpstreamSegment() {

        TransportStop from = new TransportStop(SEAPORT, "112", null, null);
        TransportStop to = new TransportStop(TERMINAL, "113456789", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, EMPTY, null, ModeOfTransport.WATER);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger("113456789"));

        MainRunConnection connection = new MainRunConnection();
        connection.setBargeDieselDistance(new BigDecimal("500"));
        connection.setTerminal(terminal);

        when(mainRunConnectionServiceMock.getConnectionByTerminalUidAndSeaportUidAndType(new BigInteger("113456789"),
                    new BigInteger("112"), RouteType.BARGE)).thenReturn(connection);

        sut.with(segment);

        assertThat(segment.distance.value, is(500));
        assertThat(segment.distance.unit, is(KILOMETRE));
        assertThat(segment.duration.value, is(3000));
        assertThat(segment.duration.unit, is(MINUTE));
        assertThat(segment.co2.value, comparesEqualTo(new BigDecimal("135.00")));
        assertThat(segment.co2.unit, is(KILOGRAM));
    }


    @Test
    public void withRailSegment() {

        TransportStop from = new TransportStop(SEAPORT, "113", null, null);
        TransportStop to = new TransportStop(TERMINAL, "121456789", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, FULL, null, ModeOfTransport.RAIL);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        MainRunConnection connection = new MainRunConnection();
        connection.setRailDieselDistance(new BigDecimal("400"));
        connection.setRailElectricDistance(new BigDecimal("200"));

        when(mainRunConnectionServiceMock.getConnectionByTerminalUidAndSeaportUidAndType(new BigInteger("121456789"),
                    new BigInteger("113"), RouteType.RAIL)).thenReturn(connection);

        sut.with(segment);

        assertThat(segment.distance.value, is(600));
        assertThat(segment.distance.unit, is(KILOMETRE));
        assertThat(segment.duration.value, is(800));
        assertThat(segment.duration.unit, is(MINUTE));
        assertThat(segment.co2.value, comparesEqualTo(new BigDecimal("268.00")));
        assertThat(segment.co2.unit, is(KILOGRAM));
    }


    @Test(expected = IllegalArgumentException.class)
    public void withNonMainRunSegment() {

        TransportStop from = new TransportStop(ADDRESS, null, new BigDecimal("49.12323"), new BigDecimal("8.32432"));
        TransportStop to = new TransportStop(TERMINAL, "114", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, null, null, ModeOfTransport.ROAD);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        sut.with(segment);
    }
}
