package net.contargo.iris.transport;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.StopType;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.api.TransportStop;
import net.contargo.iris.transport.route.RouteResult;
import net.contargo.iris.transport.route.RouteService;

import org.junit.Before;
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

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.TERMINAL;
import static net.contargo.iris.transport.route.RouteStatus.OK;
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
import static java.util.Optional.empty;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Bjoern Martin - martin@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TransportDescriptionRoadExtenderUnitTest {

    private static final BigInteger TERMINAL_ID = new BigInteger("111");

    private TransportResponseDto.TransportResponseSegment segment;
    private GeoLocation addressGeoLocation;

    @InjectMocks
    private TransportDescriptionRoadExtender sut;

    @Mock
    private RouteService routeServiceMock;

    @Mock
    private ConversionService conversionServiceMock;

    @Mock
    private RouteDataRevisionService routeDataRevisionServiceMock;

    @Before
    public void setup() {

        TransportStop terminal = new TransportStop(TERMINAL, TERMINAL_ID.toString(), null, null);
        TransportStop address = new TransportStop(ADDRESS, null, new BigDecimal("49.23123"), new BigDecimal("8.1233"));

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(terminal, address, EMPTY, null, ROAD);
        segment = new TransportResponseDto.TransportResponseSegment(descriptionSegment);

        GeoLocation terminalGeoLocation = new GeoLocation(new BigDecimal("42.42"), new BigDecimal("8.42"));
        addressGeoLocation = new GeoLocation(new BigDecimal("49.23123"), new BigDecimal("8.1233"));

        when(conversionServiceMock.convert(matchesStopType(TERMINAL), any())).thenReturn(terminalGeoLocation);
        when(conversionServiceMock.convert(matchesStopType(ADDRESS), any())).thenReturn(addressGeoLocation);

        RouteResult routeResult = new RouteResult(40, 20, 300, asList("geometries1", "geometries2"), OK);
        when(routeServiceMock.route(terminalGeoLocation, addressGeoLocation, ROAD)).thenReturn(routeResult);
    }


    @Test
    public void withoutRouteRevision() {

        when(routeDataRevisionServiceMock.getRouteDataRevision(TERMINAL_ID, addressGeoLocation)).thenReturn(empty());

        sut.forNebenlauf(segment);

        assertThat(segment.distance.value, is(40));
        assertThat(segment.distance.unit, is(KILOMETRE));
        assertThat(segment.tollDistance.value, is(20));
        assertThat(segment.tollDistance.unit, is(KILOMETRE));
        assertThat(segment.duration.value, is(300));
        assertThat(segment.duration.unit, is(MINUTE));
        assertThat(segment.co2.value, comparesEqualTo(new BigDecimal("33.44")));
        assertThat(segment.co2.unit, is(KILOGRAM));
        assertThat(segment.geometries.get(0), is("geometries1"));
        assertThat(segment.geometries.get(1), is("geometries2"));
    }


    @Test
    public void withRouteRevision() {

        RouteDataRevision routeRevision = new RouteDataRevision();
        routeRevision.setTruckDistanceOneWayInKilometer(new BigDecimal("50"));
        routeRevision.setTollDistanceOneWayInKilometer(new BigDecimal("32"));

        when(routeDataRevisionServiceMock.getRouteDataRevision(TERMINAL_ID, addressGeoLocation))
            .thenReturn(Optional.of(routeRevision));

        sut.forNebenlauf(segment);

        assertThat(segment.distance.value, is(50));
        assertThat(segment.distance.unit, is(KILOMETRE));
        assertThat(segment.tollDistance.value, is(32));
        assertThat(segment.tollDistance.unit, is(KILOMETRE));
        assertThat(segment.duration.value, is(300));
        assertThat(segment.duration.unit, is(MINUTE));
        assertThat(segment.co2.value, comparesEqualTo(new BigDecimal("41.80")));
        assertThat(segment.co2.unit, is(KILOGRAM));
        assertThat(segment.geometries.get(0), is("geometries1"));
        assertThat(segment.geometries.get(1), is("geometries2"));
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
