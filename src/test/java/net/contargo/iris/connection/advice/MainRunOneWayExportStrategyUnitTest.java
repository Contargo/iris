package net.contargo.iris.connection.advice;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.route.Route;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static net.contargo.iris.container.ContainerType.TWENTY_LIGHT;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.BARGE_RAIL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;


/**
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
public class MainRunOneWayExportStrategyUnitTest {

    @Before
    public void before() {

        Locale.setDefault(Locale.GERMANY);
    }


    @Test
    public void testGetRoute() {

        Seaport seaPort = new Seaport();
        seaPort.setName("seaPort");

        GeoLocation destination = new GeoLocation(ONE, ONE);

        Terminal terminal = new Terminal();
        terminal.setName("term");

        MainRunConnection connection = new MainRunConnection(seaPort);
        connection.setTerminal(terminal);
        connection.setRouteType(BARGE);

        MainRunOneWayExportStrategy sut = new MainRunOneWayExportStrategy();
        Route route = sut.getRoute(connection, destination, TWENTY_LIGHT);

        assertThat(route.getName(), is("term -> 1,0:1,0 -> term -> seaPort"));
    }


    @Test
    public void getBargeRailRoute() {

        Terminal terminal = new Terminal(new GeoLocation(TEN, TEN));
        terminal.setName("term");

        Terminal terminal2 = new Terminal(new GeoLocation(ZERO, ZERO));
        Seaport seaport = new Seaport(new GeoLocation(ONE, ONE));
        seaport.setName("seaPort");

        SeaportSubConnection subConnection1 = new SeaportSubConnection();
        subConnection1.setSeaport(seaport);
        subConnection1.setTerminal(terminal2);

        TerminalSubConnection subConnection2 = new TerminalSubConnection();
        subConnection2.setTerminal(terminal2);
        subConnection2.setTerminal2(terminal);

        GeoLocation destination = new GeoLocation(ONE, ONE);

        MainRunConnection connection = new MainRunConnection(seaport);
        connection.setTerminal(terminal);
        connection.setRouteType(BARGE_RAIL);
        connection.setSubConnections(asList(subConnection1, subConnection2));

        MainRunOneWayExportStrategy sut = new MainRunOneWayExportStrategy();
        Route route = sut.getRoute(connection, destination, TWENTY_LIGHT);

        assertThat(route.getName(), is("term -> 1,0:1,0 -> term -> seaPort"));
        assertThat(route.getData().getParts(), hasSize(3));
        assertThat(route.getData().getParts().get(0).getSubRouteParts(), hasSize(0));
        assertThat(route.getData().getParts().get(1).getSubRouteParts(), hasSize(0));
        assertThat(route.getData().getParts().get(2).getSubRouteParts(), hasSize(2));
    }
}
