package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;

import java.util.Arrays;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public final class RouteCreationUtil {

    public static final String ANTWERP = "Antwerp";
    public static final String BASEL = "Basel";

    private RouteCreationUtil() {
    }

    public static Route createOneWayImportRoute() {

        GeoLocation loc = new GeoLocation(new BigDecimal("49.0"), new BigDecimal("8.41"));

        Terminal terminal = new Terminal();
        terminal.setId(3L);
        terminal.setLatitude(new BigDecimal(49.06846));
        terminal.setLongitude(new BigDecimal(8.28070));

        terminal.setName(BASEL);

        Seaport seaPort = new Seaport();
        seaPort.setLatitude(new BigDecimal(51.36833));
        seaPort.setLongitude(new BigDecimal(4.3));
        seaPort.setName(ANTWERP);

        RoutePart part1 = new RoutePart();
        part1.setContainerType(ContainerType.THIRTY);
        part1.setContainerState(ContainerState.FULL);
        part1.setOrigin(seaPort);
        part1.setRouteType(RouteType.BARGE);
        part1.setDestination(terminal);

        RoutePart part2 = new RoutePart();
        part2.setContainerType(ContainerType.THIRTY);
        part2.setContainerState(ContainerState.FULL);
        part2.setOrigin(terminal);
        part2.setRouteType(RouteType.TRUCK);
        part2.setDestination(loc);

        RoutePartData routePartData = new RoutePartData();
        routePartData.setAirLineDistance(new BigDecimal("0"));
        part2.setData(routePartData);

        RoutePart part3 = new RoutePart();
        part3.setContainerType(ContainerType.THIRTY);
        part3.setContainerState(ContainerState.EMPTY);
        part3.setOrigin(loc);
        part3.setRouteType(RouteType.TRUCK);
        part3.setDestination(terminal);

        routePartData = new RoutePartData();
        routePartData.setAirLineDistance(new BigDecimal("50.00"));
        part3.setData(routePartData);

        Route route = new Route();
        route.getData().setParts(Arrays.asList(part1, part2, part3));

        return addDistances(route);
    }


    private static Route addDistances(Route route) {

        int distance = 10;

        for (RoutePart p : route.getData().getParts()) {
            p.getData().setDistance(new BigDecimal(distance++));
        }

        return route;
    }


    public static Route createRoundTripImportRoute() {

        GeoLocation loc = new GeoLocation(new BigDecimal(49.0), new BigDecimal(8.41));

        Terminal terminal = new Terminal();
        terminal.setId(3L);
        terminal.setLatitude(new BigDecimal(49.06846));
        terminal.setLongitude(new BigDecimal(8.28070));
        terminal.setName(BASEL);

        Seaport seaPort = new Seaport();
        seaPort.setLatitude(new BigDecimal(51.36833));
        seaPort.setLongitude(new BigDecimal(4.3));
        seaPort.setName(ANTWERP);

        RoutePart part1 = new RoutePart();
        part1.setContainerType(ContainerType.FORTY);
        part1.setContainerState(ContainerState.FULL);
        part1.setOrigin(seaPort);
        part1.setRouteType(RouteType.BARGE);
        part1.setDestination(terminal);

        RoutePart part2 = new RoutePart();
        part2.setContainerType(ContainerType.FORTY);
        part2.setContainerState(ContainerState.FULL);
        part2.setOrigin(terminal);
        part2.setRouteType(RouteType.TRUCK);
        part2.setDestination(loc);

        RoutePart part3 = new RoutePart();
        part3.setContainerType(ContainerType.FORTY);
        part3.setContainerState(ContainerState.EMPTY);
        part3.setOrigin(loc);
        part3.setRouteType(RouteType.TRUCK);
        part3.setDestination(terminal);

        RoutePart part4 = new RoutePart();
        part4.setContainerType(ContainerType.FORTY);
        part4.setContainerState(ContainerState.EMPTY);
        part4.setOrigin(terminal);
        part4.setRouteType(RouteType.BARGE);
        part4.setDestination(seaPort);

        Route route = new Route();
        route.getData().setParts(Arrays.asList(part1, part2, part3, part4));

        return addDistances(route);
    }
}
