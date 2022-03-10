package net.contargo.iris.transport.co2;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.api.TransportStop;

import static net.contargo.iris.transport.api.StopType.TERMINAL;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2CalculationHandlingParams implements Co2CalculationParams.Handling {

    private final int numberOfTerminals;
    private final boolean partOfRoundtrip;

    public Co2CalculationHandlingParams(TransportResponseDto.TransportResponseSegment segment) {

        int numberOfTerminals = 0;

        numberOfTerminals += isTerminal(segment.from) ? 1 : 0;
        numberOfTerminals += isTerminal(segment.to) ? 1 : 0;

        this.numberOfTerminals = numberOfTerminals;
        this.partOfRoundtrip = segment.partOfRoundtrip;
    }

    private static boolean isTerminal(TransportStop stop) {

        return stop.type == TERMINAL;
    }


    @Override
    public int numberOfTerminals() {

        return numberOfTerminals;
    }


    @Override
    public boolean isPartOfRoundtrip() {

        return partOfRoundtrip;
    }
}
