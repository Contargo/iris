package net.contargo.iris.truck.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.osrm.service.OSRMQueryResult;
import net.contargo.iris.osrm.service.OSRMQueryService;
import net.contargo.iris.truck.TruckRoute;

import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;


/**
 * Service to get information of the truck route via OSRM. The toll will be calculates through the route instructions.
 * Further information see below.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class OSRMTruckRouteService implements TruckRouteService {

    private static final BigDecimal METERS_PER_KILOMETER = new BigDecimal("1000.0");
    private static final BigDecimal SECONDS_PER_MINUTE = new BigDecimal("60.0");
    private static final int STATUS_NO_ROUTE = 207;
    private static final int SCALE = 5;

    private final OSRMQueryService osrmQueryService;

    public OSRMTruckRouteService(OSRMQueryService osrmQueryService) {

        this.osrmQueryService = osrmQueryService;
    }

    @Override
    @Cacheable(value = "routingCache")
    public TruckRoute route(GeoLocation start, GeoLocation destination) {

        OSRMQueryResult osrmResult = osrmQueryService.getOSRMXmlRoute(start, destination);

        if (osrmResult.getStatus() == STATUS_NO_ROUTE) {
            throw new OSRMNonRoutableRouteException("Start: "
                + start.toString() + " Destination: " + destination.toString() + " Status: " + osrmResult.getStatus());
        }

        BigDecimal toll = osrmResult.getToll();
        BigDecimal distance = new BigDecimal(osrmResult.getTotalDistance()).divide(METERS_PER_KILOMETER, SCALE,
                HALF_UP);
        BigDecimal duration = new BigDecimal(osrmResult.getTotalTime()).divide(SECONDS_PER_MINUTE, SCALE, HALF_UP);

        return new TruckRoute(distance, toll, duration);
    }
}
