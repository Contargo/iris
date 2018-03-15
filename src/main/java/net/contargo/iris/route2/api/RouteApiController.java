package net.contargo.iris.route2.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.route2.service.RoutePartEdgeResult;
import net.contargo.iris.route2.service.RouteService;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@Api(value = "/route", description = "(Api-endpoint under development, may change)")
@RestController
public class RouteApiController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final RouteService routeService;

    @Autowired
    public RouteApiController(RouteService routeService) {

        this.routeService = routeService;
    }

    @ApiOperation(
        notes = "distance in meters, co2 in kg and duration in seconds",
        value = "Performs a routing for the specified route description", response = RoutePartEdgeResult.class,
        responseContainer = "List"
    )
    @RequestMapping(value = "/route", method = POST)
    public List<RoutePartEdgeResultDto> getRoute(@RequestBody List<RoutePartEdgeDto> route) {

        LOG.info("Requesting route {}", route);

        return route.stream().map(routeService::route).map(RoutePartEdgeResultDto::new).collect(toList());
    }
}
