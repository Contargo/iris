package net.contargo.iris.transport.service;

import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;

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
    private final TransportDescriptionNebenlaufExtender nebenlaufExtender;

    public TransportDescriptionExtender(TransportDescriptionMainRunExtender mainRunExtender,
        TransportDescriptionNebenlaufExtender nebenlaufExtender) {

        this.mainRunExtender = mainRunExtender;
        this.nebenlaufExtender = nebenlaufExtender;
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

        result.transportChain.forEach(s -> {
            if (isNebenlauf(s)) {
                nebenlaufExtender.with(s);
            } else if (isMainRun(s)) {
                mainRunExtender.with(s);
            }

            boolean toIsTerminal = s.to.type == TERMINAL;
            boolean fromIsTerminal = s.from.type == TERMINAL;
            s.co2 = s.co2.add(handling(fromIsTerminal, toIsTerminal));
        });

        return result;
    }


    private static boolean isNebenlauf(TransportResponseDto.TransportResponseSegment segment) {

        return ((segment.from.type == TERMINAL && segment.to.type == ADDRESS)
                || (segment.from.type == ADDRESS && segment.to.type == TERMINAL)) && segment.modeOfTransport == ROAD;
    }


    private static boolean isMainRun(TransportResponseDto.TransportResponseSegment segment) {

        return (segment.from.type == TERMINAL && segment.to.type == SEAPORT)
            || (segment.from.type == SEAPORT && segment.to.type == TERMINAL);
    }
}
