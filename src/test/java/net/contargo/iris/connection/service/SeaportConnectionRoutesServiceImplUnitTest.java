package net.contargo.iris.connection.service;

import com.vividsolutions.jts.util.Assert;

import net.contargo.iris.GeoLocation;
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
import java.util.Collections;
import java.util.List;

import static net.contargo.iris.container.ContainerType.TWENTY_LIGHT;
import static net.contargo.iris.route.RouteDirection.EXPORT;
import static net.contargo.iris.route.RouteDirection.IMPORT;
import static net.contargo.iris.route.RouteProduct.ONEWAY;
import static net.contargo.iris.route.RouteProduct.ROUNDTRIP;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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

    private Seaport seaPort;
    private GeoLocation destination;
    private ContainerType containerType;
    private Terminal terminal;

    @Before
    public void setup() throws Exception {

        List<Terminal> terminals = Collections.singletonList(new Terminal());

        when(seaportTerminalConnectionService.getTerminalsConnectedToSeaPortByRouteType(any(Seaport.class),
                    any(RouteType.class))).thenReturn(terminals);

        RoutePart bargeRoute = new RoutePart();
        bargeRoute.setRouteType(RouteType.BARGE);

        RoutePart railRoute = new RoutePart();
        railRoute.setRouteType(RouteType.RAIL);

        RoutePart truckRoute = new RoutePart();
        truckRoute.setRouteType(RouteType.TRUCK);

        seaPort = new Seaport();
        destination = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);
        containerType = TWENTY_LIGHT;
        terminal = new Terminal();

        sut = new SeaportConnectionRoutesServiceImpl(seaportTerminalConnectionService, mainRunAdvisorMock);
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
        expectedRouteTypes.add(RouteType.BARGE);
        expectedRouteTypes.add(RouteType.TRUCK);
        expectedRouteTypes.add(RouteType.TRUCK);

        Route expectedRoute = getExpectedRoute(expectedRouteTypes);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ONEWAY, IMPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(seaPort, destination, terminal, TWENTY_LIGHT, RouteType.BARGE)).thenReturn(
            expectedRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, containerType, IMPORT,
                RouteCombo.WATERWAY);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaPort, information);

        Assert.equals(1, routes.size());
        Assert.equals(3, routes.get(0).getData().getParts().size());
        Assert.equals(RouteType.BARGE, routes.get(0).getData().getParts().get(0).getRouteType());
        Assert.equals(RouteType.TRUCK, routes.get(0).getData().getParts().get(1).getRouteType());
        Assert.equals(RouteType.TRUCK, routes.get(0).getData().getParts().get(2).getRouteType());
    }


    @Test
    public void testGetPossibleConnectionsOneWayExport() {

        List<RouteType> expectedRouteTypes = new ArrayList<>();
        expectedRouteTypes.add(RouteType.TRUCK);
        expectedRouteTypes.add(RouteType.TRUCK);
        expectedRouteTypes.add(RouteType.RAIL);

        Route expectedRoute = getExpectedRoute(expectedRouteTypes);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ONEWAY, EXPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(seaPort, destination, terminal, TWENTY_LIGHT, RouteType.RAIL)).thenReturn(
            expectedRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, containerType, EXPORT,
                RouteCombo.RAILWAY);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaPort, information);

        // assert stuff
        assertThat(routes.size(), is(1));
        assertThat(routes.get(0).getData().getParts().size(), is(3));
        assertThat(routes.get(0).getData().getParts().get(0).getRouteType(), is(RouteType.TRUCK));
        assertThat(routes.get(0).getData().getParts().get(1).getRouteType(), is(RouteType.TRUCK));
        assertThat(routes.get(0).getData().getParts().get(2).getRouteType(), is(RouteType.RAIL));
    }


    @Test
    public void testGetPossibleConnectionsRoundTrip() {

        List<RouteType> expectedRouteTypes = new ArrayList<>();
        expectedRouteTypes.add(RouteType.BARGE);
        expectedRouteTypes.add(RouteType.TRUCK);
        expectedRouteTypes.add(RouteType.TRUCK);
        expectedRouteTypes.add(RouteType.BARGE);

        Route expectedRoute = getExpectedRoute(expectedRouteTypes);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ROUNDTRIP, IMPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(seaPort, destination, terminal, TWENTY_LIGHT, RouteType.BARGE)).thenReturn(
            expectedRoute);

        RouteInformation information = new RouteInformation(destination, ROUNDTRIP, containerType, IMPORT,
                RouteCombo.WATERWAY);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaPort, information);

        // assert stuff
        assertThat(routes.size(), is(1));
        assertThat(routes.get(0).getData().getParts().size(), is(4));
        assertThat(routes.get(0).getData().getParts().get(0).getRouteType(), is(RouteType.BARGE));
        assertThat(routes.get(0).getData().getParts().get(1).getRouteType(), is(RouteType.TRUCK));
        assertThat(routes.get(0).getData().getParts().get(2).getRouteType(), is(RouteType.TRUCK));
        assertThat(routes.get(0).getData().getParts().get(3).getRouteType(), is(RouteType.BARGE));
    }


    @Test
    public void testComboAll() {

        List<RouteType> expectedFirstRouteTypes = new ArrayList<>();
        expectedFirstRouteTypes.add(RouteType.BARGE);
        expectedFirstRouteTypes.add(RouteType.TRUCK);
        expectedFirstRouteTypes.add(RouteType.TRUCK);

        Route expectedFirstRoute = getExpectedRoute(expectedFirstRouteTypes);

        List<RouteType> expectedSecondRouteTypes = new ArrayList<>();
        expectedSecondRouteTypes.add(RouteType.RAIL);
        expectedSecondRouteTypes.add(RouteType.TRUCK);
        expectedSecondRouteTypes.add(RouteType.TRUCK);

        Route expectedSecondRoute = getExpectedRoute(expectedSecondRouteTypes);

        List<RouteType> expectedThirdRouteTypes = new ArrayList<>();
        expectedThirdRouteTypes.add(RouteType.BARGE_RAIL);
        expectedThirdRouteTypes.add(RouteType.TRUCK);
        expectedThirdRouteTypes.add(RouteType.TRUCK);

        Route expectedThirdRoute = getExpectedRoute(expectedThirdRouteTypes);

        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ONEWAY, IMPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(seaPort, destination, terminal, TWENTY_LIGHT, RouteType.BARGE)).thenReturn(
            expectedFirstRoute);
        when(mainRunStrategy.getRoute(seaPort, destination, terminal, TWENTY_LIGHT, RouteType.RAIL)).thenReturn(
            expectedSecondRoute);
        when(mainRunStrategy.getRoute(seaPort, destination, terminal, TWENTY_LIGHT, RouteType.BARGE_RAIL)).thenReturn(
            expectedThirdRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, containerType, IMPORT, RouteCombo.ALL);

        // run test
        List<Route> routes = sut.getAvailableSeaportConnectionRoutes(seaPort, information);

        // assert stuff
        assertThat(routes.size(), is(3));

        assertThat(routes.get(0).getData().getParts().size(), is(3));
        assertThat(routes.get(0).getData().getParts().get(0).getRouteType(), is(RouteType.BARGE));
        assertThat(routes.get(0).getData().getParts().get(1).getRouteType(), is(RouteType.TRUCK));
        assertThat(routes.get(0).getData().getParts().get(2).getRouteType(), is(RouteType.TRUCK));

        assertThat(routes.get(1).getData().getParts().size(), is(3));
        assertThat(routes.get(1).getData().getParts().get(0).getRouteType(), is(RouteType.RAIL));
        assertThat(routes.get(1).getData().getParts().get(1).getRouteType(), is(RouteType.TRUCK));
        assertThat(routes.get(1).getData().getParts().get(2).getRouteType(), is(RouteType.TRUCK));

        assertThat(routes.get(2).getData().getParts().size(), is(3));
        assertThat(routes.get(2).getData().getParts().get(0).getRouteType(), is(RouteType.BARGE_RAIL));
        assertThat(routes.get(2).getData().getParts().get(1).getRouteType(), is(RouteType.TRUCK));
        assertThat(routes.get(2).getData().getParts().get(2).getRouteType(), is(RouteType.TRUCK));
    }


    @Test
    public void testDirectTruck() {

        GeoLocation destination = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);
        Terminal terminal = new Terminal();

        Route expectedRoute = mock(Route.class);
        MainRunStrategy mainRunStrategy = mock(MainRunStrategy.class);
        when(mainRunAdvisorMock.advice(ONEWAY, IMPORT)).thenReturn(mainRunStrategy);
        when(mainRunStrategy.getRoute(seaPort, destination, terminal, TWENTY_LIGHT, RouteType.BARGE)).thenReturn(
            expectedRoute);

        RouteInformation information = new RouteInformation(destination, ONEWAY, TWENTY_LIGHT, IMPORT, null);
        Route route = sut.getMainRunRoute(seaPort, terminal, information, RouteType.BARGE);

        assertThat(route, is(expectedRoute));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGetMainRunConnectionWithException() {

        RouteInformation information = new RouteInformation(destination, ONEWAY, TWENTY_LIGHT, IMPORT, null);
        sut.getMainRunRoute(seaPort, terminal, information, RouteType.TRUCK);
    }
}
