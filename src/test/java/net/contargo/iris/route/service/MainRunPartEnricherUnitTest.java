package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.distance.service.ConnectionDistanceService;
import net.contargo.iris.mainrun.service.MainRunDurationService;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;


/**
 * Unit test for implementation of MainRunPartEnricher: {@link net.contargo.iris.route.service.MainRunPartEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class MainRunPartEnricherUnitTest {

    private static final BigDecimal ELECTRICAL_DISTANCE = ONE;
    private static final BigDecimal DIESEL_DISTANCE = TEN;
    private static final BigDecimal DISTANCE = BigDecimal.ZERO;
    private static final BigDecimal DURATION = TEN;

    @InjectMocks
    private MainRunPartEnricher sut;

    @Mock
    private MainRunConnectionService mainRunConnectionServiceMock;
    @Mock
    private MainRunDurationService mainRunDurationServiceMock;
    @Mock
    private ConnectionDistanceService connectionDistanceServiceMock;

    // This is no mock because it is final
    private EnricherContext enricherContext;
    private MainRunConnection mainrunConnection;
    private RoutePart routePart;

    @Before
    public void setup() {

        mainrunConnection = new MainRunConnection();

        Terminal terminal = new Terminal(new GeoLocation(ONE, ONE));
        Seaport seaport = new Seaport(new GeoLocation(TEN, TEN));

        RouteType routeType = RouteType.BARGE;

        mainrunConnection.setSeaport(seaport);

        routePart = new RoutePart();
        routePart.setOrigin(seaport);
        routePart.setDestination(terminal);
        routePart.setRouteType(routeType);

        when(mainRunConnectionServiceMock.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal, seaport,
                    routeType)).thenReturn(mainrunConnection);
        when(connectionDistanceServiceMock.getElectricDistance(mainrunConnection)).thenReturn(ELECTRICAL_DISTANCE);
        when(connectionDistanceServiceMock.getDistance(mainrunConnection)).thenReturn(DISTANCE);
        when(connectionDistanceServiceMock.getDieselDistance(mainrunConnection)).thenReturn(DIESEL_DISTANCE);
        when(mainRunDurationServiceMock.getMainRunRoutePartDuration(mainrunConnection, routePart)).thenReturn(
            DURATION);

        enricherContext = new EnricherContext.Builder().build();
    }


    @Test
    public void enrich() throws CriticalEnricherException {

        sut.enrich(routePart, enricherContext);

        assertThat(routePart.getData().getElectricDistance(), is(ELECTRICAL_DISTANCE));
        assertThat(routePart.getData().getDieselDistance(), is(DIESEL_DISTANCE));
        assertThat(routePart.getData().getDistance(), is(DISTANCE));
        assertThat(routePart.getData().getDuration(), is(DURATION));

        verify(connectionDistanceServiceMock).getElectricDistance(mainrunConnection);
        verify(connectionDistanceServiceMock).getDieselDistance(mainrunConnection);
        verify(connectionDistanceServiceMock).getDistance(mainrunConnection);
    }


    @Test
    public void enrichRouteTypeIsWrong() throws CriticalEnricherException {

        // wrong RouteType => all attributes has to be BigDecimal(0)
        routePart.setRouteType(RouteType.TRUCK);

        sut.enrich(routePart, enricherContext);

        assertThat(routePart.getData().getDistance(), nullValue());
        assertThat(routePart.getData().getDieselDistance(), nullValue());
        assertThat(routePart.getData().getElectricDistance(), nullValue());
        assertThat(routePart.getData().getDuration(), nullValue());

        verify(connectionDistanceServiceMock, never()).getDistance(mainrunConnection);
        verify(connectionDistanceServiceMock, never()).getDieselDistance(mainrunConnection);
        verify(connectionDistanceServiceMock, never()).getElectricDistance(mainrunConnection);
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichWithoutMainRunConnection() throws CriticalEnricherException {

        when(mainRunConnectionServiceMock.findRoutingConnectionBetweenTerminalAndSeaportByType(any(Terminal.class),
                    any(Seaport.class), any(RouteType.class))).thenReturn(null);

        sut.enrich(routePart, enricherContext);
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichOriginIsNull() throws CriticalEnricherException {

        routePart.setOrigin(null);
        routePart.setRouteType(RouteType.RAIL);

        sut.enrich(routePart, enricherContext);
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichDestinationIsNull() throws CriticalEnricherException {

        routePart.setDestination(null);
        routePart.setRouteType(RouteType.RAIL);

        sut.enrich(routePart, enricherContext);
    }
}
