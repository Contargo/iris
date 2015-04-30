package net.contargo.iris.co2.advice;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;

import java.math.BigDecimal;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
class Co2PartTruckStrategy implements Co2PartStrategy {

    private static final BigDecimal CO2_TRUCK_FULL = BigDecimal.valueOf(0.88);
    private static final BigDecimal CO2_TRUCK_EMPTY = BigDecimal.valueOf(0.73);

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        RoutePartData routePartData = routePart.getData();

        BigDecimal co2Factor;

        if (ContainerState.FULL == routePart.getContainerState()) {
            co2Factor = CO2_TRUCK_FULL;
        } else {
            co2Factor = CO2_TRUCK_EMPTY;
        }

        BigDecimal distance = routePartData.getDistance();

        return distance.multiply(co2Factor);
    }
}
