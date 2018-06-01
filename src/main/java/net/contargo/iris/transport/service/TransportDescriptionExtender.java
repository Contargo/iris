package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;

import org.springframework.core.convert.ConversionService;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TransportDescriptionExtender {

    private final RouteService routeService;
    private final ConversionService conversionService;

    public TransportDescriptionExtender(RouteService routeService, ConversionService conversionService) {

        this.routeService = routeService;
        this.conversionService = conversionService;
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

        result.transportDescription.stream()
            .filter(transportSegment -> transportSegment.modeOfTransport == ROAD)
            .forEach(s -> {
                GeoLocation start = conversionService.convert(s.fromSite, GeoLocation.class);
                GeoLocation end = conversionService.convert(s.toSite, GeoLocation.class);
                ModeOfTransport mot = s.modeOfTransport;
                RouteResult routeResult = routeService.route(start, end, mot);

                s.distance = routeResult.getDistance();
                s.tollDistance = routeResult.getToll();
                s.duration = routeResult.getDuration();
            });

        return result;
    }
}
