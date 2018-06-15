package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.SiteType;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.api.TransportSite;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;
import java.util.Optional;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.transport.api.ModeOfTransport.RAIL;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.SiteType.ADDRESS;
import static net.contargo.iris.transport.api.SiteType.SEAPORT;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;
import static net.contargo.iris.transport.service.RouteStatus.OK;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TransportDescriptionExtenderUnitTest {

    @InjectMocks
    private TransportDescriptionExtender sut;

    @Mock
    private RouteService routeServiceMock;
    @Mock
    private ConversionService conversionServiceMock;
    @Mock
    private RouteDataRevisionService routeDataRevisionServiceMock;

    @Test
    public void withRoutingInformation() {

        TransportSite terminal = new TransportSite(TERMINAL, "42", null, null);
        GeoLocation terminalGeoLocation = new GeoLocation(new BigDecimal("42.42"), new BigDecimal("8.42"));

        TransportSite seaport = new TransportSite(SEAPORT, "62", null, null);

        TransportSite address = new TransportSite(ADDRESS, null, new BigDecimal("42.34234"), new BigDecimal("8.0023"));
        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("42.34234"), new BigDecimal("8.0023"));

        TransportDescriptionDto.TransportDescriptionSegment seaportTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(seaport, terminal, FULL, true, RAIL);
        TransportDescriptionDto.TransportDescriptionSegment terminalAddress =
            new TransportDescriptionDto.TransportDescriptionSegment(terminal, address, FULL, true, ROAD);
        TransportDescriptionDto.TransportDescriptionSegment AddressTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(address, terminal, EMPTY, true, ROAD);

        List<TransportDescriptionDto.TransportDescriptionSegment> descriptions = asList(seaportTerminal,
                terminalAddress, AddressTerminal);

        TransportDescriptionDto description = new TransportDescriptionDto(descriptions);

        when(conversionServiceMock.convert(matchesSiteType(TERMINAL), any())).thenReturn(terminalGeoLocation);
        when(conversionServiceMock.convert(matchesSiteType(ADDRESS), any())).thenReturn(addressGeoLocation);

        RouteResult terminalAddressDistances = new RouteResult(40, 20, 300, emptyList(), OK);
        when(routeServiceMock.route(terminalGeoLocation, addressGeoLocation, ROAD)).thenReturn(
            terminalAddressDistances);

        RouteResult addressTerminalDistances = new RouteResult(45, 25, 400, emptyList(), OK);
        when(routeServiceMock.route(addressGeoLocation, terminalGeoLocation, ROAD)).thenReturn(
            addressTerminalDistances);

        when(routeDataRevisionServiceMock.getRouteDataRevision(new BigInteger("42"), addressGeoLocation)).thenReturn(
            Optional.empty());

        TransportResponseDto result = sut.withRoutingInformation(description);

        assertThat(result.transportChain.get(0).duration, nullValue());
        assertThat(result.transportChain.get(0).distance, nullValue());
        assertThat(result.transportChain.get(0).tollDistance, nullValue());

        assertThat(result.transportChain.get(1).duration, is(300));
        assertThat(result.transportChain.get(1).distance, is(40));
        assertThat(result.transportChain.get(1).tollDistance, is(20));

        assertThat(result.transportChain.get(2).duration, is(400));
        assertThat(result.transportChain.get(2).distance, is(45));
        assertThat(result.transportChain.get(2).tollDistance, is(25));
    }


    @Test
    public void withRoutingInformationWithRouteDataRevision() {

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setTruckDistanceOneWayInKilometer(new BigDecimal("50"));
        routeDataRevision.setTollDistanceOneWayInKilometer(new BigDecimal("32"));

        when(routeDataRevisionServiceMock.getRouteDataRevision(new BigInteger("42"),
                    new GeoLocation(new BigDecimal("42.34234"), new BigDecimal("8.0023")))).thenReturn(Optional.of(
                routeDataRevision));

        TransportSite terminal = new TransportSite(TERMINAL, "42", null, null);
        GeoLocation terminalGeoLocation = new GeoLocation(new BigDecimal("42.42"), new BigDecimal("8.42"));

        TransportSite seaport = new TransportSite(SEAPORT, "62", null, null);

        TransportSite address = new TransportSite(ADDRESS, null, new BigDecimal("42.34234"), new BigDecimal("8.0023"));
        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("42.34234"), new BigDecimal("8.0023"));

        TransportDescriptionDto.TransportDescriptionSegment seaportTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(seaport, terminal, FULL, true, RAIL);
        TransportDescriptionDto.TransportDescriptionSegment terminalAddress =
            new TransportDescriptionDto.TransportDescriptionSegment(terminal, address, FULL, true, ROAD);
        TransportDescriptionDto.TransportDescriptionSegment AddressTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(address, terminal, EMPTY, true, ROAD);

        List<TransportDescriptionDto.TransportDescriptionSegment> descriptions = asList(seaportTerminal,
                terminalAddress, AddressTerminal);

        TransportDescriptionDto description = new TransportDescriptionDto(descriptions);

        when(conversionServiceMock.convert(matchesSiteType(TERMINAL), any())).thenReturn(terminalGeoLocation);
        when(conversionServiceMock.convert(matchesSiteType(ADDRESS), any())).thenReturn(addressGeoLocation);

        RouteResult terminalAddressDistances = new RouteResult(40, 20, 300, emptyList(), OK);
        when(routeServiceMock.route(terminalGeoLocation, addressGeoLocation, ROAD)).thenReturn(
            terminalAddressDistances);

        RouteResult addressTerminalDistances = new RouteResult(45, 25, 400, emptyList(), OK);
        when(routeServiceMock.route(addressGeoLocation, terminalGeoLocation, ROAD)).thenReturn(
            addressTerminalDistances);

        TransportResponseDto result = sut.withRoutingInformation(description);

        assertThat(result.transportChain.get(0).duration, nullValue());
        assertThat(result.transportChain.get(0).distance, nullValue());
        assertThat(result.transportChain.get(0).tollDistance, nullValue());

        assertThat(result.transportChain.get(1).duration, is(300));
        assertThat(result.transportChain.get(1).distance, is(50));
        assertThat(result.transportChain.get(1).tollDistance, is(32));

        assertThat(result.transportChain.get(2).duration, is(400));
        assertThat(result.transportChain.get(2).distance, is(50));
        assertThat(result.transportChain.get(2).tollDistance, is(32));
    }


    private TransportSite matchesSiteType(SiteType type) {

        return argThat(new ArgumentMatcher<TransportSite>() {

                    @Override
                    public boolean matches(Object argument) {

                        return argument != null && ((TransportSite) argument).type == type;
                    }
                });
    }
}
