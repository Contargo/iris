package net.contargo.iris.truck.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.osrm.service.OSRMQueryResult;
import net.contargo.iris.osrm.service.OSRMQueryService;
import net.contargo.iris.truck.TruckRoute;

import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.math.RoundingMode;


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
    private static final int STANDARD_STREETDETAILS_LENGTH = 4;
    private static final int INDEX_3 = 3;
    private static final int INDEX_2 = 2;

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

        BigDecimal toll = extractToll(osrmResult.getRouteInstructions());
        BigDecimal distance = new BigDecimal(osrmResult.getTotalDistance()).divide(METERS_PER_KILOMETER, SCALE,
                RoundingMode.HALF_UP);
        BigDecimal duration = new BigDecimal(osrmResult.getTotalTime()).divide(SECONDS_PER_MINUTE, SCALE,
                RoundingMode.HALF_UP);

        return new TruckRoute(distance, toll, duration);
    }


    private BigDecimal extractToll(String[][] routeInstructions) {

        BigDecimal toll = BigDecimal.ZERO;
        toll = toll.setScale(SCALE);

        for (String[] routeInstruction : routeInstructions) {
            /*
             * route_instructions:
             *
             * 0 - driving directions : integer numbers as defined in the source
             * file DataStructures/TurnInstructions.h.
             *
             * 1 - way name (string)
             *    [
             *      street_name ("A 65")
             *      street_type ("motorway")
             *      toll_route ("yes")
             *      country ("DE")
             *    ]
             *
             * 2 - length in meters (integer)
             *
             * 3 - position
             *
             * 4 - time
             *
             * 5 - length with unit (string)
             *
             * 6 - Direction abbreviation (string) N: north, S: south, E: east, W: west, NW: North West, ...
             *
             * 7 - azimuth (float)
             */
            BigDecimal distance = new BigDecimal(routeInstruction[2]);
            distance = distance.divide(METERS_PER_KILOMETER);

            String[] streetDetails = routeInstruction[1].split("/;");

            // currently, only german ("DE") toll roads are considered.
            if (streetDetails.length == STANDARD_STREETDETAILS_LENGTH
                    && ("yes".equalsIgnoreCase(streetDetails[INDEX_2])
                        && "de".equalsIgnoreCase(streetDetails[INDEX_3]))) {
                toll = toll.add(distance);
            }
        }

        return toll;
    }
}
