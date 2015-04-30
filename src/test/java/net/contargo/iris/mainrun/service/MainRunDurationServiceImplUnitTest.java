package net.contargo.iris.mainrun.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.rounding.RoundingService;
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

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.argThat;

import static org.mockito.Mockito.when;


/**
 * Unit test for {@link MainRunDurationServiceImpl}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class MainRunDurationServiceImplUnitTest {

    private static final BigDecimal DIESEL_DISTANCE = BigDecimal.ONE;
    private static final BigDecimal ELECTRICAL_DISTANCE = BigDecimal.TEN;

    private MainRunDurationServiceImpl sut;

    @Mock
    private RoundingService roundingServiceMock;

    private RoutePart routePart;
    private Terminal terminal;
    private Seaport seaport;
    private MainRunConnection mainRunConnection;

    @Before
    public void setUp() {

        sut = new MainRunDurationServiceImpl(roundingServiceMock);

        routePart = new RoutePart();
        terminal = new Terminal();
        seaport = new Seaport();
        mainRunConnection = new MainRunConnection();
        mainRunConnection.setDieselDistance(DIESEL_DISTANCE);
        mainRunConnection.setElectricDistance(ELECTRICAL_DISTANCE);
    }


    @Test
    public void getMainRunRoutePartDurationBargeUpStream() {

        routePart.setOrigin(seaport);
        routePart.setDestination(terminal);
        routePart.setRouteType(RouteType.BARGE);

        when(roundingServiceMock.roundDuration(argThat(closeTo(new BigDecimal("66"), BigDecimal.ZERO)))).thenReturn(
            new BigDecimal("66"));

        BigDecimal duration = sut.getMainRunRoutePartDuration(mainRunConnection, routePart);

        assertThat(duration, closeTo(new BigDecimal("66"), BigDecimal.ZERO));
    }


    @Test
    public void getMainRunRoutePartDurationBargeDownStream() {

        routePart.setOrigin(terminal);
        routePart.setDestination(seaport);
        routePart.setRouteType(RouteType.BARGE);
        mainRunConnection.setDieselDistance(BigDecimal.ONE);
        mainRunConnection.setElectricDistance(BigDecimal.TEN);

        when(roundingServiceMock.roundDuration(argThat(closeTo(new BigDecimal("36.6666"), BigDecimal.ZERO))))
            .thenReturn(new BigDecimal("36.67"));

        BigDecimal duration = sut.getMainRunRoutePartDuration(mainRunConnection, routePart);

        assertThat(duration, closeTo(new BigDecimal("36.67"), BigDecimal.ZERO));
    }


    @Test
    public void getMainRunRoutePartDurationRail() {

        routePart.setOrigin(terminal);
        routePart.setDestination(seaport);
        routePart.setRouteType(RouteType.RAIL);
        mainRunConnection.setDieselDistance(BigDecimal.ONE);
        mainRunConnection.setElectricDistance(BigDecimal.TEN);

        when(roundingServiceMock.roundDuration(argThat(closeTo(new BigDecimal("14.6664"), BigDecimal.ZERO))))
            .thenReturn(new BigDecimal("14.67"));

        BigDecimal duration = sut.getMainRunRoutePartDuration(mainRunConnection, routePart);

        assertThat(duration, closeTo(new BigDecimal("14.67"), BigDecimal.ZERO));
    }


    @Test
    public void getMainRunRoutePartDurationTruck() {

        routePart.setOrigin(terminal);
        routePart.setDestination(seaport);
        routePart.setRouteType(RouteType.TRUCK);

        when(roundingServiceMock.roundDuration(argThat(closeTo(BigDecimal.ZERO, BigDecimal.ZERO)))).thenReturn(
            BigDecimal.ZERO);

        BigDecimal duration = sut.getMainRunRoutePartDuration(mainRunConnection, routePart);

        assertThat(duration, is(BigDecimal.ZERO));
    }
}
