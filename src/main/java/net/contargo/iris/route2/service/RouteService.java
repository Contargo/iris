package net.contargo.iris.route2.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route2.ModeOfTransport;
import net.contargo.iris.route2.RoutePartEdgeResultStatus;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.routing.RoutingQueryStrategy;
import net.contargo.iris.routing.RoutingQueryStrategyProvider;

import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import static net.contargo.iris.route2.RoutePartEdgeResultStatus.NO_ROUTE;
import static net.contargo.iris.route2.RoutePartEdgeResultStatus.OK;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class RouteService {

    private final RoutingQueryStrategyProvider routingQueryStrategyProvider;

    public RouteService(RoutingQueryStrategyProvider routingQueryStrategyProvider) {

        this.routingQueryStrategyProvider = routingQueryStrategyProvider;
    }

    @Cacheable("RouteServiceRoute")
    public RoutePartEdgeResult route(GeoLocation start, GeoLocation end, ModeOfTransport mot) {

        RoutingQueryStrategy strategy = routingQueryStrategyProvider.strategy();
        RoutingQueryResult queryResult = strategy.route(start, end, mot);

        return toRouteResult(queryResult);
    }


    private RoutePartEdgeResult toRouteResult(RoutingQueryResult queryResult) {

        List<String> geometries = queryResult.getGeometries();
        double totalDistance = queryResult.getTotalDistance();
        double duration = queryResult.getTotalTime();

        RoutePartEdgeResultStatus status = null;

        if (queryResult.getStatus() == 200) {
            status = OK;
        } else if (queryResult.getStatus() == 207) {
            status = NO_ROUTE;
        }

        return new RoutePartEdgeResult(totalDistance, duration, geometries, status);
    }
}
