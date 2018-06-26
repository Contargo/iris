package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.units.Distance;
import net.contargo.iris.units.Duration;
import net.contargo.iris.units.Weight;

import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.co2.Co2Calculator.road;
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.TERMINAL;
import static net.contargo.iris.units.LengthUnit.KILOMETRE;
import static net.contargo.iris.units.MassUnit.KILOGRAM;
import static net.contargo.iris.units.TimeUnit.MINUTE;


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

        segment.distance = new Distance(routeResult.getDistance(), KILOMETRE);
        segment.tollDistance = new Distance(routeResult.getToll(), KILOMETRE);
        segment.duration = new Duration(routeResult.getDuration(), MINUTE);
        segment.geometries = routeResult.getGeometries();

        applyRouteRevision(segment);

        BigDecimal co2Value = road(routeResult.getDistance(), segment.loadingState);
        segment.co2 = new Weight(co2Value, KILOGRAM);
    }


    private void applyRouteRevision(TransportResponseDto.TransportResponseSegment segment) {

        BigInteger uuid = getTerminalUuid(segment);

        GeoLocation address = getAddress(segment);

        routeDataRevisionService.getRouteDataRevision(uuid, address).ifPresent(r -> {
            segment.distance = new Distance(r.getTruckDistanceOneWayInKilometer().intValue(), KILOMETRE);
            segment.tollDistance = new Distance(r.getTollDistanceOneWayInKilometer().intValue(), KILOMETRE);
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
