package net.contargo.iris.transport.co2;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.util.TransportDescriptionUtils;

import org.springframework.util.Assert;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2CalculationWaterParams implements Co2CalculationParams.Water {

    private final int distance;
    private final Region region;
    private final ContainerState loadingState;
    private final FlowDirection flowDirection;

    public Co2CalculationWaterParams(MainRunConnection connection,
        TransportResponseDto.TransportResponseSegment segment) {

        distance = segment.distance.value;
        region = connection.getTerminal().getRegion();
        loadingState = segment.loadingState;
        flowDirection = TransportDescriptionUtils.getFlowDirection(segment);

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
