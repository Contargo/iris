package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;

import org.springframework.util.Assert;

import static java.math.RoundingMode.UP;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
class Co2CalculationRoadParams implements Co2CalculationParams.Road {

    private final int distance;
    private final ContainerState loadingState;

    private Co2CalculationRoadParams(Integer distance, ContainerState loadingState) {

        this.distance = distance;
        this.loadingState = loadingState;

        Assert.notNull(this.loadingState, "must not be null");
    }

    static Co2CalculationParams.Road truckParams(RoutePart routePart) {

        int distance = routePart.getData().getDistance().setScale(0, UP).intValue();
        ContainerState loadingState = routePart.getContainerState();

        return new Co2CalculationRoadParams(distance, loadingState);
    }


    static Co2CalculationParams.Road dTruckParams(RoutePart routePart) {

        int distance = routePart.getData().getDtruckDistance().setScale(0, UP).intValue();
        ContainerState loadingState = routePart.getContainerState();

        return new Co2CalculationRoadParams(distance, loadingState);
    }


    @Override
    public int getDistance() {

        return distance;
    }


    @Override
    public ContainerState getLoadingState() {

        return loadingState;
    }
}
