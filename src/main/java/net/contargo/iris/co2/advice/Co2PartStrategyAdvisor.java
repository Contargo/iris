package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RouteType;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.DTRUCK;
import static net.contargo.iris.route.RouteType.RAIL;
import static net.contargo.iris.route.RouteType.TRUCK;


/**
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartStrategyAdvisor {

    private Co2PartStrategy bargeStrategy;
    private Co2PartStrategy railStrategy;
    private Co2PartStrategy truckStrategy;
    private Co2PartStrategy dtruckStrategy;

    public Co2PartStrategyAdvisor(Co2PartStrategy bargeStrategy, Co2PartStrategy railStrategy,
        Co2PartStrategy truckStrategy, Co2PartStrategy dtruckStrategy) {

        this.bargeStrategy = bargeStrategy;
        this.railStrategy = railStrategy;
        this.truckStrategy = truckStrategy;
        this.dtruckStrategy = dtruckStrategy;
    }

    public Co2PartStrategy advice(RouteType routeType) {

        switch (routeType) {
            case BARGE:
                return bargeStrategy;

            case RAIL:
                return railStrategy;

            case TRUCK:
                return truckStrategy;

            case DTRUCK:
                return dtruckStrategy;

            default:
                throw new IllegalStateException("Cannot determine co2 for route part of type " + routeType);
        }
    }
}
