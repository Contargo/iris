package net.contargo.iris.route2.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.route2.ModeOfTransport;
import net.contargo.iris.route2.RoutePartEdgeResultStatus;
import net.contargo.iris.route2.api.RoutePartEdgeDto;
import net.contargo.iris.route2.api.RoutePartNodeDto;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.routing.RoutingQueryStrategy;
import net.contargo.iris.routing.RoutingQueryStrategyProvider;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;

import java.util.List;

import static net.contargo.iris.route2.RoutePartEdgeResultStatus.NO_ROUTE;
import static net.contargo.iris.route2.RoutePartEdgeResultStatus.OK;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@Service
public class RouteService {

    private final RoutingQueryStrategyProvider routingQueryStrategyProvider;
    private final TerminalService terminalService;
    private final SeaportService seaportService;
    private final AddressServiceWrapper addressServiceWrapper;

    public RouteService(RoutingQueryStrategyProvider routingQueryStrategyProvider, TerminalService terminalService,
        SeaportService seaportService, AddressServiceWrapper addressServiceWrapper) {

        this.routingQueryStrategyProvider = routingQueryStrategyProvider;
        this.terminalService = terminalService;
        this.seaportService = seaportService;
        this.addressServiceWrapper = addressServiceWrapper;
    }

    @Cacheable("RouteServiceRoute")
    public RoutePartEdgeResult route(RoutePartEdgeDto edge) {

        ModeOfTransport modeOfTransport = edge.getModeOfTransport();
        GeoLocation start = getGeoLocation(edge.getStart());
        GeoLocation end = getGeoLocation(edge.getEnd());

        RoutingQueryStrategy strategy = routingQueryStrategyProvider.strategy();

        RoutingQueryResult queryResult = strategy.route(start, end, modeOfTransport);

        return toRouteResult(queryResult);
    }


    private RoutePartEdgeResult toRouteResult(RoutingQueryResult queryResult) {

        double totalDistance = queryResult.getTotalDistance();
        List<String> geometries = queryResult.getGeometries();
        double duration = queryResult.getTotalTime();
        RoutePartEdgeResultStatus status = null;

        if (queryResult.getStatus() == 200) {
            status = OK;
        } else if (queryResult.getStatus() == 207) {
            status = NO_ROUTE;
        }

        return new RoutePartEdgeResult(totalDistance, duration, geometries, status);
    }


    private GeoLocation getGeoLocation(RoutePartNodeDto node) {

        GeoLocation result;

        switch (node.getType()) {
            case TERMINAL:
                result = terminalService.getByUniqueId(node.getUuid());
                break;

            case SEAPORT:
                result = seaportService.getByUniqueId(node.getUuid());
                break;

            case ADDRESS:
                result = addressServiceWrapper.getByHashKey(node.getHashKey());
                break;

            case GEOLOCATION:
                result = new GeoLocation(node.getLat(), node.getLon());
                break;

            default:
                throw new IllegalStateException("Unknown RoutePartNodeDtoType: " + node.getType());
        }

        return result;
    }
}
