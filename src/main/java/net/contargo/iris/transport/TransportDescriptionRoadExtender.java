package net.contargo.iris.transport;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.co2.Co2CalculationRoadParams;
import net.contargo.iris.transport.route.RouteResult;
import net.contargo.iris.transport.route.RouteService;
import net.contargo.iris.units.Distance;
import net.contargo.iris.units.Duration;
import net.contargo.iris.units.Weight;

import org.springframework.core.convert.ConversionService;

import java.math.BigInteger;

import java.util.Map;

import static net.contargo.iris.co2.Co2Calculator.road;
import static net.contargo.iris.routedatarevision.DistancesByCountryUtil.getDistancesByCountry;
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.TERMINAL;
import static net.contargo.iris.units.LengthUnit.KILOMETRE;
import static net.contargo.iris.units.MassUnit.KILOGRAM;
import static net.contargo.iris.units.TimeUnit.MINUTE;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TransportDescriptionRoadExtender {

    private final RouteService routeService;
    private final ConversionService conversionService;
    private final RouteDataRevisionService routeDataRevisionService;

    public TransportDescriptionRoadExtender(RouteService routeService, ConversionService conversionService,
        RouteDataRevisionService routeDataRevisionService) {

        this.routeService = routeService;
        this.conversionService = conversionService;
        this.routeDataRevisionService = routeDataRevisionService;
    }

    void forNebenlauf(TransportResponseDto.TransportResponseSegment segment) {

        withTrucking(segment, true, false);
    }


    void forAddressesOnly(TransportResponseDto.TransportResponseSegment segment) {

        withTrucking(segment, false, false);
    }


    public void withTrucking(TransportResponseDto.TransportResponseSegment segment, boolean includeRouteRevision,
        boolean isDirectTruck) {

        GeoLocation start = conversionService.convert(segment.from, GeoLocation.class);
        GeoLocation end = conversionService.convert(segment.to, GeoLocation.class);
        ModeOfTransport mot = segment.modeOfTransport;
        RouteResult routeResult = routeService.route(start, end, mot);

        segment.distance = new Distance(routeResult.getDistance(), KILOMETRE);
        segment.tollDistance = new Distance(routeResult.getToll(), KILOMETRE);
        segment.duration = new Duration(routeResult.getDuration(), MINUTE);
        segment.geometries = routeResult.getGeometries();
        segment.distancesByCountry = routeResult.getDistancesByCountry()
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Distance(e.getValue(), KILOMETRE)));

        if (includeRouteRevision) {
            // applying a route revision changes the distances on the segment
            applyRouteRevision(segment);
        }

        alignDistancesByCountry(segment);

        // with the route distance set on the segment, calculate co2 emissions
        Co2CalculationParams.Road params = new Co2CalculationRoadParams(segment, isDirectTruck);
        segment.co2 = new Weight(road(params), KILOGRAM);
    }


    private void alignDistancesByCountry(TransportResponseDto.TransportResponseSegment segment) {

        Map<String, Distance> distancesByCountry = segment.distancesByCountry;
        String countryMaxDistance = distancesByCountry.entrySet().stream()
                .max(comparing(e -> e.getValue().value))
                .map(Map.Entry::getKey)
                .orElse(null);

        Integer totalDistanceByCountry = distancesByCountry.values().stream().map(d -> d.value)
                .reduce(0, Integer::sum);
        int distanceDifference = segment.distance.value - totalDistanceByCountry;

        Integer oldDistance = segment.distancesByCountry.get(countryMaxDistance).value;

        segment.distancesByCountry.put(countryMaxDistance, new Distance(oldDistance + distanceDifference, KILOMETRE));
    }


    private void applyRouteRevision(TransportResponseDto.TransportResponseSegment segment) {

        BigInteger uuid = getTerminalUuid(segment);

        GeoLocation address = getAddress(segment);

        routeDataRevisionService.getRouteDataRevision(uuid, address).ifPresent(r -> {
            segment.distance = new Distance(r.getTruckDistanceOneWayInKilometer().intValue(), KILOMETRE);
            segment.tollDistance = new Distance(r.getTollDistanceOneWayInKilometer().intValue(), KILOMETRE);
            segment.distancesByCountry = getDistancesByCountry(r).entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Distance(e.getValue().intValue(), KILOMETRE)));
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
