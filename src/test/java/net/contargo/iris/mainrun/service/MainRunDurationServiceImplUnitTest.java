package net.contargo.iris.mainrun.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static net.contargo.iris.route.RouteType.BARGE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * Unit test for {@link MainRunDurationServiceImpl}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class MainRunDurationServiceImplUnitTest {

    private static final BigDecimal BARGE_DIESEL_DISTANCE = BigDecimal.ONE;
    private static final BigDecimal RAIL_DIESEL_DISTANCE = BigDecimal.ONE;
    private static final BigDecimal RAIL_ELECTRICAL_DISTANCE = TEN;

    private MainRunDurationServiceImpl sut;

    private RoutePart routePart;
    private Terminal terminal;
    private Seaport seaport;
    private MainRunConnection mainRunConnection;

    @Before
    public void setUp() {

        sut = new MainRunDurationServiceImpl();

        routePart = new RoutePart();
        terminal = new Terminal();
        seaport = new Seaport();
        mainRunConnection = new MainRunConnection();
        mainRunConnection.setBargeDieselDistance(BARGE_DIESEL_DISTANCE);
        mainRunConnection.setRailDieselDistance(RAIL_DIESEL_DISTANCE);
        mainRunConnection.setRailElectricDistance(RAIL_ELECTRICAL_DISTANCE);
    }


    @Test
    public void getMainRunRoutePartDurationBargeUpStream() {

        routePart.setOrigin(seaport);
        routePart.setDestination(terminal);
        routePart.setRouteType(BARGE);

        BigDecimal duration = sut.getMainRunRoutePartDuration(mainRunConnection, routePart);

        assertThat(duration, closeTo(new BigDecimal("72"), ZERO));
    }


    @Test
    public void getMainRunRoutePartDurationBargeDownStream() {

        routePart.setOrigin(terminal);
        routePart.setDestination(seaport);
        routePart.setRouteType(BARGE);
        mainRunConnection.setRailDieselDistance(BigDecimal.ONE);
        mainRunConnection.setRailElectricDistance(TEN);

        BigDecimal duration = sut.getMainRunRoutePartDuration(mainRunConnection, routePart);

        assertThat(duration, closeTo(new BigDecimal("40.00"), ZERO));
    }


    @Test
    public void getMainRunRoutePartDurationRail() {

        routePart.setOrigin(terminal);
        routePart.setDestination(seaport);
        routePart.setRouteType(RouteType.RAIL);
        mainRunConnection.setRailDieselDistance(BigDecimal.ONE);
        mainRunConnection.setRailElectricDistance(TEN);

        BigDecimal duration = sut.getMainRunRoutePartDuration(mainRunConnection, routePart);

        assertThat(duration, closeTo(new BigDecimal("16.00"), ZERO));
    }


    @Test
    public void getMainRunRoutePartDurationTruck() {

        routePart.setOrigin(terminal);
        routePart.setDestination(seaport);
        routePart.setRouteType(RouteType.TRUCK);

        BigDecimal duration = sut.getMainRunRoutePartDuration(mainRunConnection, routePart);

        assertThat(duration, is(ZERO));
    }
}
