package net.contargo.iris.connection.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.advice.MainRunAdvisor;
import net.contargo.iris.connection.advice.MainRunStrategy;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.contargo.iris.route.RouteCombo.ALL;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.DTRUCK;
import static net.contargo.iris.route.RouteType.RAIL;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public class SeaportConnectionRoutesServiceImpl implements SeaportConnectionRoutesService {

    private final SeaportTerminalConnectionService seaportTerminalConnectionService;
    private final MainRunAdvisor mainRunAdvisor;
    private final boolean dtruckActive;

    public SeaportConnectionRoutesServiceImpl(SeaportTerminalConnectionService seaportTerminalConnectionService,
        MainRunAdvisor mainRunAdvisor, boolean dtruckActive) {

        this.seaportTerminalConnectionService = seaportTerminalConnectionService;
        this.mainRunAdvisor = mainRunAdvisor;
        this.dtruckActive = dtruckActive;
    }

    Route getMainRunRoute(MainRunConnection connection, RouteInformation routeInformation, RouteType routeType) {

        if (routeType != BARGE && routeType != RAIL && routeType != DTRUCK) {
            throw new IllegalArgumentException();
        }

        MainRunStrategy mainRunStrategy = mainRunAdvisor.advice(routeInformation.getProduct(),
                routeInformation.getRouteDirection());

        return mainRunStrategy.getRoute(connection, routeInformation.getDestination(),
                routeInformation.getContainerType());
    }


    @Override
    public List<Route> getAvailableSeaportConnectionRoutes(Seaport origin, RouteInformation routeInformation) {

        List<Route> routes = new ArrayList<>();

        // a RouteCombo has one ore more (in case RouteCombo.ALL) RouteTypes
        List<RouteType> routeTypes = new ArrayList<>(asList(routeInformation.getRouteCombo().getRouteTypes()));

        if (routeInformation.getRouteCombo() == ALL) {
            routeTypes.add(DTRUCK);
        }

        for (RouteType routeType : routeTypes) {
            List<MainRunConnection> connections = emptyList();

            if (routeType == DTRUCK) {
                if (dtruckActive) {
                    connections = seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(origin,
                            routeType);
                }
            } else {
                connections = seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(origin, routeType);
            }

            routes.addAll(connections.stream()
                .filter(MainRunConnection::getEverythingEnabled)
                .map(connection -> getMainRunRoute(connection, routeInformation, routeType))
                .collect(Collectors.toList()));
        }

        return routes;
    }
}
