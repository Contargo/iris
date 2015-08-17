package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.builder.RouteBuilder;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Test;

import java.math.BigDecimal;

import java.util.List;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.container.ContainerType.THIRTY;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.BARGE_RAIL;
import static net.contargo.iris.route.RouteType.RAIL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link RouteBuilder}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class RouteBuilderUnitTest {

    private RouteBuilder sut;

    @Test
    public void testNewRoute() {

        sut = new RouteBuilder(new GeoLocation(new BigDecimal(42.0), new BigDecimal(8.3)), ContainerType.FORTY, FULL);

        assertThat(sut.getRoute(), notNullValue());
    }


    @Test
    public void testSettingRouteAttributes() {

        GeoLocation loc = new GeoLocation(new BigDecimal(42.0), new BigDecimal(8.3));
        GeoLocation otherLoc = new GeoLocation(new BigDecimal(42.01), new BigDecimal(8.19));

        sut = new RouteBuilder(loc, ContainerType.FORTY, FULL);
        sut.goTo(otherLoc, RouteType.TRUCK);

        Route route = sut.getRoute();
        assertThat(route.getData().getParts(), hasSize(1));

        RoutePart part = route.getData().getParts().get(0);
        assertThat(part.getContainerType(), is(ContainerType.FORTY));
        assertThat(part.getContainerState(), is(FULL));
        assertThat(part.getOrigin(), is(loc));
        assertThat(part.getDestination(), is(otherLoc));
    }


    @Test
    public void testUnloadContainer() {

        GeoLocation one = new GeoLocation(new BigDecimal(42.0), new BigDecimal(8.3));
        GeoLocation two = new GeoLocation(new BigDecimal(42.01), new BigDecimal(8.19));
        GeoLocation three = new GeoLocation(new BigDecimal(40.87), new BigDecimal(7.9));

        sut = new RouteBuilder(one, ContainerType.FORTY, FULL);
        sut.goTo(two, RouteType.BARGE);
        sut.unloadContainer();
        sut.goTo(three, RouteType.TRUCK);

        Route route = sut.getRoute();
        assertThat(route.getData().getParts(), hasSize(2));

        // show that route is from location one to location two and container is full
        RoutePart part = route.getData().getParts().get(0);
        assertThat(part.getContainerType(), is(ContainerType.FORTY));
        assertThat(part.getContainerState(), is(FULL));

        assertThat(part.getOrigin(), is(one));
        assertThat(part.getDestination(), is(two));

        // show that route is from location two to location three and container is empty
        RoutePart part2 = route.getData().getParts().get(1);
        assertThat(part2.getContainerType(), is(ContainerType.FORTY));
        assertThat(part2.getContainerState(), is(EMPTY));
        assertThat(part2.getOrigin(), is(two));
        assertThat(part2.getDestination(), is(three));
    }


    @Test
    public void goToSeaportViaSubConnections() {

        Terminal terminal = new Terminal(new GeoLocation(TEN, TEN));
        Terminal terminal2 = new Terminal(new GeoLocation(ZERO, ZERO));
        Seaport seaport = new Seaport(new GeoLocation(ONE, ONE));

        SeaportSubConnection subConnection1 = new SeaportSubConnection();
        subConnection1.setSeaport(seaport);
        subConnection1.setTerminal(terminal2);

        TerminalSubConnection subConnection2 = new TerminalSubConnection();
        subConnection2.setTerminal(terminal2);
        subConnection2.setTerminal2(terminal);

        List<AbstractSubConnection> subconnections = asList(subConnection1, subConnection2);

        sut = new RouteBuilder(terminal, THIRTY, FULL);

        sut.goToSeaportViaSubConnections(seaport, subconnections);

        Route route = sut.getRoute();

        assertThat(route.getData().getParts(), hasSize(1));
        assertThat(route.getData().getParts().get(0).getRouteType(), is(BARGE_RAIL));
        assertThat(route.getData().getParts().get(0).getContainerType(), is(THIRTY));
        assertThat(route.getData().getParts().get(0).getContainerState(), is(FULL));
        assertThat(route.getData().getParts().get(0).getOrigin(), is(terminal));
        assertThat(route.getData().getParts().get(0).getDestination(), is(seaport));
        assertThat(route.getData().getParts().get(0).getSubRouteParts(), hasSize(2));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(0).getOrigin(), is(terminal));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(0).getDestination(), is(terminal2));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(0).getRouteType(), is(RAIL));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(1).getOrigin(), is(terminal2));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(1).getDestination(), is(seaport));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(1).getRouteType(), is(BARGE));
    }


    @Test
    public void goToTerminalViaSubConnections() {

        Terminal terminal = new Terminal(new GeoLocation(TEN, TEN));
        Terminal terminal2 = new Terminal(new GeoLocation(ZERO, ZERO));
        Seaport seaport = new Seaport(new GeoLocation(ONE, ONE));

        SeaportSubConnection subConnection1 = new SeaportSubConnection();
        subConnection1.setSeaport(seaport);
        subConnection1.setTerminal(terminal2);

        TerminalSubConnection subConnection2 = new TerminalSubConnection();
        subConnection2.setTerminal(terminal2);
        subConnection2.setTerminal2(terminal);

        List<AbstractSubConnection> subconnections = asList(subConnection1, subConnection2);

        sut = new RouteBuilder(seaport, THIRTY, FULL);

        sut.goToTerminalViaSubConnections(terminal, subconnections);

        Route route = sut.getRoute();

        assertThat(route.getData().getParts(), hasSize(1));
        assertThat(route.getData().getParts().get(0).getRouteType(), is(BARGE_RAIL));
        assertThat(route.getData().getParts().get(0).getContainerType(), is(THIRTY));
        assertThat(route.getData().getParts().get(0).getContainerState(), is(FULL));
        assertThat(route.getData().getParts().get(0).getOrigin(), is(seaport));
        assertThat(route.getData().getParts().get(0).getDestination(), is(terminal));
        assertThat(route.getData().getParts().get(0).getSubRouteParts(), hasSize(2));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(0).getOrigin(), is(seaport));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(0).getDestination(), is(terminal2));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(0).getRouteType(), is(BARGE));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(1).getOrigin(), is(terminal2));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(1).getDestination(), is(terminal));
        assertThat(route.getData().getParts().get(0).getSubRouteParts().get(1).getRouteType(), is(RAIL));
    }
}
