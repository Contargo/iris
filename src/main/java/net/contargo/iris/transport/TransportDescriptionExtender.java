package net.contargo.iris.transport;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.co2.Co2CalculationHandlingParams;

import java.util.List;

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
     * Maps the specified {@link net.contargo.iris.transport.api.TransportDescriptionDto} to a
     * {@link net.contargo.iris.transport.api.TransportResponseDto} whose transport chain segments are enriched with
     * values for distance, duration and Co2 emission.
     *
     * @param  description  the transport description
     *
     * @return  the transport response
     */
    public TransportResponseDto withRoutingInformation(TransportDescriptionDto description) {

        TransportResponseDto transportResponse = toTransportResponse(description);

        transportResponse.transportChain.forEach(segment -> {
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

        return transportResponse;
    }


    private static TransportResponseDto toTransportResponse(TransportDescriptionDto description) {

        TransportResponseDto transportResponse = new TransportResponseDto(description);

        if (isRoundTrip(transportResponse)) {
            // flag each segment that it is part of a roundtrip transport
            transportResponse.transportChain.forEach(s -> s.partOfRoundtrip = true);
        }

        return transportResponse;
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


    private static boolean isRoundTrip(TransportResponseDto transportResponse) {

        List<TransportResponseDto.TransportResponseSegment> segments = transportResponse.transportChain;
        int lastSegmentIndex = segments.size() - 1;

        return segments.get(0).from.type == SEAPORT && segments.get(lastSegmentIndex).to.type == SEAPORT;
    }
}
