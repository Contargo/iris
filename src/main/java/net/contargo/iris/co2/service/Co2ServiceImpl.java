package net.contargo.iris.co2.service;

import net.contargo.iris.co2.advice.Co2PartStrategy;
import net.contargo.iris.co2.advice.Co2PartStrategyAdvisor;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.builder.DirectTruckRouteBuilder;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class Co2ServiceImpl implements Co2Service {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    // Umschlag "CO2 Handling"
    private static final BigDecimal CO2_HANDLING = BigDecimal.valueOf(8);

    // Umschlag "Direkttruck Oneway"
    private static final BigDecimal CO2_TRUCK_ONE_WAY = BigDecimal.valueOf(4);

    private final DirectTruckRouteBuilder directTruckRouteBuilder;
    private final Co2PartStrategyAdvisor co2PartStrategyAdvisor;

    Co2ServiceImpl(DirectTruckRouteBuilder directTruckRouteBuilder, Co2PartStrategyAdvisor co2PartStrategyAdvisor) {

        this.directTruckRouteBuilder = directTruckRouteBuilder;
        this.co2PartStrategyAdvisor = co2PartStrategyAdvisor;
    }

    @Override
    public BigDecimal getEmission(Route route) {

        BigDecimal co2 = BigDecimal.ZERO;

        List<RoutePart> parts = route.getData().getParts();

        for (int i = 0; i < parts.size(); i++) {
            RoutePart part = parts.get(i);
            RouteType type = part.getRouteType();

            Co2PartStrategy strategy = co2PartStrategyAdvisor.advice(type);
            co2 = co2.add(strategy.getEmissionForRoutePart(part));

            if (co2HandlingRequired(parts, i)) {
                co2 = co2.add(CO2_HANDLING);
            }
        }

        if (!route.isRoundTrip()) {
            co2 = co2.add(CO2_TRUCK_ONE_WAY);
        }

        LOG.debug("Setting CO2 for route {}: {} kg", route.getName(), co2);

        return co2;
    }


    @Override
    public BigDecimal getEmissionDirectTruck(Route route) {

        Route truckRoute = directTruckRouteBuilder.getCorrespondingDirectTruckRoute(route);

        BigDecimal co2 = BigDecimal.ZERO;
        List<RoutePart> parts = truckRoute.getData().getParts();

        for (RoutePart part : parts) {
            Co2PartStrategy strategy = co2PartStrategyAdvisor.advice(RouteType.TRUCK);
            co2 = co2.add(strategy.getEmissionForRoutePart(part));
        }

        if (!truckRoute.isRoundTrip()) {
            co2 = co2.add(CO2_TRUCK_ONE_WAY);
        }

        LOG.debug("Setting CO2 Direct Truck for route {}: {} kg", route.getName(), co2);

        return co2;
    }


    /**
     * If there a is a change in transport, you have to consider Co2-Handling in emission calculation. (e.g. from truck
     * route to rail route). Please notice that there are two different Co2-Handling values: one for direct truck and
     * one for transport with main run (barge or rail)
     *
     * @param  routeParts
     * @param  indexOfRoutePart
     *
     * @return  true if Co2 Handling is to be considered in emission calculation, false if not
     */
    private boolean co2HandlingRequired(List<RoutePart> routeParts, int indexOfRoutePart) {

        RoutePart part = routeParts.get(indexOfRoutePart);

        // if this part is not the last route part AND the next part is not of the same route type, then return true
        return (indexOfRoutePart < (routeParts.size() - 1))
            && (!part.isOfType(routeParts.get(indexOfRoutePart + 1).getRouteType()));
    }
}
