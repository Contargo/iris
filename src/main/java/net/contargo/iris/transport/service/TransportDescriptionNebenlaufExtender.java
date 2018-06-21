package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.TransportResponseDto;

import org.springframework.core.convert.ConversionService;

import java.math.BigInteger;

import static net.contargo.iris.co2.Co2Calculator.road;
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.TERMINAL;


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

        GeoLocation start = conversionService.convert(segment.from, GeoLocation.class);
        GeoLocation end = conversionService.convert(segment.to, GeoLocation.class);
        ModeOfTransport mot = segment.modeOfTransport;
        RouteResult routeResult = routeService.route(start, end, mot);

        segment.distance = routeResult.getDistance();
        segment.tollDistance = routeResult.getToll();
        segment.duration = routeResult.getDuration();
        segment.geometries = routeResult.getGeometries();

        applyRouteRevision(segment);

        segment.co2 = road(segment.distance, segment.loadingState);
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

        if (segment.from.type == ADDRESS) {
            return new GeoLocation(segment.from.lat, segment.from.lon);
        } else if (segment.to.type == ADDRESS) {
            return new GeoLocation(segment.to.lat, segment.to.lon);
        } else {
            return null;
        }
    }


    private static BigInteger getTerminalUuid(TransportResponseDto.TransportResponseSegment segment) {

        if (segment.from.type == TERMINAL) {
            return new BigInteger(segment.from.uuid);
        } else if (segment.to.type == TERMINAL) {
            return new BigInteger(segment.to.uuid);
        } else {
            return null;
        }
    }
}
