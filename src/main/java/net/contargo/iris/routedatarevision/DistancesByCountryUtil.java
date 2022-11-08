package net.contargo.iris.routedatarevision;

import net.contargo.iris.countries.service.CountryCode;

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

        CountryCode.countries().stream()
            .map(CountryCode::getValue)
            .forEach(c ->
                    putCountryIfNotZero(result, c, routeDataRevision.getTruckDistanceOnWayInKilometerCountry().get(c)));

        return result;
    }


    private static void putCountryIfNotZero(Map<String, BigDecimal> result, String countryCode, BigDecimal distance) {

        if (distance != null && distance.compareTo(ZERO) != 0) {
            result.put(countryCode, distance);
        }
    }
}
