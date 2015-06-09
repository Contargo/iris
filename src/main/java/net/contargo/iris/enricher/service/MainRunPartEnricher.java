package net.contargo.iris.enricher.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.distance.service.ConnectionDistanceService;
import net.contargo.iris.mainrun.service.MainRunDurationService;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.route.RouteType;


/**
 * Enricher to set information about distance and duration on a main run {@link RoutePart}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
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

        if (routePart.isOfType(RouteType.BARGE) || routePart.isOfType(RouteType.RAIL)) {
            RoutePartData routePartData = routePart.getData();

            MainRunConnection mainRunConnection;

            try {
                mainRunConnection = mainRunConnectionService.findRoutingConnectionBetweenTerminalAndSeaportByType(
                        routePart.findTerminal(), routePart.findSeaport(), routePart.getRouteType());

                if (mainRunConnection == null) {
                    throw new IllegalStateException("mainRunConnection may not be null");
                }
            } catch (IllegalStateException e) {
                throw new CriticalEnricherException(
                    "Enriching not possible: missing terminal or seaport to determine main run connection", e);
            }

            routePartData.setDistance(connectionDistanceService.getDistance(mainRunConnection));
            routePartData.setDieselDistance(connectionDistanceService.getDieselDistance(mainRunConnection));
            routePartData.setElectricDistance(connectionDistanceService.getElectricDistance(mainRunConnection));
            routePartData.setRailDieselDistance(connectionDistanceService.getRailDieselDistance(mainRunConnection));
            routePartData.setBargeDieselDistance(connectionDistanceService.getBargeDieselDistance(mainRunConnection));
            routePartData.setDuration(mainRunDurationService.getMainRunRoutePartDuration(mainRunConnection, routePart));
        }
    }
}
