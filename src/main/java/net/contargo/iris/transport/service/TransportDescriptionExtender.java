package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;

import org.springframework.core.convert.ConversionService;

import java.math.BigInteger;

import java.util.Optional;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.SiteType.ADDRESS;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TransportDescriptionExtender {

    private final RouteService routeService;
    private final ConversionService conversionService;
    private final RouteDataRevisionService routeDataRevisionService;

    public TransportDescriptionExtender(RouteService routeService, ConversionService conversionService,
        RouteDataRevisionService routeDataRevisionService) {

        this.routeService = routeService;
        this.conversionService = conversionService;
        this.routeDataRevisionService = routeDataRevisionService;
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

        result.transportChain.stream()
            .filter(transportSegment -> transportSegment.modeOfTransport == ROAD)
            .forEach(s -> {
                GeoLocation start = conversionService.convert(s.fromSite, GeoLocation.class);
                GeoLocation end = conversionService.convert(s.toSite, GeoLocation.class);
                ModeOfTransport mot = s.modeOfTransport;
                RouteResult routeResult = routeService.route(start, end, mot);

                s.distance = routeResult.getDistance();
                s.tollDistance = routeResult.getToll();
                s.duration = routeResult.getDuration();
                s.geometries = routeResult.getGeometries();

                applyRouteRevision(s);
            });

        return result;
    }


    private void applyRouteRevision(TransportResponseDto.TransportResponseSegment segment) {

        if (isNebenlauf(segment)) {
            BigInteger uuid = getTerminalUuid(segment);
            GeoLocation address = getAddress(segment);

            Optional<RouteDataRevision> routeDataRevisionMaybe = routeDataRevisionService.getRouteDataRevision(uuid,
                    address);

            routeDataRevisionMaybe.ifPresent(r -> {
                segment.distance = r.getTruckDistanceOneWayInKilometer().intValue();
                segment.tollDistance = r.getTollDistanceOneWayInKilometer().intValue();
            });
        }
    }


    private BigInteger getTerminalUuid(TransportResponseDto.TransportResponseSegment segment) {

        if (segment.fromSite.type == TERMINAL) {
            return new BigInteger(segment.fromSite.uuid);
        } else if (segment.toSite.type == TERMINAL) {
            return new BigInteger(segment.toSite.uuid);
        } else {
            return null;
        }
    }


    private GeoLocation getAddress(TransportResponseDto.TransportResponseSegment segment) {

        if (segment.fromSite.type == ADDRESS) {
            return new GeoLocation(segment.fromSite.lat, segment.fromSite.lon);
        } else if (segment.toSite.type == ADDRESS) {
            return new GeoLocation(segment.toSite.lat, segment.toSite.lon);
        } else {
            return null;
        }
    }


    private static boolean isNebenlauf(TransportResponseDto.TransportResponseSegment segment) {

        return (segment.fromSite.type == TERMINAL && segment.toSite.type == ADDRESS)
            || (segment.fromSite.type == ADDRESS && segment.toSite.type == TERMINAL);
    }
}
