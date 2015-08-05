package net.contargo.iris.co2.advice;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;

import java.math.BigDecimal;


/**
 * Co2 strategy for main run connections with route type rail.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
class Co2PartRailStrategy implements Co2PartStrategy {

    private static final BigDecimal CO2_RAIL_FULL_DIESEL = BigDecimal.valueOf(0.5);
    private static final BigDecimal CO2_RAIL_EMPTY_DIESEL = BigDecimal.valueOf(0.4);
    private static final BigDecimal CO2_RAIL_FULL_ELEKTRO = BigDecimal.valueOf(0.34);
    private static final BigDecimal CO2_RAIL_EMPTY_ELEKTRO = BigDecimal.valueOf(0.27);

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        BigDecimal co2 = BigDecimal.ZERO;

        RoutePartData routePartData = routePart.getData();

        BigDecimal distance1 = routePartData.getRailDieselDistance();
        BigDecimal distance2 = routePartData.getElectricDistance();

        BigDecimal co2DieselFactor;
        BigDecimal co2ElektroFactor;

        if (ContainerState.FULL == routePart.getContainerState()) {
            co2DieselFactor = CO2_RAIL_FULL_DIESEL;
            co2ElektroFactor = CO2_RAIL_FULL_ELEKTRO;
        } else {
            co2DieselFactor = CO2_RAIL_EMPTY_DIESEL;
            co2ElektroFactor = CO2_RAIL_EMPTY_ELEKTRO;
        }

        co2 = co2.add(distance1.multiply(co2DieselFactor));
        co2 = co2.add(distance2.multiply(co2ElektroFactor));

        return co2;
    }
}
