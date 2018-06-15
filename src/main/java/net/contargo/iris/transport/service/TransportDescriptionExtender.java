package net.contargo.iris.transport.service;

import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.SiteType.ADDRESS;
import static net.contargo.iris.transport.api.SiteType.SEAPORT;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;


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
        });

        return result;
    }


    private static boolean isNebenlauf(TransportResponseDto.TransportResponseSegment segment) {

        return ((segment.fromSite.type == TERMINAL && segment.toSite.type == ADDRESS)
                || (segment.fromSite.type == ADDRESS && segment.toSite.type == TERMINAL))
            && segment.modeOfTransport == ROAD;
    }


    private static boolean isMainRun(TransportResponseDto.TransportResponseSegment segment) {

        return (segment.fromSite.type == TERMINAL && segment.toSite.type == SEAPORT)
            || (segment.fromSite.type == SEAPORT && segment.toSite.type == TERMINAL);
    }
}
