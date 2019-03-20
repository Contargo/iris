package net.contargo.iris.transport.service.co2;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.transport.api.TransportResponseDto;

import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.transport.api.StopType.SEAPORT;
import static net.contargo.iris.transport.api.StopType.TERMINAL;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2CalculationRailParams implements Co2CalculationParams.Rail {

    private final int dieselDistance;
    private final int electricDistance;
    private final Direction direction;

    public Co2CalculationRailParams(MainRunConnection connection,
        TransportResponseDto.TransportResponseSegment segment) {

        dieselDistance = connection.getRailDieselDistance().intValue();
        electricDistance = connection.getRailElectricDistance().intValue();

        if (segment.from.type == SEAPORT && segment.to.type == TERMINAL) {
            direction = segment.loadingState == FULL ? Direction.IMPORT : Direction.EXPORT;
        } else if (segment.from.type == TERMINAL && segment.to.type == SEAPORT) {
            direction = segment.loadingState == FULL ? Direction.EXPORT : Direction.IMPORT;
        } else {
            throw new IllegalArgumentException("Illegal transport description segment: " + segment);
        }
    }

    @Override
    public int getDieselDistance() {

        return dieselDistance;
    }


    @Override
    public int getElectricDistance() {

        return electricDistance;
    }


    @Override
    public Direction getDirection() {

        return direction;
    }
}
