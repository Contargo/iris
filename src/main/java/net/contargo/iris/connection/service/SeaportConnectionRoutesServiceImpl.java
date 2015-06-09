package net.contargo.iris.connection.service;

import net.contargo.iris.connection.advice.MainRunAdvisor;
import net.contargo.iris.connection.advice.MainRunStrategy;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

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

    Route getMainRunRoute(Seaport seaPort, Terminal terminal, RouteInformation routeInformation, RouteType routeType) {

        if (routeType != RouteType.BARGE && routeType != RouteType.RAIL && routeType != RouteType.BARGE_RAIL) {
            throw new IllegalArgumentException();
        }

        MainRunStrategy mainRunStrategy = mainRunAdvisor.advice(routeInformation.getProduct(),
                routeInformation.getRouteDirection());

        return mainRunStrategy.getRoute(seaPort, routeInformation.getDestination(), terminal,
                routeInformation.getContainerType(), routeType);
    }


    @Override
    public List<Route> getAvailableSeaportConnectionRoutes(Seaport origin, RouteInformation routeInformation) {

        List<Route> routes = new ArrayList<>();

        // a RouteCombo has one ore more (in case RouteCombo.ALL) RouteTypes
        for (RouteType routeType : routeInformation.getRouteCombo().getRouteTypes()) {
            // get all terminals that have routes with the given route type to the given seaport
            List<Terminal> terminals = seaportTerminalConnectionService.getTerminalsConnectedToSeaPortByRouteType(
                    origin, routeType);

            // get route for each location and add it to route list
            for (Terminal terminal : terminals) {
                routes.add(getMainRunRoute(origin, terminal, routeInformation, routeType));
            }
        }

        return routes;
    }
}
