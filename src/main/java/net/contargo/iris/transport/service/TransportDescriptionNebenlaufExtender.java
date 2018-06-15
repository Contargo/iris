package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.TransportResponseDto;

import org.springframework.core.convert.ConversionService;

import java.math.BigInteger;

import static net.contargo.iris.transport.api.SiteType.ADDRESS;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TransportDescriptionNebenlaufExtender {

    private final RouteService routeService;
    private final ConversionService conversionService;
    private final RouteDataRevisionService routeDataRevisionService;

    public TransportDescriptionNebenlaufExtender(RouteService routeService, ConversionService conversionService,
        RouteDataRevisionService routeDataRevisionService) {

        this.routeService = routeService;
        this.conversionService = conversionService;
        this.routeDataRevisionService = routeDataRevisionService;
    }

    void with(TransportResponseDto.TransportResponseSegment segment) {

        GeoLocation start = conversionService.convert(segment.fromSite, GeoLocation.class);
        GeoLocation end = conversionService.convert(segment.toSite, GeoLocation.class);
        ModeOfTransport mot = segment.modeOfTransport;
        RouteResult routeResult = routeService.route(start, end, mot);

        segment.distance = routeResult.getDistance();
        segment.tollDistance = routeResult.getToll();
        segment.duration = routeResult.getDuration();
        segment.geometries = routeResult.getGeometries();

        applyRouteRevision(segment);

        segment.co2 = Co2Calculator.truck(segment.distance, segment.loadingState);
    }


    private void applyRouteRevision(TransportResponseDto.TransportResponseSegment segment) {

        BigInteger uuid = getTerminalUuid(segment);

        GeoLocation address = getAddress(segment);

        routeDataRevisionService.getRouteDataRevision(uuid, address).ifPresent(r -> {
            segment.distance = r.getTruckDistanceOneWayInKilometer().intValue();
            segment.tollDistance = r.getTollDistanceOneWayInKilometer().intValue();
        });
    }


    private static GeoLocation getAddress(TransportResponseDto.TransportResponseSegment segment) {

        if (segment.fromSite.type == ADDRESS) {
            return new GeoLocation(segment.fromSite.lat, segment.fromSite.lon);
        } else if (segment.toSite.type == ADDRESS) {
            return new GeoLocation(segment.toSite.lat, segment.toSite.lon);
        } else {
            return null;
        }
    }


    private static BigInteger getTerminalUuid(TransportResponseDto.TransportResponseSegment segment) {

        if (segment.fromSite.type == TERMINAL) {
            return new BigInteger(segment.fromSite.uuid);
        } else if (segment.toSite.type == TERMINAL) {
            return new BigInteger(segment.toSite.uuid);
        } else {
            return null;
        }
    }
}
