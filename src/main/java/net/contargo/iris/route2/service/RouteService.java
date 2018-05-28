package net.contargo.iris.route2.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route2.ModeOfTransport;
import net.contargo.iris.route2.RoutePartEdgeResultStatus;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.routing.RoutingQueryStrategy;
import net.contargo.iris.routing.RoutingQueryStrategyProvider;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import java.util.List;

import static net.contargo.iris.route2.RoutePartEdgeResultStatus.NO_ROUTE;
import static net.contargo.iris.route2.RoutePartEdgeResultStatus.OK;

import static java.math.RoundingMode.UP;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class RouteService {

    private static final BigDecimal METER_PER_KILOMETER = new BigDecimal("1000");
    private static final int SCALE = 2;
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


    private static RoutePartEdgeResult toRouteResult(RoutingQueryResult queryResult) {

        List<String> geometries = queryResult.getGeometries();
        BigDecimal distanceInMeter = BigDecimal.valueOf(queryResult.getTotalDistance());

        BigDecimal totalDistance = distanceInMeter.divide(METER_PER_KILOMETER, SCALE, UP);

        BigDecimal toll = queryResult.getToll();

        BigDecimal duration = BigDecimal.valueOf(queryResult.getTotalTime());

        RoutePartEdgeResultStatus status = null;

        if (queryResult.getStatus() == HttpStatus.OK.value()) {
            status = OK;
        } else if (queryResult.getStatus() == HttpStatus.MULTI_STATUS.value()) {
            status = NO_ROUTE;
        }

        return new RoutePartEdgeResult(totalDistance, toll, duration, geometries, status);
    }
}
