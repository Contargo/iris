package net.contargo.iris.connection.api;

import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.connection.dto.SeaportTerminalConnectionDtoService;
import net.contargo.iris.route.RouteCombo;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.dto.SeaportDto;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.invoke.MethodHandles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.contargo.iris.route.RouteCombo.ALL;
import static net.contargo.iris.route.RouteType.DTRUCK;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import static java.util.Arrays.asList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@Controller
@RequestMapping(value = "/connections")
public class SeaportConnectionApiController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService;

    private final boolean dtruckActive;

    @Autowired
    public SeaportConnectionApiController(SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService,
        @Value("${feature.dtruck}") boolean dtruckActive) {

        this.seaportTerminalConnectionDtoService = seaportTerminalConnectionDtoService;
        this.dtruckActive = dtruckActive;
    }

    @ApiOperation(
        value = "Returns all seaports that have a connection of a given route-combo.",
        notes = "Returns all seaports that have a connection of a given route-combo."
    )
    @RequestMapping(value = "/seaports", method = GET)
    @ResponseBody
    public SeaportsResponse getSeaportsInConnections(
        @RequestParam(value = "combo", defaultValue = "ALL") RouteCombo routeCombo) {

        SeaportsResponse response = new SeaportsResponse();

        response.add(linkTo(methodOn(getClass()).getSeaportsInConnections(routeCombo)).withSelfRel());

        Set<SeaportDto> ports = new HashSet<>();

        List<RouteType> routeTypes = new ArrayList<>(asList(routeCombo.getRouteTypes()));

        if (routeCombo == ALL) {
            routeTypes.add(DTRUCK);
        }

        for (RouteType t : routeTypes) {
            if (t == DTRUCK) {
                if (dtruckActive) {
                    ports.addAll(seaportTerminalConnectionDtoService.findSeaportsConnectedByRouteType(t));
                }
            } else {
                ports.addAll(seaportTerminalConnectionDtoService.findSeaportsConnectedByRouteType(t));
            }
        }

        response.setSeaports(ports);

        LOG.info("API: Returning {} active seaports in connections with combo {}", ports.size(), routeCombo);

        return response;
    }
}
