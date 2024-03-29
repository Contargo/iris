package net.contargo.iris.transport.co2;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.transport.api.TransportResponseDto;

import org.springframework.util.Assert;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2CalculationRoadParams implements Co2CalculationParams.Road {

    private final int distance;
    private final ContainerState loadingState;
    private final boolean directTruck;

    public Co2CalculationRoadParams(TransportResponseDto.TransportResponseSegment segment, boolean directTruck) {

        this.distance = segment.distance.value;
        this.loadingState = segment.loadingState;
        this.directTruck = directTruck;

        Assert.notNull(this.loadingState, "must not be null");
    }

    @Override
    public int getDistance() {

        return distance;
    }


    @Override
    public ContainerState getLoadingState() {

        return loadingState;
    }


    @Override
    public boolean isDirectTruck() {

        return directTruck;
    }
}
