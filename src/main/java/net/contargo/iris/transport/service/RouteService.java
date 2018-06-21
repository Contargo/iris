package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.routing.RoutingQueryStrategy;
import net.contargo.iris.routing.RoutingQueryStrategyProvider;
import net.contargo.iris.transport.api.ModeOfTransport;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import java.util.List;

import static net.contargo.iris.transport.service.RouteStatus.NO_ROUTE;
import static net.contargo.iris.transport.service.RouteStatus.OK;

import static java.math.RoundingMode.UP;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class RouteService {

    private static final BigDecimal METER_PER_KILOMETER = new BigDecimal("1000");
    private static final BigDecimal SECONDS_PER_MINUTES = new BigDecimal("60");

    private final RoutingQueryStrategyProvider routingQueryStrategyProvider;

    public RouteService(RoutingQueryStrategyProvider routingQueryStrategyProvider) {

        this.routingQueryStrategyProvider = routingQueryStrategyProvider;
    }

    @Cacheable("RouteServiceRoute")
    public RouteResult route(GeoLocation start, GeoLocation end, ModeOfTransport mot) {

        RoutingQueryStrategy strategy = routingQueryStrategyProvider.strategy();
        RoutingQueryResult queryResult = strategy.route(start, end, mot.getOsrmProfile());

        return toRouteResult(queryResult);
    }


    private static RouteResult toRouteResult(RoutingQueryResult queryResult) {

        List<String> geometries = queryResult.getGeometries();
        BigDecimal distanceInMeter = BigDecimal.valueOf(queryResult.getTotalDistance());

        Integer totalDistance = distanceInMeter.divide(METER_PER_KILOMETER, 0, UP).intValue();

        Integer toll = queryResult.getToll().setScale(0, UP).intValue();

        Integer duration = BigDecimal.valueOf(queryResult.getTotalTime())
                .divide(SECONDS_PER_MINUTES, 0, UP)
                .intValue();

        RouteStatus status = null;

        if (queryResult.getStatus() == HttpStatus.OK.value()) {
            status = OK;
        } else if (queryResult.getStatus() == HttpStatus.MULTI_STATUS.value()) {
            status = NO_ROUTE;
        }

        return new RouteResult(totalDistance, toll, duration, geometries, status);
    }
}
