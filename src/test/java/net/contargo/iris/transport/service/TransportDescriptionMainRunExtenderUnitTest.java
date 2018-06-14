package net.contargo.iris.transport.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.api.TransportSite;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.transport.api.ModeOfTransport.RAIL;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.ModeOfTransport.WATER;
import static net.contargo.iris.transport.api.SiteType.ADDRESS;
import static net.contargo.iris.transport.api.SiteType.SEAPORT;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;


/**
 * @author  Ben Antony - antony@synyx.de
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

        int result = TransportDescriptionMainRunExtender.calculateDuration(new BigDecimal(distance),
                new BigDecimal("1"));
        assertThat(result, is(expected));
    }


    @Test
    public void withWaterDownstreamSegment() {

        TransportSite from = new TransportSite(TERMINAL, "123456789", null, null);
        TransportSite to = new TransportSite(SEAPORT, "111", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, null, null, WATER);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger("123456789"));

        Seaport seaport = new Seaport();
        seaport.setUniqueId(new BigInteger("111"));

        MainRunConnection connection = new MainRunConnection();
        connection.setBargeDieselDistance(new BigDecimal("400"));

        when(mainRunConnectionServiceMock.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal, seaport,
                    RouteType.BARGE, null)).thenReturn(connection);

        sut.with(segment);

        assertThat(segment.distance, is(400));
        assertThat(segment.duration, is(1334));
    }


    @Test
    public void withWaterUpstreamSegment() {

        TransportSite from = new TransportSite(SEAPORT, "112", null, null);
        TransportSite to = new TransportSite(TERMINAL, "113456789", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, null, null, WATER);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger("113456789"));

        Seaport seaport = new Seaport();
        seaport.setUniqueId(new BigInteger("112"));

        MainRunConnection connection = new MainRunConnection();
        connection.setBargeDieselDistance(new BigDecimal("500"));

        when(mainRunConnectionServiceMock.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal, seaport,
                    RouteType.BARGE, null)).thenReturn(connection);

        sut.with(segment);

        assertThat(segment.distance, is(500));
        assertThat(segment.duration, is(3000));
    }


    @Test
    public void withRailDieselSegment() {

        TransportSite from = new TransportSite(SEAPORT, "113", null, null);
        TransportSite to = new TransportSite(TERMINAL, "121456789", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, null, null, RAIL);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger("121456789"));

        Seaport seaport = new Seaport();
        seaport.setUniqueId(new BigInteger("113"));

        MainRunConnection connection = new MainRunConnection();
        connection.setRailDieselDistance(new BigDecimal("600"));

        when(mainRunConnectionServiceMock.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal, seaport,
                    RouteType.RAIL, null)).thenReturn(connection);

        sut.with(segment);

        assertThat(segment.distance, is(600));
        assertThat(segment.duration, is(800));
    }


    @Test
    public void withRailElectricSegment() {

        TransportSite from = new TransportSite(SEAPORT, "123156789", null, null);
        TransportSite to = new TransportSite(TERMINAL, "114", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, null, null, RAIL);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger("123156789"));

        Seaport seaport = new Seaport();
        seaport.setUniqueId(new BigInteger("114"));

        MainRunConnection connection = new MainRunConnection();
        connection.setRailElectricDistance(new BigDecimal("700"));

        when(mainRunConnectionServiceMock.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal, seaport,
                    RouteType.RAIL, null)).thenReturn(connection);

        sut.with(segment);

        assertThat(segment.distance, is(700));
        assertThat(segment.duration, is(934));
    }


    @Test(expected = IllegalArgumentException.class)
    public void withNonMainRunSegment() {

        TransportSite from = new TransportSite(ADDRESS, null, new BigDecimal("49.12323"), new BigDecimal("8.32432"));
        TransportSite to = new TransportSite(TERMINAL, "114", null, null);

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, null, null, ROAD);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        sut.with(segment);
    }
}
