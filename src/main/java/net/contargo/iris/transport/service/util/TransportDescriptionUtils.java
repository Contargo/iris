package net.contargo.iris.transport.service.util;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.transport.api.TransportResponseDto;

import static net.contargo.iris.transport.api.StopType.SEAPORT;
import static net.contargo.iris.transport.api.StopType.TERMINAL;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class TransportDescriptionUtils {

    private TransportDescriptionUtils() {
    }

    public static FlowDirection getFlowDirection(TransportResponseDto.TransportResponseSegment segment) {

        if (segment.from.type == SEAPORT && segment.to.type == TERMINAL) {
            return FlowDirection.UPSTREAM;
        }

        if (segment.from.type == TERMINAL && segment.to.type == SEAPORT) {
            return FlowDirection.DOWNSTREAM;
        }

        throw new IllegalArgumentException("Illegal transport description segment: " + segment);
    }
}
