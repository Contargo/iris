package net.contargo.iris.route2.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.route2.api.RoutePartEdgeDto;
import net.contargo.iris.route2.api.RoutePartNodeDto;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.routing.RoutingQueryStrategy;
import net.contargo.iris.routing.RoutingQueryStrategyProvider;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.route2.ModeOfTransport.ROAD;
import static net.contargo.iris.route2.ModeOfTransport.WATER;
import static net.contargo.iris.route2.RoutePartEdgeResultStatus.NO_ROUTE;
import static net.contargo.iris.route2.RoutePartEdgeResultStatus.OK;
import static net.contargo.iris.route2.api.RoutePartNodeDtoType.ADDRESS;
import static net.contargo.iris.route2.api.RoutePartNodeDtoType.GEOLOCATION;
import static net.contargo.iris.route2.api.RoutePartNodeDtoType.SEAPORT;
import static net.contargo.iris.route2.api.RoutePartNodeDtoType.TERMINAL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {

    @Mock
    private TerminalService terminalServiceMock;
    @Mock
    private SeaportService seaportServiceMock;
    @Mock
    private AddressServiceWrapper addressServiceWrapperMock;
    @Mock
    private RoutingQueryStrategyProvider routingQueryStrategyProviderMock;

    @InjectMocks
    private RouteService sut;

    private RoutePartNodeDto terminalNode;
    private RoutePartNodeDto seaportNode;
    private RoutePartNodeDto addressNode;
    private RoutePartNodeDto geoLocationNode;

    @Before
    public void setUp() {

        terminalNode = new RoutePartNodeDto(TERMINAL, new BigInteger("100023"), null, null, null);
        seaportNode = new RoutePartNodeDto(SEAPORT, new BigInteger("100025"), null, null, null);
        addressNode = new RoutePartNodeDto(ADDRESS, null, "HJHG234AS", null, null);
        geoLocationNode = new RoutePartNodeDto(GEOLOCATION, null, null, new BigDecimal("50.12312"),
                new BigDecimal("8.23123"));
    }


    @Test
    public void routeWithTerminalAndSeaport() {

        Terminal terminal = new Terminal();
        when(terminalServiceMock.getByUniqueId(new BigInteger("100023"))).thenReturn(terminal);

        Seaport seaport = new Seaport();
        when(seaportServiceMock.getByUniqueId(new BigInteger("100025"))).thenReturn(seaport);

        RoutingQueryStrategy strategyMock = mock(RoutingQueryStrategy.class);
        when(routingQueryStrategyProviderMock.strategy()).thenReturn(strategyMock);

        when(strategyMock.route(terminal, seaport, WATER)).thenReturn(new RoutingQueryResult(200, 61299.3, 22068, ZERO,
                asList("geometry1", "geometry2")));

        RoutePartEdgeDto edge = new RoutePartEdgeDto(terminalNode, seaportNode, WATER);

        RoutePartEdgeResult result = sut.route(edge);

        assertThat(result.getDistance(), is(61299.3));
        assertThat(result.getDuration(), is(22068.0));
        assertThat(result.getGeometries().get(0), is("geometry1"));
        assertThat(result.getGeometries().get(1), is("geometry2"));
        assertThat(result.getStatus(), is(OK));
    }


    @Test
    public void routeWithAddressAndGeolocation() {

        Address address = new Address();
        when(addressServiceWrapperMock.getByHashKey("HJHG234AS")).thenReturn(address);

        GeoLocation geoLocation = new GeoLocation(new BigDecimal("50.12312"), new BigDecimal("8.23123"));

        RoutingQueryStrategy strategyMock = mock(RoutingQueryStrategy.class);
        when(routingQueryStrategyProviderMock.strategy()).thenReturn(strategyMock);

        when(strategyMock.route(address, geoLocation, ROAD)).thenReturn(new RoutingQueryResult(200, 23599.3, 682,
                new BigDecimal("6455"), asList("geometry3", "geometry4")));

        RoutePartEdgeDto edge = new RoutePartEdgeDto(addressNode, geoLocationNode, ROAD);

        RoutePartEdgeResult result = sut.route(edge);

        assertThat(result.getDistance(), is(23599.3));
        assertThat(result.getDuration(), is(682.0));
        assertThat(result.getGeometries().get(0), is("geometry3"));
        assertThat(result.getGeometries().get(1), is("geometry4"));
        assertThat(result.getStatus(), is(OK));
    }


    @Test
    public void routeWithError() {

        Address address = new Address();
        when(addressServiceWrapperMock.getByHashKey("HJHG234AS")).thenReturn(address);

        GeoLocation geoLocation = new GeoLocation(new BigDecimal("50.12312"), new BigDecimal("8.23123"));

        RoutingQueryStrategy strategyMock = mock(RoutingQueryStrategy.class);
        when(routingQueryStrategyProviderMock.strategy()).thenReturn(strategyMock);

        when(strategyMock.route(address, geoLocation, ROAD)).thenReturn(new RoutingQueryResult(207, 0.0, 0.0, null));

        RoutePartEdgeDto edge = new RoutePartEdgeDto(addressNode, geoLocationNode, ROAD);

        RoutePartEdgeResult result = sut.route(edge);

        assertThat(result.getStatus(), is(NO_ROUTE));
    }
}
