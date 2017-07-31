package net.contargo.iris.route.service;

import com.google.common.collect.Lists;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.distance.service.ConnectionDistanceService;
import net.contargo.iris.mainrun.service.MainRunDurationService;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.route.SubRoutePart;

import java.util.List;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.BARGE_RAIL;
import static net.contargo.iris.route.RouteType.DTRUCK;
import static net.contargo.iris.route.RouteType.RAIL;


/**
 * Enricher to set information about distance and duration on a main run {@link RoutePart}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
class MainRunPartEnricher implements RoutePartEnricher {

    private final MainRunConnectionService mainRunConnectionService;
    private final MainRunDurationService mainRunDurationService;
    private final ConnectionDistanceService connectionDistanceService;

    MainRunPartEnricher(MainRunConnectionService mainRunConnectionService,
        MainRunDurationService mainRunDurationService, ConnectionDistanceService connectionDistanceService) {

        this.mainRunConnectionService = mainRunConnectionService;
        this.mainRunDurationService = mainRunDurationService;
        this.connectionDistanceService = connectionDistanceService;
    }

    /**
     * @see  RoutePartEnricher#enrich(net.contargo.iris.route.RoutePart, EnricherContext)
     */
    @Override
    public void enrich(RoutePart routePart, EnricherContext context) throws CriticalEnricherException {

        if (routePart.isOfType(BARGE) || routePart.isOfType(RAIL) || routePart.isOfType(BARGE_RAIL)
                || routePart.isOfType(DTRUCK)) {
            RoutePartData routePartData = routePart.getData();

            MainRunConnection mainRunConnection;

            try {
                mainRunConnection = mainRunConnectionService.findRoutingConnectionBetweenTerminalAndSeaportByType(
                        routePart.findTerminal(), routePart.findSeaport(), routePart.getRouteType(),
                        routePart.getSubRouteParts());

                if (mainRunConnection == null) {
                    throw new IllegalStateException("mainRunConnection may not be null");
                }
            } catch (IllegalStateException e) {
                throw new CriticalEnricherException(
                    "Enriching not possible: missing terminal or seaport to determine main run connection", e);
            }

            if (routePart.getRouteType() == BARGE_RAIL) {
                prepareSubRouteParts(routePart, mainRunConnection);
            }

            routePartData.setDistance(connectionDistanceService.getDistance(mainRunConnection));
            routePartData.setDieselDistance(connectionDistanceService.getDieselDistance(mainRunConnection));
            routePartData.setElectricDistance(connectionDistanceService.getElectricDistance(mainRunConnection));
            routePartData.setRailDieselDistance(connectionDistanceService.getRailDieselDistance(mainRunConnection));
            routePartData.setBargeDieselDistance(connectionDistanceService.getBargeDieselDistance(mainRunConnection));
            routePartData.setDtruckDistance(connectionDistanceService.getDtruckDistance(mainRunConnection));
            routePartData.setDuration(mainRunDurationService.getMainRunRoutePartDuration(mainRunConnection,
                    routePart));
        }
    }


    private void prepareSubRouteParts(RoutePart routePart, MainRunConnection mainRunConnection) {

        List<AbstractSubConnection> subConnectionList;

        if (routePart.getOrigin().equals(mainRunConnection.getSeaport())) {
            subConnectionList = mainRunConnection.getSubConnections();
        } else {
            subConnectionList = Lists.reverse(mainRunConnection.getSubConnections());
        }

        int i = 0;

        for (AbstractSubConnection subConnection : subConnectionList) {
            SubRoutePart subRoutePart = routePart.getSubRouteParts().get(i);
            subRoutePart.setBargeDieselDistance(connectionDistanceService.getBargeDieselDistance(subConnection));
            subRoutePart.setRailDieselDistance(connectionDistanceService.getRailDieselDistance(subConnection));
            subRoutePart.setElectricDistance(connectionDistanceService.getRailElectricDistance(subConnection));
            subRoutePart.setDieselDistance(connectionDistanceService.getDieselDistance(subConnection));
            subRoutePart.setDistance(connectionDistanceService.getDistance(subConnection));
            subRoutePart.setDuration(mainRunDurationService.getSubRoutePartDuration(subConnection, subRoutePart,
                    routePart.getDirection()));
            i++;
        }
    }
}
