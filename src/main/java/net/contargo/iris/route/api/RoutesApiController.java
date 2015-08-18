package net.contargo.iris.route.api;

import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.api.ControllerConstants;
import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.connection.dto.SeaportConnectionRoutesDtoService;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.RouteCombo;
import net.contargo.iris.route.RouteInformation;
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

import java.lang.invoke.MethodHandles;

import java.math.BigInteger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Controller
public class RoutesApiController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final String ROUTE_DETAILS_URL = "/routedetails";
    private static final String ROUTE_PART_DETAILS_URL = "/routepartdetails";

    private final SeaportDtoService seaportDtoService;
    private final SeaportConnectionRoutesDtoService seaportConnectionRoutesDtoService;
    private final RouteUrlSerializationService routeUrlSerializationService;

    @Autowired
    public RoutesApiController(SeaportDtoService seaportDtoService,
        SeaportConnectionRoutesDtoService seaportConnectionRoutesDtoService,
        RouteUrlSerializationService routeUrlSerializationService) {

        this.seaportDtoService = seaportDtoService;
        this.seaportConnectionRoutesDtoService = seaportConnectionRoutesDtoService;
        this.routeUrlSerializationService = routeUrlSerializationService;
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
}
