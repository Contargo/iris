package net.contargo.iris.transport;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.co2.Co2CalculationHandlingParams;

import static net.contargo.iris.co2.Co2Calculator.handling;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.SEAPORT;
import static net.contargo.iris.transport.api.StopType.TERMINAL;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TransportDescriptionExtender {

    private final TransportDescriptionMainRunExtender mainRunExtender;
    private final TransportDescriptionRoadExtender roadExtender;

    public TransportDescriptionExtender(TransportDescriptionMainRunExtender mainRunExtender,
        TransportDescriptionRoadExtender roadExtender) {

        this.mainRunExtender = mainRunExtender;
        this.roadExtender = roadExtender;
    }

    /**
     * Extends a {@link net.contargo.iris.transport.api.TransportDescriptionDto} with distances and durations to a
     * {@link net.contargo.iris.transport.api.TransportResponseDto}.
     *
     * @param  description  the description dto
     *
     * @return  the response dto
     */
    public TransportResponseDto withRoutingInformation(TransportDescriptionDto description) {

        TransportResponseDto result = new TransportResponseDto(description);

        result.transportChain.forEach(segment -> {
            if (isNebenlauf(segment)) {
                roadExtender.forNebenlauf(segment);
            } else if (isMainRun(segment)) {
                mainRunExtender.with(segment);
            } else if (isRoadAndAddressOnly(segment)) {
                roadExtender.forAddressesOnly(segment);
            }

            Co2CalculationParams.Handling params = new Co2CalculationHandlingParams(segment);
            segment.co2 = segment.co2.add(handling(params));
        });

        return result;
    }


    private static boolean isRoadAndAddressOnly(TransportResponseDto.TransportResponseSegment segment) {

        return segment.isConnectionBetween(ADDRESS, ADDRESS) && segment.modeOfTransport == ROAD;
    }


    private static boolean isNebenlauf(TransportResponseDto.TransportResponseSegment segment) {

        return segment.isConnectionBetween(TERMINAL, ADDRESS) && segment.modeOfTransport == ROAD;
    }


    private static boolean isMainRun(TransportResponseDto.TransportResponseSegment segment) {

        return segment.isConnectionBetween(TERMINAL, SEAPORT) || segment.isConnectionBetween(SEAPORT, ADDRESS);
    }
}
