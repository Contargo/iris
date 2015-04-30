package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Test;

import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Mockito.when;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class RoutePartUnitTest {

    private RoutePart sut;

    @Test
    public void calculatesUpstream() {

        sut = new RoutePart(new Seaport(), new Terminal(), RouteType.BARGE);
        assertThat(sut.getDirection(), is(RoutePart.Direction.UPSTREAM));
    }


    @Test
    public void calculatesDownstream() {

        sut = new RoutePart(new Terminal(), new Seaport(), RouteType.BARGE);
        assertThat(sut.getDirection(), is(RoutePart.Direction.DOWNSTREAM));
    }


    @Test
    public void calculatesNoStream() {

        sut = new RoutePart(new GeoLocation(), new Terminal(), RouteType.TRUCK);
        assertThat(sut.getDirection(), is(RoutePart.Direction.NOT_SET));
    }


    @Test
    public void returnsCorrectName() {

        GeoLocation origin = Mockito.mock(GeoLocation.class);
        GeoLocation destination = Mockito.mock(GeoLocation.class);
        when(origin.getNiceName()).thenReturn("origin");
        when(destination.getNiceName()).thenReturn("destination");

        sut = new RoutePart();
        sut.setOrigin(origin);
        sut.setDestination(destination);
        assertThat(sut.getName(), is("origin -> destination"));
    }


    @Test
    public void isOfType() {

        sut = new RoutePart();
        assertThat(sut.isOfType(RouteType.BARGE), is(false));

        sut.setRouteType(null); // explicitly set to null
        assertThat(sut.isOfType(null), is(false));

        sut.setRouteType(null); // explicitly set to null
        assertThat(sut.isOfType(RouteType.BARGE), is(false));

        sut.setRouteType(RouteType.BARGE);
        assertThat(sut.isOfType(null), is(false));

        sut.setRouteType(RouteType.TRUCK);
        assertThat(sut.isOfType(RouteType.BARGE), is(false));

        sut.setRouteType(RouteType.BARGE);
        assertThat(sut.isOfType(RouteType.BARGE), is(true));
    }


    @Test
    public void hasTerminal() {

        sut = new RoutePart();

        sut.setOrigin(new Terminal());
        sut.setDestination(new Address());
        assertThat(sut.hasTerminal(), is(true));

        sut.setOrigin(new Address());
        sut.setDestination(new Terminal());
        assertThat(sut.hasTerminal(), is(true));

        sut.setOrigin(new Address());
        sut.setDestination(new Address());
        assertThat(sut.hasTerminal(), is(false));
    }


    @Test
    public void findTerminal() {

        Terminal terminal = new Terminal();

        sut = new RoutePart();
        sut.setOrigin(terminal);
        sut.setDestination(new Address());
        assertThat(sut.findTerminal(), is(terminal));

        sut.setOrigin(new Address());
        sut.setDestination(terminal);
        assertThat(sut.findTerminal(), is(terminal));
    }


    @Test(expected = IllegalStateException.class)
    public void findNoTerminal() {

        sut = new RoutePart();
        sut.setOrigin(new Address());
        sut.setDestination(new Address());
        sut.findTerminal();
    }


    @Test
    public void hasSeaport() {

        sut = new RoutePart();

        sut.setOrigin(new Seaport());
        sut.setDestination(new Address());
        assertThat(sut.hasSeaport(), is(true));

        sut.setOrigin(new Address());
        sut.setDestination(new Seaport());
        assertThat(sut.hasSeaport(), is(true));

        sut.setOrigin(new Address());
        sut.setDestination(new Address());
        assertThat(sut.hasSeaport(), is(false));
    }


    @Test
    public void findSeaport() {

        Seaport seaport = new Seaport();

        sut = new RoutePart();
        sut.setOrigin(seaport);
        sut.setDestination(new Address());
        assertThat(sut.findSeaport(), is(seaport));

        sut.setOrigin(new Address());
        sut.setDestination(seaport);
        assertThat(sut.findSeaport(), is(seaport));
    }


    @Test(expected = IllegalStateException.class)
    public void findNoSeaport() {

        sut = new RoutePart();
        sut.setOrigin(new Address());
        sut.setDestination(new Address());
        sut.findSeaport();
    }


    @Test
    public void copyWithoutData() {

        RoutePartData routePartData = new RoutePartData();
        routePartData.setAirLineDistance(BigDecimal.ONE);

        sut = new RoutePart(new Seaport(), new Terminal(), RouteType.BARGE);
        sut.setContainerState(ContainerState.FULL);
        sut.setContainerType(ContainerType.FORTY);
        sut.setData(routePartData);

        RoutePart actualRoutePart = sut.copyWithoutData();
        assertThat(actualRoutePart.getContainerState(), is(ContainerState.FULL));
        assertThat(actualRoutePart.getContainerType(), is(ContainerType.FORTY));

        RoutePartData actualRoutePartData = actualRoutePart.getData();
        assertThat(actualRoutePartData.getAirLineDistance(), nullValue());
        assertThat(actualRoutePartData.getDieselDistance(), nullValue());
        assertThat(actualRoutePartData.getElectricDistance(), nullValue());
        assertThat(actualRoutePartData.getTollDistance(), nullValue());
        assertThat(actualRoutePartData.getDuration(), nullValue());
    }
}
