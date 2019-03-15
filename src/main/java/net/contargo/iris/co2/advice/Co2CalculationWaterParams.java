package net.contargo.iris.co2.advice;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.terminal.Region;

import org.springframework.util.Assert;

import static java.math.RoundingMode.UP;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2CalculationWaterParams implements Co2CalculationParams.Water {

    private final int distance;
    private final Region region;
    private final ContainerState loadingState;
    private final FlowDirection flowDirection;

    Co2CalculationWaterParams(RoutePart routePart) {

        distance = routePart.getData().getBargeDieselDistance().setScale(0, UP).intValue();
        region = routePart.findTerminal().getRegion();
        loadingState = routePart.getContainerState();
        flowDirection = FlowDirection.from(routePart.getDirection());

        Assert.notNull(region, "must not be null");
        Assert.notNull(loadingState, "must not be null");
    }

    @Override
    public int getDistance() {

        return distance;
    }


    @Override
    public Region getRegion() {

        return region;
    }


    @Override
    public ContainerState getLoadingState() {

        return loadingState;
    }


    @Override
    public FlowDirection getFlowDirection() {

        return flowDirection;
    }
}
