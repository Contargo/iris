package net.contargo.iris.route.api;

import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.api.ControllerConstants;
import net.contargo.iris.connection.dto.RouteDataDto;
import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.connection.dto.RoutePartDto;
import net.contargo.iris.connection.dto.SeaportConnectionRoutesDtoService;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.RouteCombo;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.route.dto.EnricherDtoService;
import net.contargo.iris.route.dto.RoutingResultDto;
import net.contargo.iris.route.service.RouteUrlSerializationService;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.seaport.dto.SeaportDtoService;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.contargo.iris.container.ContainerType.TWENTY_LIGHT;
import static net.contargo.iris.route.RouteCombo.WATERWAY;
import static net.contargo.iris.route.RouteDirection.EXPORT;
import static net.contargo.iris.route.RouteProduct.ONEWAY;
import static net.contargo.iris.route.RouteType.BARGE;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import static java.math.BigDecimal.ZERO;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@Controller
public class RoutesApiController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final String ROUTE_DETAILS_URL = "/routedetails";
    private static final String ROUTE_PART_DETAILS_URL = "/routepartdetails";

    private final SeaportDtoService seaportDtoService;
    private final SeaportConnectionRoutesDtoService seaportConnectionRoutesDtoService;
    private final RouteUrlSerializationService routeUrlSerializationService;
    private final EnricherDtoService enricherDtoService;

    @Autowired
    public RoutesApiController(SeaportDtoService seaportDtoService,
        SeaportConnectionRoutesDtoService seaportConnectionRoutesDtoService,
        RouteUrlSerializationService routeUrlSerializationService, EnricherDtoService enricherDtoService) {

        this.seaportDtoService = seaportDtoService;
        this.seaportConnectionRoutesDtoService = seaportConnectionRoutesDtoService;
        this.routeUrlSerializationService = routeUrlSerializationService;
        this.enricherDtoService = enricherDtoService;
    }

    /**
     * @deprecated  use {@link #getRoutes(BigInteger, double, double, boolean, ContainerType, boolean, RouteCombo)}
     *              instead
     */
    @Deprecated
    @ApiOperation(
        value = "Deprecated. Use /routes/{seaportuid}/{lat}:{lon}/{isroundtrip} instead.",
        notes = "Deprecated. Use /routes/{seaportuid}/{lat}:{lon}/{isroundtrip} instead."
    )
    @RequestMapping(value = "/connections/{seaportuid}/{lat}:{lon}/{isroundtrip}", method = GET)
    @ModelAttribute(ControllerConstants.RESPONSE)
    public RoutesResponse getSeaportRoutes(@PathVariable("seaportuid") BigInteger seaportUid,
        @PathVariable("lat") double latitude,
        @PathVariable("lon") double longitude,
        @PathVariable("isroundtrip") boolean isRoundTrip,
        @RequestParam(value = "containerType", required = false) ContainerType containerType,
        @RequestParam(value = "isImport", defaultValue = "true") boolean isImport,
        @RequestParam(value = "combo", defaultValue = "ALL") RouteCombo routeCombo) {

        return getRoutes(seaportUid, latitude, longitude, isRoundTrip, containerType, isImport, routeCombo);
    }


    @ApiOperation(
        value = "Returns a list of all possible connection routes between a seaport and a destination address.",
        notes = "Returns a list of all possible connection routes between a seaport and a destination address."
    )
    @RequestMapping(value = "/routes/{seaportuid}/{lat}:{lon}/{isroundtrip}", method = GET)
    @ModelAttribute(ControllerConstants.RESPONSE)
    public RoutesResponse getRoutes(@PathVariable("seaportuid") BigInteger seaportUid,
        @PathVariable("lat") double latitude,
        @PathVariable("lon") double longitude,
        @PathVariable("isroundtrip") boolean isRoundTrip,
        @RequestParam(value = "containerType", required = false) ContainerType containerType,
        @RequestParam(value = "isImport", defaultValue = "true") boolean isImport,
        @RequestParam(value = "combo", defaultValue = "ALL") RouteCombo routeCombo) {

        RouteInformation routeInformation = new RouteInformation(containerType, routeCombo, latitude, longitude,
                isRoundTrip, isImport);

        SeaportDto seaport = seaportDtoService.getByUid(seaportUid);

        List<RouteDto> routes = seaportConnectionRoutesDtoService.getAvailableSeaportConnectionRoutes(seaport,
                routeInformation);

        for (RouteDto route : routes) {
            routeUrlSerializationService.serializeUrl(route, ROUTE_DETAILS_URL, ROUTE_PART_DETAILS_URL);
        }

        RoutesResponse response = new RoutesResponse();

        response.add(linkTo(
                    methodOn(getClass()).getRoutes(seaportUid, latitude, longitude, isRoundTrip, containerType,
                        isImport, routeCombo)).withSelfRel());

        response.setRoutes(routes);

        LOG.info("API: Responding with {} connections for seaportsconnections-request: seaport {} to {}:{}"
            + " with isRoundtrip {} containerType {} and isimport {} ", routes.size(), seaportUid, latitude, longitude,
            isRoundTrip, containerType, isImport);

        return response;
    }


    @ApiOperation(value = "Returns a list of all possible routes to a location.")
    @RequestMapping(value = "/routes", method = GET)
    @ResponseBody
    public List<RoutingResultDto> getRoutesWithCoordinates(@RequestParam(value = "lat") double latitude,
        @RequestParam(value = "lon") double longitude) {

        RouteInformation routeInformation = new RouteInformation(new GeoLocation(BigDecimal.valueOf(latitude),
                    BigDecimal.valueOf(longitude)), ONEWAY, TWENTY_LIGHT, EXPORT, WATERWAY);

        return seaportDtoService.getAllActive()
            .stream()
            .map(s -> seaportConnectionRoutesDtoService.getAvailableSeaportConnectionRoutes(s, routeInformation))
            .flatMap(Collection::stream)
            .map(enricherDtoService::enrich)
            .map(this::toRoutingResultDto)
            .sorted(comparing(RoutingResultDto::getDistance))
            .collect(toList());
    }


    private RoutingResultDto toRoutingResultDto(RouteDto routeDto) {

        RouteDataDto data = routeDto.getData();
        BigDecimal bargeDistance = data.getParts().stream().filter(p -> p.getRouteType() == BARGE)
                .map(p -> p.getData().getBargeDieselDistance())
                .reduce(ZERO, BigDecimal::add);

        List<GeoLocationDto> stops = new ArrayList<>();
        stops.add(data.getParts().get(0).getOrigin());
        stops.addAll(data.getParts().stream().map(RoutePartDto::getDestination).collect(toList()));

        return new RoutingResultDto.Builder().withCo2(data.getCo2())
            .withCo2DirectTruck(data.getCo2DirectTruck())
            .withDistance(data.getTotalDistance())
            .withDuration(data.getTotalDuration())
            .withOnewayTruckDistance(data.getTotalOnewayTruckDistance())
            .withRealTollDistance(data.getTotalRealTollDistance())
            .withTollDistance(data.getTotalTollDistance())
            .withBargeDistance(bargeDistance)
            .withStops(stops)
            .build();
    }
}
