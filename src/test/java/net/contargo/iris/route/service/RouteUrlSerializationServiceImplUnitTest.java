package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


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
}
