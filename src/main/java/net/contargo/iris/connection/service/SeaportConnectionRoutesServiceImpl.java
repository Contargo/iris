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


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportConnectionRoutesServiceImpl implements SeaportConnectionRoutesService {

    private final SeaportTerminalConnectionService seaportTerminalConnectionService;
    private final MainRunAdvisor mainRunAdvisor;

    public SeaportConnectionRoutesServiceImpl(SeaportTerminalConnectionService seaportTerminalConnectionService,
        MainRunAdvisor mainRunAdvisor) {

        this.seaportTerminalConnectionService = seaportTerminalConnectionService;
        this.mainRunAdvisor = mainRunAdvisor;
    }

    Route getMainRunRoute(MainRunConnection connection, RouteInformation routeInformation, RouteType routeType) {

        if (routeType != RouteType.BARGE && routeType != RouteType.RAIL && routeType != RouteType.BARGE_RAIL) {
            throw new IllegalArgumentException();
        }

        MainRunStrategy mainRunStrategy = mainRunAdvisor.advice(routeInformation.getProduct(),
                routeInformation.getRouteDirection());

        return mainRunStrategy.getRoute(connection, routeInformation.getDestination(),
                routeInformation.getContainerType(), routeType);
    }


    @Override
    public List<Route> getAvailableSeaportConnectionRoutes(Seaport origin, RouteInformation routeInformation) {

        List<Route> routes = new ArrayList<>();

        // a RouteCombo has one ore more (in case RouteCombo.ALL) RouteTypes
        for (RouteType routeType : routeInformation.getRouteCombo().getRouteTypes()) {
            List<MainRunConnection> connections = seaportTerminalConnectionService.getConnectionsToSeaPortByRouteType(
                    origin, routeType);

            for (MainRunConnection connection : connections) {
                routes.add(getMainRunRoute(connection, routeInformation, routeType));
            }
        }

        return routes;
    }
}
