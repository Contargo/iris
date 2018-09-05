package net.contargo.iris.connection.advice;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.Route;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static net.contargo.iris.container.ContainerType.TWENTY_LIGHT;
import static net.contargo.iris.route.RouteType.BARGE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.ONE;


/**
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
public class MainRunOneWayImportStrategyUnitTest {

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

        MainRunOneWayImportStrategy sut = new MainRunOneWayImportStrategy();
        Route route = sut.getRoute(connection, destination, TWENTY_LIGHT);

        assertThat(route.getName(), is("seaPort -> term -> 1,0:1,0 -> term"));
    }
}
