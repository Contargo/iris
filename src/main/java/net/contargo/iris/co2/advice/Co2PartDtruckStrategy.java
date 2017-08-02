package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;

import java.math.BigDecimal;

import static net.contargo.iris.co2.advice.Co2PartTruckStrategy.CO2_TRUCK_EMPTY;
import static net.contargo.iris.co2.advice.Co2PartTruckStrategy.CO2_TRUCK_FULL;
import static net.contargo.iris.container.ContainerState.FULL;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartDtruckStrategy implements Co2PartStrategy {

    @Override
    public BigDecimal getEmissionForRoutePart(RoutePart routePart) {

        RoutePartData routePartData = routePart.getData();

        BigDecimal co2Factor;

        if (FULL == routePart.getContainerState()) {
            co2Factor = CO2_TRUCK_FULL;
        } else {
            co2Factor = CO2_TRUCK_EMPTY;
        }

        BigDecimal distance = routePartData.getDtruckDistance();

        return distance.multiply(co2Factor);
    }
}
