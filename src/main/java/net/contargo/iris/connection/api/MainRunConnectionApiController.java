package net.contargo.iris.connection.api;

import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.api.AbstractController;
import net.contargo.iris.connection.dto.MainRunConnectionDto;
import net.contargo.iris.connection.dto.MainRunConnectionDtoService;
import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.connection.dto.SeaportConnectionRoutesDtoService;
import net.contargo.iris.connection.dto.SeaportTerminalConnectionDtoService;
import net.contargo.iris.connection.dto.SimpleMainRunConnectionDto;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.RouteCombo;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.seaport.dto.SeaportDtoService;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.invoke.MethodHandles;

import java.math.BigInteger;

import java.net.URI;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * Public API controller that responds to request for {@link net.contargo.iris.connection.MainRunConnection}s.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Controller
@RequestMapping(value = "/connections")
public class MainRunConnectionApiController extends AbstractController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private static final String ROUTE_DETAILS_URL = "/routedetails";
    private static final String ROUTE_PART_DETAILS_URL = "/routepartdetails";

    private final SeaportDtoService seaportDtoService;
    private final SeaportConnectionRoutesDtoService seaportConnectionRoutesDtoService;
    private final RouteUrlSerializationService routeUrlSerializationService;
    private final MainRunConnectionDtoService connectionApiDtoService;
    private final SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService;

    @Autowired
    public MainRunConnectionApiController(SeaportDtoService seaportDtoService,
        SeaportConnectionRoutesDtoService seaportConnectionRoutesDtoService,
        RouteUrlSerializationService routeUrlSerializationService, MainRunConnectionDtoService connectionApiDtoService,
        SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService) {

        this.seaportDtoService = seaportDtoService;
        this.seaportConnectionRoutesDtoService = seaportConnectionRoutesDtoService;
        this.routeUrlSerializationService = routeUrlSerializationService;
        this.connectionApiDtoService = connectionApiDtoService;
        this.seaportTerminalConnectionDtoService = seaportTerminalConnectionDtoService;
    }

    @ApiOperation(
        value = "Returns a list of all possible connection routes between a seaport and a destination address.",
        notes = "Returns a list of all possible connection routes between a seaport and a destination address."
    )
    @RequestMapping(value = SLASH + "{seaportuid}" + SLASH + "{lat}:{lon}" + SLASH + "{isroundtrip}", method = GET)
    @ModelAttribute(RESPONSE)
    public RoutesResponse getSeaportRoutes(@PathVariable("seaportuid") BigInteger seaportUid,
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
                    methodOn(getClass()).getSeaportRoutes(seaportUid, latitude, longitude, isRoundTrip, containerType,
                        isImport, routeCombo)).withSelfRel());

        response.setRoutes(routes);

        LOG.info("API: Responding with {} connections for seaportsconnections-request: seaport {} to {}:{}"
            + " with isRoundtrip {} containerType {} and isimport {} ", routes.size(), seaportUid, latitude, longitude,
            isRoundTrip, containerType, isImport);

        return response;
    }


    @ApiOperation(
        value = "Returns all seaports that have a connection of a given route-combo.",
        notes = "Returns all seaports that have a connection of a given route-combo."
    )
    @RequestMapping(value = SLASH + "seaports", method = GET)
    @ResponseBody
    public SeaportsResponse getSeaportsInConnections(
        @RequestParam(value = "combo", defaultValue = "ALL") RouteCombo routeCombo) {

        SeaportsResponse response = new SeaportsResponse();

        response.add(linkTo(methodOn(getClass()).getSeaportsInConnections(routeCombo)).withSelfRel());

        Set<SeaportDto> ports = new HashSet<>();

        for (RouteType t : routeCombo.getRouteTypes()) {
            ports.addAll(seaportTerminalConnectionDtoService.findSeaportsConnectedByRouteType(t));
        }

        response.setSeaports(ports);

        LOG.info("API: Returning {} active seaports in connections with combo {}", ports.size(), routeCombo);

        return response;
    }


    @ApiOperation(
        value = "Returns all Connections containing the given terminal",
        notes = "Returns all Connections containing the given terminal", response = SimpleMainRunConnectionDto.class,
        responseContainer = "List"
    )
    @RequestMapping(method = GET, params = "terminalUid", produces = "application/json")
    @ResponseBody
    public Collection<SimpleMainRunConnectionDto> getConnectionsForTerminal(
        @RequestParam("terminalUid") BigInteger terminalUid) {

        LOG.info("API: client requests connections for terminal UID: " + terminalUid);

        return connectionApiDtoService.getConnectionsForTerminal(terminalUid);
    }


    @RequestMapping(method = GET, value = "/{id}")
    public ResponseEntity<MainRunConnectionDto> getConnection(@PathVariable Long id) {

        MainRunConnectionDto dto = connectionApiDtoService.get(id);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @RequestMapping(method = POST, value = "")
    public ResponseEntity createConnection(@RequestBody MainRunConnectionDto dto) {

        MainRunConnectionDto savedDto = connectionApiDtoService.save(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
            .path("/../web/connections/{id}")
            .build()
            .expand(savedDto.getId())
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<MainRunConnectionDto> updateConnection(@RequestBody MainRunConnectionDto dto) {

        MainRunConnectionDto updatedDto = connectionApiDtoService.save(dto);

        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }
}
