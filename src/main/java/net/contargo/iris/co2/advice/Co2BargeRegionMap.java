package net.contargo.iris.co2.advice;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.terminal.Region;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;

import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.route.RoutePart.Direction.DOWNSTREAM;

import static java.util.Collections.unmodifiableMap;


/**
 * Map object containing all co2 factors for every possible barge routing combination of
 * {@link net.contargo.iris.route.RoutePart.Direction}, {@link Region} and {@link ContainerState}.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
class Co2BargeRegionMap {

    static final String FULL_UPSTREAM = "FullUpstream";
    static final String FULL_DOWNSTREAM = "FullDownstream";
    static final String EMPTY_UPSTREAM = "EmptyUpstream";
    static final String EMPTY_DOWNSTREAM = "EmptyDownstream";

    static final Map<String, BigDecimal> NIEDERRHEIN_CO2_MAP;

    static {
        Map<String, BigDecimal> map = new HashMap<>();
        map.put(FULL_UPSTREAM, new BigDecimal("0.31"));
        map.put(EMPTY_UPSTREAM, new BigDecimal("0.27"));
        map.put(FULL_DOWNSTREAM, new BigDecimal("0.17"));
        map.put(EMPTY_DOWNSTREAM, new BigDecimal("0.14"));
        NIEDERRHEIN_CO2_MAP = unmodifiableMap(map);
    }

    static final Map<String, BigDecimal> OBERRHEIN_CO2_MAP;

    static {
        Map<String, BigDecimal> map = new HashMap<>();
        map.put(FULL_UPSTREAM, new BigDecimal("0.43"));
        map.put(EMPTY_UPSTREAM, new BigDecimal("0.4"));
        map.put(FULL_DOWNSTREAM, new BigDecimal("0.23"));
        map.put(EMPTY_DOWNSTREAM, new BigDecimal("0.21"));
        OBERRHEIN_CO2_MAP = unmodifiableMap(map);
    }

    static final Map<String, BigDecimal> SCHELDE_CO2_MAP;

    static {
        Map<String, BigDecimal> map = new HashMap<>();
        map.put(FULL_UPSTREAM, new BigDecimal("0.427"));
        map.put(FULL_DOWNSTREAM, new BigDecimal("0.427"));
        map.put(EMPTY_UPSTREAM, new BigDecimal("0.375"));
        map.put(EMPTY_DOWNSTREAM, new BigDecimal("0.375"));
        SCHELDE_CO2_MAP = unmodifiableMap(map);
    }

    BigDecimal getCo2Factor(Region region, RoutePart.Direction direction, ContainerState state) {

        return getCo2Map(region).get(getCo2FactorsKey(direction, state));
    }


    String getCo2FactorsKey(RoutePart.Direction direction, ContainerState state) {

        if (state == FULL) {
            return direction == DOWNSTREAM ? FULL_DOWNSTREAM : FULL_UPSTREAM;
        } else {
            return direction == DOWNSTREAM ? EMPTY_DOWNSTREAM : EMPTY_UPSTREAM;
        }
    }


    private Map<String, BigDecimal> getCo2Map(Region region) {

        if (region == Region.NIEDERRHEIN || region == Region.NOT_SET) {
            return NIEDERRHEIN_CO2_MAP;
        }

        if (region == Region.OBERRHEIN) {
            return OBERRHEIN_CO2_MAP;
        }

        if (region == Region.SCHELDE) {
            return SCHELDE_CO2_MAP;
        }

        throw new IllegalStateException("There is no Co2 Map for region: " + region);
    }
}
