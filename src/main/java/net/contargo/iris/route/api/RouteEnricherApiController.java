package net.contargo.iris.route.api;

import com.mangofactory.swagger.annotations.ApiIgnore;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.api.ControllerConstants;
import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.route.dto.EnricherDtoService;
import net.contargo.iris.security.UserAuthenticationService;
import net.contargo.iris.terminal.dto.TerminalDto;
import net.contargo.iris.terminal.dto.TerminalDtoService;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.invoke.MethodHandles;

import java.math.BigInteger;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Api(value = "/routedetails", description = "Api to get enriched routes")
@Controller
@RequestMapping("/routedetails")
public class RouteEnricherApiController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final EnricherDtoService enricherDtoService;
    private final TerminalDtoService terminalDtoService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    RouteEnricherApiController(EnricherDtoService enricherDtoService, TerminalDtoService terminalDtoService,
        UserAuthenticationService userAuthenticationService) {

        this.enricherDtoService = enricherDtoService;
        this.terminalDtoService = terminalDtoService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @ApiOperation(
        value = "Takes a route and returns it enriched with additional information.",
        notes = "Takes a route and returns it enriched with additional information. The route consists of an arbitrary "
            + "number of route parts which are passed as request params.\n\n"
            + "Example usage:\n\n"
            + "/api/routedetails?data.parts[0].origin.latitude=51.39119&data.parts[0].origin.longitude=6.72873&\n"
            + "data.parts[0].destination.latitude=51.36833&data.parts[0].destination.longitude=4.3&\n"
            + "data.parts[0].containerType=TWENTY_LIGHT&data.parts[0].containerState=FULL&\n"
            + "data.parts[0].routeType=TRUCK&data.parts[1].origin.latitude=51.36833&\n"
            + "data.parts[1].origin.longitude=4.3&data.parts[1].destination.latitude=51.39119&\n"
            + "data.parts[1].destination.longitude=6.72873&data.parts[1].containerType=TWENTY_LIGHT&\n"
            + "data.parts[1].containerState=FULL&data.parts[1].routeType=TRUCK"
    )
    @RequestMapping(method = RequestMethod.GET)
    @ModelAttribute(ControllerConstants.RESPONSE)
    public RouteResponse getEnrichedRoute(@ApiIgnore RouteDto route,
        @RequestParam(value = "terminal", required = false)
        @ApiIgnore String terminalUid, Model model) {

        model.asMap().remove("routeDto");

        if (terminalUid != null) {
            TerminalDto terminal = terminalDtoService.getByUid(new BigInteger(terminalUid));
            route.setResponsibleTerminal(terminal);
        }

        RouteResponse response = new RouteResponse();
        response.setRoute(enricherDtoService.enrich(route));
        response.add(linkTo(getClass()).withSelfRel());

        Authentication authentication = userAuthenticationService.getCurrentUser();

        LOG.info("API: Responding routedetails with a route with {} parts. Route name is {} - user: {}",
            response.getRoute().size(), response.getRoute().getName(), authentication.getName());

        return response;
    }
}
