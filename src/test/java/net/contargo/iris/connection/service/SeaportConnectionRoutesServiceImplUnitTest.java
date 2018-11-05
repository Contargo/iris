package net.contargo.iris.connection.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.advice.MainRunAdvisor;
import net.contargo.iris.connection.advice.MainRunStrategy;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteCombo;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import static net.contargo.iris.container.ContainerType.TWENTY_LIGHT;
import static net.contargo.iris.route.RouteDirection.EXPORT;
import static net.contargo.iris.route.RouteDirection.IMPORT;
import static net.contargo.iris.route.RouteProduct.ONEWAY;
import static net.contargo.iris.route.RouteProduct.ROUNDTRIP;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.DTRUCK;
import static net.contargo.iris.route.RouteType.RAIL;
import static net.contargo.iris.route.RouteType.TRUCK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class SeaportConnectionRoutesServiceImplUnitTest {

    private SeaportConnectionRoutesServiceImpl sut;

    @Mock
    private SeaportTerminalConnectionService seaportTerminalConnectionService;
    @Mock
    private MainRunAdvisor mainRunAdvisorMock;

    private Seaport seaport;
    private GeoLocation destination;
    private ContainerType containerType;

    @Mock
    private MainRunConnection connectionMock;

    @Before
    public void setup() throws Exception {

        RoutePart bargeRoute = new RoutePart();
        bargeRoute.setRouteType(BARGE);

        RoutePart railRoute = new RoutePart();
        railRoute.setRouteType(RAIL);

        RoutePart truckRoute = new RoutePart();
        truckRoute.setRouteType(TRUCK);

        seaport = new Seaport();
        destination = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);
        containerType = TWENTY_LIGHT;

        Terminal terminal = new Terminal();

        when(connectionMock.getTerminal()).thenReturn(terminal);
        when(connectionMock.getEverythingEnabled()).thenReturn(true);

        when(seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(any(Seaport.class),
                    any(RouteType.class))).thenReturn(singletonList(connectionMock));

        sut = new SeaportConnectionRoutesServiceImpl(seaportTerminalConnectionService, mainRunAdvisorMock, false);
    }


    private Route getExpectedRoute(List<RouteType> expectedRouteTypes) {

        Route expectedRoute = new Route();
        RouteData expectedRouteData = new RouteData();

        List<RoutePart> expectedRouteParts = new ArrayList<>();

        for (int i = 0; i < expectedRouteTypes.size(); i++) {
            RoutePart routePart = new RoutePart();
            routePart.setRouteType(expectedRouteTypes.get(i));
            expectedRouteParts.add(i, routePart);
        }

        expectedRouteData.setParts(expectedRouteParts);
        expectedRoute.setData(expectedRouteData);

        return expectedRoute;
    }


    @Test
    public void testGetPossibleConnectionsOneWayImport() {

        List<RouteType> expectedRouteTypes = new ArrayList<>();
        expectedRouteTypes.add(BARGE);
        expectedRouteTypes.add(TRUCK);
        expectedRouteTypes.add(TRUCK);

        Route expectedRoute = getExpectedRoute(expectedRouteTypes);

        when(connectionMock.getRouteType()).thenReturn(BARGE);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ONEWAY, IMPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(connectionMock, destination, TWENTY_LIGHT)).thenReturn(expectedRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, containerType, IMPORT,
                RouteCombo.WATERWAY);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaport, information);

        assertThat(1, is(routes.size()));
        assertThat(3, is(routes.get(0).getData().getParts().size()));
        assertThat(BARGE, is(routes.get(0).getData().getParts().get(0).getRouteType()));
        assertThat(TRUCK, is(routes.get(0).getData().getParts().get(1).getRouteType()));
        assertThat(TRUCK, is(routes.get(0).getData().getParts().get(2).getRouteType()));
    }


    @Test
    public void testGetPossibleConnectionsOneWayExport() {

        List<RouteType> expectedRouteTypes = new ArrayList<>();
        expectedRouteTypes.add(TRUCK);
        expectedRouteTypes.add(TRUCK);
        expectedRouteTypes.add(RAIL);

        Route expectedRoute = getExpectedRoute(expectedRouteTypes);

        when(connectionMock.getRouteType()).thenReturn(RAIL);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ONEWAY, EXPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(connectionMock, destination, TWENTY_LIGHT)).thenReturn(expectedRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, containerType, EXPORT,
                RouteCombo.RAILWAY);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaport, information);

        // assert stuff
        assertThat(routes.size(), is(1));
        assertThat(routes.get(0).getData().getParts().size(), is(3));
        assertThat(routes.get(0).getData().getParts().get(0).getRouteType(), is(TRUCK));
        assertThat(routes.get(0).getData().getParts().get(1).getRouteType(), is(TRUCK));
        assertThat(routes.get(0).getData().getParts().get(2).getRouteType(), is(RAIL));
    }


    @Test
    public void testGetPossibleConnectionsRoundTrip() {

        List<RouteType> expectedRouteTypes = new ArrayList<>();
        expectedRouteTypes.add(BARGE);
        expectedRouteTypes.add(TRUCK);
        expectedRouteTypes.add(TRUCK);
        expectedRouteTypes.add(BARGE);

        Route expectedRoute = getExpectedRoute(expectedRouteTypes);
        when(connectionMock.getRouteType()).thenReturn(BARGE);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ROUNDTRIP, IMPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(connectionMock, destination, TWENTY_LIGHT)).thenReturn(expectedRoute);

        RouteInformation information = new RouteInformation(destination, ROUNDTRIP, containerType, IMPORT,
                RouteCombo.WATERWAY);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaport, information);

        // assert stuff
        assertThat(routes.size(), is(1));
        assertThat(routes.get(0).getData().getParts().size(), is(4));
        assertThat(routes.get(0).getData().getParts().get(0).getRouteType(), is(BARGE));
        assertThat(routes.get(0).getData().getParts().get(1).getRouteType(), is(TRUCK));
        assertThat(routes.get(0).getData().getParts().get(2).getRouteType(), is(TRUCK));
        assertThat(routes.get(0).getData().getParts().get(3).getRouteType(), is(BARGE));
    }


    @Test
    public void testComboAll() {

        List<RouteType> expectedFirstRouteTypes = new ArrayList<>();
        expectedFirstRouteTypes.add(BARGE);
        expectedFirstRouteTypes.add(TRUCK);
        expectedFirstRouteTypes.add(TRUCK);

        Route expectedFirstRoute = getExpectedRoute(expectedFirstRouteTypes);

        MainRunConnection firstConnection = mock(MainRunConnection.class);
        when(firstConnection.getSeaport()).thenReturn(seaport);
        when(firstConnection.getEverythingEnabled()).thenReturn(true);
        when(firstConnection.getRouteType()).thenReturn(BARGE);

        MainRunConnection disabledConnection = mock(MainRunConnection.class);
        when(disabledConnection.getSeaport()).thenReturn(seaport);
        when(disabledConnection.getEverythingEnabled()).thenReturn(false);
        when(disabledConnection.getRouteType()).thenReturn(BARGE);

        List<RouteType> expectedSecondRouteTypes = new ArrayList<>();
        expectedSecondRouteTypes.add(RAIL);
        expectedSecondRouteTypes.add(TRUCK);
        expectedSecondRouteTypes.add(TRUCK);

        Route expectedSecondRoute = getExpectedRoute(expectedSecondRouteTypes);

        MainRunConnection secondConnection = mock(MainRunConnection.class);
        when(secondConnection.getSeaport()).thenReturn(seaport);
        when(secondConnection.getEverythingEnabled()).thenReturn(true);
        when(secondConnection.getRouteType()).thenReturn(RAIL);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(eq(ONEWAY), eq(IMPORT))).thenReturn(mainRunStrategy);

        when(seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(seaport, BARGE)).thenReturn(asList(
                firstConnection, disabledConnection));
        when(seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(seaport, RAIL)).thenReturn(
            singletonList(secondConnection));

        when(mainRunStrategy.getRoute(firstConnection, destination, TWENTY_LIGHT)).thenReturn(expectedFirstRoute);
        when(mainRunStrategy.getRoute(secondConnection, destination, TWENTY_LIGHT)).thenReturn(expectedSecondRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, containerType, IMPORT,
                RouteCombo.ALL);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaport, information);

        // assert stuff
        assertThat(routes.size(), is(2));

        assertThat(routes.get(0).getData().getParts().size(), is(3));
        assertThat(routes.get(0).getData().getParts().get(0).getRouteType(), is(BARGE));
        assertThat(routes.get(0).getData().getParts().get(1).getRouteType(), is(TRUCK));
        assertThat(routes.get(0).getData().getParts().get(2).getRouteType(), is(TRUCK));

        assertThat(routes.get(1).getData().getParts().size(), is(3));
        assertThat(routes.get(1).getData().getParts().get(0).getRouteType(), is(RAIL));
        assertThat(routes.get(1).getData().getParts().get(1).getRouteType(), is(TRUCK));
        assertThat(routes.get(1).getData().getParts().get(2).getRouteType(), is(TRUCK));
    }


    @Test
    public void testComboAllDtruckActive() {

        sut = new SeaportConnectionRoutesServiceImpl(seaportTerminalConnectionService, mainRunAdvisorMock, true);

        List<RouteType> expectedFirstRouteTypes = new ArrayList<>();
        expectedFirstRouteTypes.add(BARGE);
        expectedFirstRouteTypes.add(TRUCK);
        expectedFirstRouteTypes.add(TRUCK);

        Route expectedFirstRoute = getExpectedRoute(expectedFirstRouteTypes);

        MainRunConnection firstConnection = mock(MainRunConnection.class);
        when(firstConnection.getSeaport()).thenReturn(seaport);
        when(firstConnection.getEverythingEnabled()).thenReturn(true);
        when(firstConnection.getRouteType()).thenReturn(BARGE);

        MainRunConnection disabledConnection = mock(MainRunConnection.class);
        when(disabledConnection.getSeaport()).thenReturn(seaport);
        when(disabledConnection.getEverythingEnabled()).thenReturn(false);
        when(disabledConnection.getRouteType()).thenReturn(BARGE);

        List<RouteType> expectedSecondRouteTypes = new ArrayList<>();
        expectedSecondRouteTypes.add(RAIL);
        expectedSecondRouteTypes.add(TRUCK);
        expectedSecondRouteTypes.add(TRUCK);

        Route expectedSecondRoute = getExpectedRoute(expectedSecondRouteTypes);

        MainRunConnection secondConnection = mock(MainRunConnection.class);
        when(secondConnection.getSeaport()).thenReturn(seaport);
        when(secondConnection.getEverythingEnabled()).thenReturn(true);
        when(secondConnection.getRouteType()).thenReturn(RAIL);

        List<RouteType> expectedFourthRouteTypes = new ArrayList<>();
        expectedFourthRouteTypes.add(DTRUCK);
        expectedFourthRouteTypes.add(TRUCK);
        expectedFourthRouteTypes.add(TRUCK);

        Route expectedFourthRoute = getExpectedRoute(expectedFourthRouteTypes);

        MainRunConnection fourthConnection = mock(MainRunConnection.class, "fourthConnection");
        when(fourthConnection.getSeaport()).thenReturn(seaport);
        when(fourthConnection.getEverythingEnabled()).thenReturn(true);
        when(fourthConnection.getRouteType()).thenReturn(DTRUCK);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(eq(ONEWAY), eq(IMPORT))).thenReturn(mainRunStrategy);

        when(seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(seaport, BARGE)).thenReturn(asList(
                firstConnection, disabledConnection));
        when(seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(seaport, RAIL)).thenReturn(
            singletonList(secondConnection));
        when(seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(seaport, DTRUCK)).thenReturn(
            singletonList(fourthConnection));

        when(mainRunStrategy.getRoute(firstConnection, destination, TWENTY_LIGHT)).thenReturn(expectedFirstRoute);
        when(mainRunStrategy.getRoute(secondConnection, destination, TWENTY_LIGHT)).thenReturn(expectedSecondRoute);
        when(mainRunStrategy.getRoute(fourthConnection, destination, TWENTY_LIGHT)).thenReturn(expectedFourthRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, containerType, IMPORT,
                RouteCombo.ALL);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaport, information);

        // assert stuff
        assertThat(routes.size(), is(3));

        assertThat(routes.get(0).getData().getParts().size(), is(3));
        assertThat(routes.get(0).getData().getParts().get(0).getRouteType(), is(BARGE));
        assertThat(routes.get(0).getData().getParts().get(1).getRouteType(), is(TRUCK));
        assertThat(routes.get(0).getData().getParts().get(2).getRouteType(), is(TRUCK));

        assertThat(routes.get(1).getData().getParts().size(), is(3));
        assertThat(routes.get(1).getData().getParts().get(0).getRouteType(), is(RAIL));
        assertThat(routes.get(1).getData().getParts().get(1).getRouteType(), is(TRUCK));
        assertThat(routes.get(1).getData().getParts().get(2).getRouteType(), is(TRUCK));

        assertThat(routes.get(2).getData().getParts().size(), is(3));
        assertThat(routes.get(2).getData().getParts().get(0).getRouteType(), is(DTRUCK));
        assertThat(routes.get(2).getData().getParts().get(1).getRouteType(), is(TRUCK));
        assertThat(routes.get(2).getData().getParts().get(2).getRouteType(), is(TRUCK));
    }


    @Test
    public void testDirectTruck() {

        GeoLocation destination = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);
        Terminal terminal = new Terminal();

        when(connectionMock.getTerminal()).thenReturn(terminal);

        Route expectedRoute = mock(Route.class);
        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ONEWAY, IMPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(connectionMock, destination, TWENTY_LIGHT)).thenReturn(expectedRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, TWENTY_LIGHT, IMPORT, null);
        Route route = sut.getMainRunRoute(connectionMock, information, BARGE);

        assertThat(route, is(expectedRoute));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGetMainRunConnectionWithException() {

        RouteInformation information = new RouteInformation(destination, ONEWAY, TWENTY_LIGHT, IMPORT, null);
        sut.getMainRunRoute(connectionMock, information, TRUCK);
    }
}
