package net.contargo.iris.connection.advice;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import java.util.Locale;

import static net.contargo.iris.route.RouteType.RAIL;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
public class MainRunRoundTripImportStrategyUnitTest {

    @Before
    public void before() {

        Locale.setDefault(Locale.GERMANY);
    }


    @Test
    public void testGetRoute() {

        Seaport seaPort = new Seaport();
        seaPort.setName("seaPort");

        GeoLocation destination = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);

        Terminal terminal = new Terminal();
        terminal.setName("term");

        MainRunConnection connection = new MainRunConnection(seaPort);
        connection.setTerminal(terminal);
        connection.setRouteType(RAIL);

        MainRunRoundTripImportStrategy sut = new MainRunRoundTripImportStrategy();
        Route route = sut.getRoute(connection, destination, ContainerType.TWENTY_LIGHT);

        assertThat(route.getName(), is("seaPort -> term -> 1,0:1,0 -> term -> seaPort"));
    }
}
