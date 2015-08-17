package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.builder.DirectTruckRouteBuilder;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.TruckRouteService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InOrder;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static net.contargo.iris.Movement.move;
import static net.contargo.iris.Movement.to;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.TRUCK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Unit test for {@link DirectTruckRouteBuilder}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class DirectTruckRouteBuilderUnitTest {

    private DirectTruckRouteBuilder sut;

    @Mock
    private TruckRouteService truckRouteServiceMock;
    @Mock
    private TruckRoute truckRouteMock;

    private Terminal terminal;
    private Seaport seaport;

    @Before
    public void setup() {

        sut = new DirectTruckRouteBuilder(truckRouteServiceMock);

        terminal = new Terminal();
        terminal.setId(1L);
        terminal.setName("Terminal");

        seaport = new Seaport();
        seaport.setId(1L);
        seaport.setName("Seaport");
    }


    @Test
    public void testEliminateTerminalsOneWayImport() {

        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("49.0"), new BigDecimal("8.0"));

        // actual route: seaport -> terminal -> address -> terminal
        Route oneWayImport = move(seaport, ContainerType.THIRTY, to(terminal, BARGE), to(addressGeoLocation, TRUCK),
                to(terminal, TRUCK));

        // expected route: seaport -> address -> terminal
        Route expectedRoute = move(seaport, ContainerType.THIRTY, to(addressGeoLocation, TRUCK), to(terminal, TRUCK));

        when(truckRouteServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(truckRouteMock);

        assertSameParts(sut.getCorrespondingDirectTruckRoute(oneWayImport), expectedRoute);

        InOrder order = inOrder(truckRouteServiceMock);
        order.verify(truckRouteServiceMock).route(any(GeoLocation.class), eq(addressGeoLocation));
        order.verify(truckRouteServiceMock).route(eq(addressGeoLocation), any(GeoLocation.class));
    }


    @Test
    public void testEliminateTerminalsOneWayExport() {

        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("49.0"), new BigDecimal("8.0"));

        // actual route: terminal -> address -> terminal -> seaport
        Route oneWayExport = move(terminal, ContainerType.THIRTY, to(addressGeoLocation, TRUCK), to(terminal, TRUCK),
                to(seaport, BARGE));

        // expected route: terminal -> address -> seaport
        Route expectedRoute = move(terminal, ContainerType.THIRTY, to(addressGeoLocation, TRUCK), to(seaport, TRUCK));

        TruckRoute truckRoute = mock(TruckRoute.class);
        when(truckRouteServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(truckRoute);

        assertSameParts(sut.getCorrespondingDirectTruckRoute(oneWayExport), expectedRoute);

        InOrder order = inOrder(truckRouteServiceMock);
        order.verify(truckRouteServiceMock).route(any(GeoLocation.class), eq(addressGeoLocation));
        order.verify(truckRouteServiceMock).route(eq(addressGeoLocation), any(GeoLocation.class));
    }


    @Test
    public void testEliminateTerminalsRoundTrip() {

        GeoLocation addressGeoLocation = new GeoLocation(new BigDecimal("49.0"), new BigDecimal("8.0"));

        // actual route: seaport -> terminal -> address -> terminal -> seaport
        Route roundTrip = move(seaport, ContainerType.THIRTY, to(terminal, BARGE), to(addressGeoLocation, TRUCK),
                to(terminal, TRUCK), to(seaport, BARGE));

        // expected route: seaport -> address -> seaport
        Route expectedRoute = move(seaport, ContainerType.THIRTY, to(addressGeoLocation, TRUCK), to(seaport, TRUCK));

        TruckRoute truckRoute = mock(TruckRoute.class);
        when(truckRouteServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(truckRoute);

        assertSameParts(sut.getCorrespondingDirectTruckRoute(roundTrip), expectedRoute);

        InOrder order = inOrder(truckRouteServiceMock);
        order.verify(truckRouteServiceMock).route(any(GeoLocation.class), eq(addressGeoLocation));
        order.verify(truckRouteServiceMock).route(eq(addressGeoLocation), any(GeoLocation.class));
    }


    private void assertSameParts(Route actual, Route expected) {

        assertThat(actual, notNullValue());
        assertThat(actual.getData().getParts().size(), is(expected.getData().getParts().size()));

        for (int i = 0; i < expected.getData().getParts().size(); i++) {
            RoutePart expectedPart = expected.getData().getParts().get(i);
            RoutePart returnedPart = actual.getData().getParts().get(i);

            assertThat(returnedPart.getOrigin(), is(expectedPart.getOrigin()));
            assertThat(returnedPart.getDestination(), is(expectedPart.getDestination()));
            assertThat(returnedPart.getRouteType(), is(expectedPart.getRouteType()));
        }
    }
}
