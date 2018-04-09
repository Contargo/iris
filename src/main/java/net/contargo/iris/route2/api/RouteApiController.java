package net.contargo.iris.route2.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route2.service.RoutePartEdgeResult;
import net.contargo.iris.route2.service.RouteService;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Api(value = "/route", description = "(Api-endpoint under development, may change)")
@RestController
public class RouteApiController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final RouteService routeService;
    private final ConversionService conversionService;

    @Autowired
    public RouteApiController(RouteService routeService, ConversionService conversionService) {

        this.routeService = routeService;
        this.conversionService = conversionService;
    }

    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<Void> handleException() {

        return new ResponseEntity<>(BAD_REQUEST);
    }


    @ApiOperation(
        notes = "distance in meters, co2 in kg and duration in seconds",
        value = "Performs a routing for the specified route description", response = RoutePartEdgeResult.class,
        responseContainer = "List"
    )
    @RequestMapping(value = "/route", method = POST)
    public ResponseEntity<List<RoutePartEdgeResultDto>> getRoute(@RequestBody List<RoutePartEdgeDto> edges) {

        LOG.info("Got request to route {}", edges);

        Function<RoutePartEdgeDto, RoutePartEdgeResult> route = e ->
                routeService.route(geoLocation(e.getStart()), geoLocation(e.getEnd()), e.getModeOfTransport());

        return new ResponseEntity<>(edges.stream()
                .map(route)
                .map(RoutePartEdgeResultDto::new)
                .collect(Collectors.toList()), OK);
    }


    private GeoLocation geoLocation(RoutePartNodeDto node) {

        return conversionService.convert(node, GeoLocation.class);
    }
}
