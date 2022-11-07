package net.contargo.iris.routedatarevision;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.ZERO;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class DistancesByCountryUtil {

    private DistancesByCountryUtil() {

        // hide the default constructor
    }

    public static Map<String, BigDecimal> getDistancesByCountry(RouteDataRevision routeDataRevision) {

        Map<String, BigDecimal> result = new HashMap<>();

        putCountryIfNotZero(result, "DE", routeDataRevision.getTruckDistanceOneWayInKilometerDe());
        putCountryIfNotZero(result, "NL", routeDataRevision.getTruckDistanceOneWayInKilometerNl());
        putCountryIfNotZero(result, "BE", routeDataRevision.getTruckDistanceOneWayInKilometerBe());
        putCountryIfNotZero(result, "LU", routeDataRevision.getTruckDistanceOneWayInKilometerLu());
        putCountryIfNotZero(result, "FR", routeDataRevision.getTruckDistanceOneWayInKilometerFr());
        putCountryIfNotZero(result, "CH", routeDataRevision.getTruckDistanceOneWayInKilometerCh());
        putCountryIfNotZero(result, "LI", routeDataRevision.getTruckDistanceOneWayInKilometerLi());
        putCountryIfNotZero(result, "AT", routeDataRevision.getTruckDistanceOneWayInKilometerAt());
        putCountryIfNotZero(result, "CZ", routeDataRevision.getTruckDistanceOneWayInKilometerCz());
        putCountryIfNotZero(result, "PL", routeDataRevision.getTruckDistanceOneWayInKilometerPl());
        putCountryIfNotZero(result, "DK", routeDataRevision.getTruckDistanceOneWayInKilometerDk());

        return result;
    }


    private static void putCountryIfNotZero(Map<String, BigDecimal> result, String countryCode, BigDecimal distance) {

        if (distance != null && distance.compareTo(ZERO) != 0) {
            result.put(countryCode, distance);
        }
    }
}
