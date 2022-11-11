package net.contargo.iris.truck.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.OsrmRoutingClient;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.truck.TruckRoute;

import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;

import java.util.Map;
import java.util.stream.Collectors;

import static net.contargo.iris.routing.osrm.OSRMProfile.DRIVING;

import static java.math.RoundingMode.HALF_UP;


/**
 * Service to get information of the truck route via OSRM. The toll will be calculates through the route instructions.
 * Further information see below.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
public class OSRMTruckRouteService implements TruckRouteService {

    private static final BigDecimal METERS_PER_KILOMETER = new BigDecimal("1000.0");
    private static final BigDecimal SECONDS_PER_MINUTE = new BigDecimal("60.0");
    private static final int SCALE = 5;

    private final OsrmRoutingClient osrmRoutingClient;

    public OSRMTruckRouteService(OsrmRoutingClient osrmRoutingClient) {

        this.osrmRoutingClient = osrmRoutingClient;
    }

    @Override
    @Cacheable("osrmCache")
    public TruckRoute route(GeoLocation start, GeoLocation destination) {

        RoutingQueryResult osrmResult = osrmRoutingClient.route(start, destination, DRIVING);

        if (osrmResult.noRoute()) {
            throw new OSRMNonRoutableRouteException("Start: "
                + start.toString() + " Destination: " + destination.toString() + " Status: " + osrmResult.getStatus());
        }

        BigDecimal toll = osrmResult.getToll();
        BigDecimal distance = BigDecimal.valueOf(osrmResult.getTotalDistance())
                .divide(METERS_PER_KILOMETER, SCALE, HALF_UP);

        Map<String, BigDecimal> distancesByCountry = osrmResult.getDistancesByCountry().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> BigDecimal.valueOf(e.getValue())
                                .divide(METERS_PER_KILOMETER, SCALE, HALF_UP)));

        BigDecimal duration = BigDecimal.valueOf(osrmResult.getTotalTime()).divide(SECONDS_PER_MINUTE, SCALE, HALF_UP);

        return new TruckRoute(distance, toll, duration, distancesByCountry);
    }
}
