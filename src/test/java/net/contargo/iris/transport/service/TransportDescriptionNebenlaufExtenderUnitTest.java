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

import java.util.Optional;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.SiteType.ADDRESS;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;
import static net.contargo.iris.transport.service.RouteStatus.OK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TransportDescriptionNebenlaufExtenderUnitTest {

    @InjectMocks
    private TransportDescriptionNebenlaufExtender sut;

    @Mock
    private RouteService routeServiceMock;
    @Mock
    private ConversionService conversionServiceMock;

    @Mock
    private RouteDataRevisionService routeDataRevisionServiceMock;

    @Test
    public void withoutRouteRevision() {

        TransportSite terminal = new TransportSite(TERMINAL, "111", null, null);
        GeoLocation terminalGeoLocation = new GeoLocation(new BigDecimal("42.42"), new BigDecimal("8.42"));

        TransportSite address = new TransportSite(ADDRESS, null, new BigDecimal("49.23123"), new BigDecimal("8.1233"));
        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("49.23123"), new BigDecimal("8.1233"));

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(terminal, address, null, null, ROAD);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        when(conversionServiceMock.convert(matchesSiteType(TERMINAL), any())).thenReturn(terminalGeoLocation);
        when(conversionServiceMock.convert(matchesSiteType(ADDRESS), any())).thenReturn(addressGeoLocation);

        RouteResult routeResult = new RouteResult(40, 20, 300, asList("geometries1", "geometries2"), OK);
        when(routeServiceMock.route(terminalGeoLocation, addressGeoLocation, ROAD)).thenReturn(routeResult);

        when(routeDataRevisionServiceMock.getRouteDataRevision(new BigInteger("111"), addressGeoLocation)).thenReturn(
            empty());

        sut.with(segment);

        assertThat(segment.distance, is(40));
        assertThat(segment.tollDistance, is(20));
        assertThat(segment.duration, is(300));
        assertThat(segment.geometries.get(0), is("geometries1"));
        assertThat(segment.geometries.get(1), is("geometries2"));
    }


    @Test
    public void withRouteRevision() {

        TransportSite terminal = new TransportSite(TERMINAL, "111", null, null);
        GeoLocation terminalGeoLocation = new GeoLocation(new BigDecimal("42.42"), new BigDecimal("8.42"));

        TransportSite address = new TransportSite(ADDRESS, null, new BigDecimal("49.23123"), new BigDecimal("8.1233"));
        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("49.23123"), new BigDecimal("8.1233"));

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(address, terminal, null, null, ROAD);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        when(conversionServiceMock.convert(matchesSiteType(TERMINAL), any())).thenReturn(terminalGeoLocation);
        when(conversionServiceMock.convert(matchesSiteType(ADDRESS), any())).thenReturn(addressGeoLocation);

        RouteResult routeResult = new RouteResult(40, 20, 300, singletonList("geometries1"), OK);
        when(routeServiceMock.route(addressGeoLocation, terminalGeoLocation, ROAD)).thenReturn(routeResult);

        RouteDataRevision routeRevision = new RouteDataRevision();
        routeRevision.setTruckDistanceOneWayInKilometer(new BigDecimal("50"));
        routeRevision.setTollDistanceOneWayInKilometer(new BigDecimal("32"));

        when(routeDataRevisionServiceMock.getRouteDataRevision(new BigInteger("111"), addressGeoLocation)).thenReturn(
            Optional.of(routeRevision));

        sut.with(segment);

        assertThat(segment.distance, is(50));
        assertThat(segment.tollDistance, is(32));
        assertThat(segment.duration, is(300));
        assertThat(segment.geometries.get(0), is("geometries1"));
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
