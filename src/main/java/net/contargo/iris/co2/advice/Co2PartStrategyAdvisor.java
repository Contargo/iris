package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RouteType;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.BARGE_RAIL;
import static net.contargo.iris.route.RouteType.DTRUCK;
import static net.contargo.iris.route.RouteType.RAIL;
import static net.contargo.iris.route.RouteType.TRUCK;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2PartStrategyAdvisor {

    private Co2PartStrategy bargeStrategy;
    private Co2PartStrategy railStrategy;
    private Co2PartStrategy truckStrategy;
    private Co2PartStrategy bargeRailStrategy;
    private Co2PartStrategy dtruckStrategy;

    Co2PartStrategyAdvisor() {

        // no-arg constructor used for unit testing
    }


    public Co2PartStrategyAdvisor(Co2PartStrategy bargeStrategy, Co2PartStrategy railStrategy,
        Co2PartStrategy truckStrategy, Co2PartStrategy bargeRailStrategy, Co2PartStrategy dtruckStrategy) {

        this.bargeStrategy = bargeStrategy;
        this.railStrategy = railStrategy;
        this.truckStrategy = truckStrategy;
        this.bargeRailStrategy = bargeRailStrategy;
        this.dtruckStrategy = dtruckStrategy;
    }

    void setBargeStrategy(Co2PartStrategy bargeStrategy) {

        this.bargeStrategy = bargeStrategy;
    }


    void setRailStrategy(Co2PartStrategy railStrategy) {

        this.railStrategy = railStrategy;
    }


    void setTruckStrategy(Co2PartStrategy truckStrategy) {

        this.truckStrategy = truckStrategy;
    }


    void setBargeRailStrategy(Co2PartStrategy bargeRailStrategy) {

        this.bargeRailStrategy = bargeRailStrategy;
    }


    public void setDtruckStrategy(Co2PartStrategy dtruckStrategy) {

        this.dtruckStrategy = dtruckStrategy;
    }


    public Co2PartStrategy advice(RouteType routeType) {

        if (routeType == BARGE) {
            return bargeStrategy;
        }

        if (routeType == RAIL) {
            return railStrategy;
        }

        if (routeType == TRUCK) {
            return truckStrategy;
        }

        if (routeType == BARGE_RAIL) {
            return bargeRailStrategy;
        }

        if (routeType == DTRUCK) {
            return dtruckStrategy;
        }

        throw new IllegalStateException("Cannot determine co2 for route part of type " + routeType);
    }
}
