package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.StopType;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.api.TransportStop;

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
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.TERMINAL;
import static net.contargo.iris.transport.service.RouteStatus.OK;
import static net.contargo.iris.units.LengthUnit.KILOMETRE;
import static net.contargo.iris.units.MassUnit.KILOGRAM;
import static net.contargo.iris.units.TimeUnit.MINUTE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
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

        TransportStop terminal = new TransportStop(TERMINAL, "111", null, null);
        GeoLocation terminalGeoLocation = new GeoLocation(new BigDecimal("42.42"), new BigDecimal("8.42"));

        TransportStop address = new TransportStop(ADDRESS, null, new BigDecimal("49.23123"), new BigDecimal("8.1233"));
        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("49.23123"), new BigDecimal("8.1233"));

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(terminal, address, null, null, ROAD);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        when(conversionServiceMock.convert(matchesStopType(TERMINAL), any())).thenReturn(terminalGeoLocation);
        when(conversionServiceMock.convert(matchesStopType(ADDRESS), any())).thenReturn(addressGeoLocation);

        RouteResult routeResult = new RouteResult(40, 20, 300, asList("geometries1", "geometries2"), OK);
        when(routeServiceMock.route(terminalGeoLocation, addressGeoLocation, ROAD)).thenReturn(routeResult);

        when(routeDataRevisionServiceMock.getRouteDataRevision(new BigInteger("111"), addressGeoLocation)).thenReturn(
            empty());

        sut.with(segment);

        assertThat(segment.distance.value, is(40));
        assertThat(segment.distance.unit, is(KILOMETRE));
        assertThat(segment.tollDistance.value, is(20));
        assertThat(segment.tollDistance.unit, is(KILOMETRE));
        assertThat(segment.duration.value, is(300));
        assertThat(segment.duration.unit, is(MINUTE));
        assertThat(segment.co2.value, comparesEqualTo(new BigDecimal("29.2")));
        assertThat(segment.co2.unit, is(KILOGRAM));
        assertThat(segment.geometries.get(0), is("geometries1"));
        assertThat(segment.geometries.get(1), is("geometries2"));
    }


    @Test
    public void withRouteRevision() {

        TransportStop terminal = new TransportStop(TERMINAL, "111", null, null);
        GeoLocation terminalGeoLocation = new GeoLocation(new BigDecimal("42.42"), new BigDecimal("8.42"));

        TransportStop address = new TransportStop(ADDRESS, null, new BigDecimal("49.23123"), new BigDecimal("8.1233"));
        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("49.23123"), new BigDecimal("8.1233"));

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(address, terminal, null, null, ROAD);

        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);

        when(conversionServiceMock.convert(matchesStopType(TERMINAL), any())).thenReturn(terminalGeoLocation);
        when(conversionServiceMock.convert(matchesStopType(ADDRESS), any())).thenReturn(addressGeoLocation);

        RouteResult routeResult = new RouteResult(40, 20, 300, singletonList("geometries1"), OK);
        when(routeServiceMock.route(addressGeoLocation, terminalGeoLocation, ROAD)).thenReturn(routeResult);

        RouteDataRevision routeRevision = new RouteDataRevision();
        routeRevision.setTruckDistanceOneWayInKilometer(new BigDecimal("50"));
        routeRevision.setTollDistanceOneWayInKilometer(new BigDecimal("32"));

        when(routeDataRevisionServiceMock.getRouteDataRevision(new BigInteger("111"), addressGeoLocation)).thenReturn(
            Optional.of(routeRevision));

        sut.with(segment);

        assertThat(segment.distance.value, is(50));
        assertThat(segment.distance.unit, is(KILOMETRE));
        assertThat(segment.tollDistance.value, is(32));
        assertThat(segment.tollDistance.unit, is(KILOMETRE));
        assertThat(segment.duration.value, is(300));
        assertThat(segment.duration.unit, is(MINUTE));
        assertThat(segment.co2.value, comparesEqualTo(new BigDecimal("29.2")));
        assertThat(segment.co2.unit, is(KILOGRAM));
        assertThat(segment.geometries.get(0), is("geometries1"));
    }


    private TransportStop matchesStopType(StopType type) {

        return argThat(new ArgumentMatcher<TransportStop>() {

                    @Override
                    public boolean matches(Object argument) {

                        return argument != null && ((TransportStop) argument).type == type;
                    }
                });
    }
}
