package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.SiteType;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;

import org.springframework.core.convert.ConversionService;

import java.math.BigInteger;

import java.util.Optional;

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

    private final RouteService routeService;
    private final ConversionService conversionService;
    private final RouteDataRevisionService routeDataRevisionService;
    private final TransportDescriptionMainRunExtender mainRunExtender;

    public TransportDescriptionExtender(RouteService routeService, ConversionService conversionService,
        RouteDataRevisionService routeDataRevisionService, TransportDescriptionMainRunExtender mainRunExtender) {

        this.routeService = routeService;
        this.conversionService = conversionService;
        this.routeDataRevisionService = routeDataRevisionService;
        this.mainRunExtender = mainRunExtender;
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
                extendNebenlauf(s);
            } else if (isMainRun(s)) {
                mainRunExtender.with(s);
            }
        });

        return result;
    }


    private void extendNebenlauf(TransportResponseDto.TransportResponseSegment segment) {

        GeoLocation start = conversionService.convert(segment.fromSite, GeoLocation.class);
        GeoLocation end = conversionService.convert(segment.toSite, GeoLocation.class);
        ModeOfTransport mot = segment.modeOfTransport;
        RouteResult routeResult = routeService.route(start, end, mot);

        segment.distance = routeResult.getDistance();
        segment.tollDistance = routeResult.getToll();
        segment.duration = routeResult.getDuration();
        segment.geometries = routeResult.getGeometries();

        applyRouteRevision(segment);
    }


    private void applyRouteRevision(TransportResponseDto.TransportResponseSegment segment) {

        BigInteger uuid = getUuid(segment, TERMINAL);

        GeoLocation address = getAddress(segment);

        Optional<RouteDataRevision> routeDataRevisionMaybe = routeDataRevisionService.getRouteDataRevision(uuid,
                address);

        routeDataRevisionMaybe.ifPresent(r -> {
            segment.distance = r.getTruckDistanceOneWayInKilometer().intValue();
            segment.tollDistance = r.getTollDistanceOneWayInKilometer().intValue();
        });
    }


    static BigInteger getUuid(TransportResponseDto.TransportResponseSegment segment, SiteType siteType) {

        if (segment.fromSite.type == siteType) {
            return new BigInteger(segment.fromSite.uuid);
        } else if (segment.toSite.type == siteType) {
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

        return ((segment.fromSite.type == TERMINAL && segment.toSite.type == ADDRESS)
                || (segment.fromSite.type == ADDRESS && segment.toSite.type == TERMINAL))
            && segment.modeOfTransport == ROAD;
    }


    private static boolean isMainRun(TransportResponseDto.TransportResponseSegment segment) {

        return (segment.fromSite.type == TERMINAL && segment.toSite.type == SEAPORT)
            || (segment.fromSite.type == SEAPORT && segment.toSite.type == TERMINAL);
    }
}
