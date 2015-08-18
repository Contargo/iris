package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link RouteUrlSerializationServiceImpl}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteUrlSerializationServiceImplUnitTest {

    private RouteUrlSerializationServiceImpl sut;

    @Before
    public void setup() {

        sut = new RouteUrlSerializationServiceImpl();
    }


    @Test
    public void testSerializeRouteUrl() {

        // set up route object to "serialize"

        Route route = new Route();

        RoutePart truckRoutePart = new RoutePart();
        truckRoutePart.setRouteType(RouteType.TRUCK);
        truckRoutePart.setOrigin(new GeoLocation(new BigDecimal(8.42), new BigDecimal(49.015)));
        truckRoutePart.setDestination(new GeoLocation(new BigDecimal(9.42), new BigDecimal(59.015)));
        truckRoutePart.setContainerType(ContainerType.TWENTY_LIGHT);
        truckRoutePart.setContainerState(ContainerState.FULL);

        route.getData().getParts().add(truckRoutePart);

        RoutePart bargeRoutePart = new RoutePart();
        bargeRoutePart.setRouteType(RouteType.BARGE);
        bargeRoutePart.setOrigin(new GeoLocation(new BigDecimal(9.42), new BigDecimal(59.015)));
        bargeRoutePart.setDestination(new GeoLocation(new BigDecimal(8.42), new BigDecimal(49.015)));
        bargeRoutePart.setContainerType(ContainerType.TWENTY_LIGHT);
        bargeRoutePart.setContainerState(ContainerState.EMPTY);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(BigInteger.ONE);
        route.setResponsibleTerminal(terminal);

        route.getData().getParts().add(bargeRoutePart);

        // run test
        RouteDto routeDto = new RouteDto(route);
        sut.serializeUrl(routeDto, "/routedetails", "/routepartdetails");

        // assert that results are correct
        assertThat("/routedetails?terminal=1&data.parts[0].origin.longitude=49.015"
            + "&data.parts[0].origin.latitude=8.42"
            + "&data.parts[0].destination.longitude=59.015"
            + "&data.parts[0].destination.latitude=9.42"
            + "&data.parts[0].routeType=TRUCK"
            + "&data.parts[0].containerType=TWENTY_LIGHT"
            + "&data.parts[0].containerState=FULL"
            + "&data.parts[1].origin.longitude=59.015"
            + "&data.parts[1].origin.latitude=9.42"
            + "&data.parts[1].destination.longitude=49.015"
            + "&data.parts[1].destination.latitude=8.42"
            + "&data.parts[1].routeType=BARGE"
            + "&data.parts[1].containerType=TWENTY_LIGHT"
            + "&data.parts[1].containerState=EMPTY", is(routeDto.getUrl()));
    }


    @Test
    public void serializeRouteUrlWithSubRouteParts() {

        // set up route object to "serialize"

        Route route = new Route();

        RoutePart truckRoutePart = new RoutePart();
        truckRoutePart.setRouteType(RouteType.TRUCK);
        truckRoutePart.setOrigin(new GeoLocation(new BigDecimal(8.42), new BigDecimal(49.015)));
        truckRoutePart.setDestination(new GeoLocation(new BigDecimal(9.42), new BigDecimal(59.015)));
        truckRoutePart.setContainerType(ContainerType.TWENTY_LIGHT);
        truckRoutePart.setContainerState(ContainerState.FULL);

        route.getData().getParts().add(truckRoutePart);

        RoutePart bargeRailRoutePart = new RoutePart();
        bargeRailRoutePart.setRouteType(RouteType.BARGE_RAIL);
        bargeRailRoutePart.setOrigin(new GeoLocation(new BigDecimal(9.42), new BigDecimal(59.015)));
        bargeRailRoutePart.setDestination(new GeoLocation(new BigDecimal(8.42), new BigDecimal(49.015)));
        bargeRailRoutePart.setContainerType(ContainerType.TWENTY_LIGHT);
        bargeRailRoutePart.setContainerState(ContainerState.EMPTY);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(BigInteger.ONE);
        route.setResponsibleTerminal(terminal);

        bargeRailRoutePart.setSubRouteParts(createSubRouteParts());

        route.getData().getParts().add(bargeRailRoutePart);

        // run test
        RouteDto routeDto = new RouteDto(route);
        sut.serializeUrl(routeDto, "/routedetails", "/routepartdetails");

        // assert that results are correct
        assertThat(routeDto.getUrl(),
            is("/routedetails?terminal=1&data.parts[0].origin.longitude=49.015"
                + "&data.parts[0].origin.latitude=8.42"
                + "&data.parts[0].destination.longitude=59.015"
                + "&data.parts[0].destination.latitude=9.42"
                + "&data.parts[0].routeType=TRUCK"
                + "&data.parts[0].containerType=TWENTY_LIGHT"
                + "&data.parts[0].containerState=FULL"
                + "&data.parts[1].origin.longitude=59.015"
                + "&data.parts[1].origin.latitude=9.42"
                + "&data.parts[1].destination.longitude=49.015"
                + "&data.parts[1].destination.latitude=8.42"
                + "&data.parts[1].routeType=BARGE_RAIL"
                + "&data.parts[1].containerType=TWENTY_LIGHT"
                + "&data.parts[1].containerState=EMPTY"
                + "&data.parts[1].subRouteParts[0].origin.longitude=1.0000000000"
                + "&data.parts[1].subRouteParts[0].origin.latitude=1.0000000000"
                + "&data.parts[1].subRouteParts[0].destination.longitude=10.0000000000"
                + "&data.parts[1].subRouteParts[0].destination.latitude=1.0000000000"
                + "&data.parts[1].subRouteParts[0].routeType=RAIL"
                + "&data.parts[1].subRouteParts[1].origin.longitude=10.0000000000"
                + "&data.parts[1].subRouteParts[1].origin.latitude=1.0000000000"
                + "&data.parts[1].subRouteParts[1].destination.longitude=1.0000000000"
                + "&data.parts[1].subRouteParts[1].destination.latitude=10.0000000000"
                + "&data.parts[1].subRouteParts[1].routeType=RAIL"));
    }


    private List<SubRoutePart> createSubRouteParts() {

        Terminal terminal1 = new Terminal(new GeoLocation(BigDecimal.ONE, BigDecimal.ONE));
        terminal1.setUniqueId(BigInteger.TEN);

        Terminal terminal2 = new Terminal(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        terminal2.setUniqueId(BigInteger.ONE);

        Seaport seaport = new Seaport(new GeoLocation(BigDecimal.TEN, BigDecimal.ONE));
        seaport.setUniqueId(BigInteger.ZERO);

        SubRoutePart subRoutePart1 = new SubRoutePart();
        subRoutePart1.setOrigin(terminal1);
        subRoutePart1.setDestination(terminal2);
        subRoutePart1.setRouteType(RouteType.RAIL);

        SubRoutePart subRoutePart2 = new SubRoutePart();
        subRoutePart2.setOrigin(terminal2);
        subRoutePart2.setDestination(seaport);
        subRoutePart2.setRouteType(RouteType.RAIL);

        return asList(subRoutePart1, subRoutePart2);
    }
}
