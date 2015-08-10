package net.contargo.iris.routedatarevision.api;

import com.mangofactory.swagger.annotations.ApiIgnore;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.api.RestApiErrorDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.routedatarevision.service.RevisionDoesNotExistException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Api(value = "/routerevisions", description = "API endpoint to retrieve routerevisions.")
@Controller
@RequestMapping(value = "/routerevisions")
public class RouteDataRevisionApiController {

    private final RouteDataRevisionDtoService routeDataRevisionDtoService;

    @Autowired
    public RouteDataRevisionApiController(RouteDataRevisionDtoService routeDataRevisionDtoService) {

        this.routeDataRevisionDtoService = routeDataRevisionDtoService;
    }

    @ApiOperation(
        value = "Receives the correct route revision with the shortest distance between the requested Geolocation and "
            + "the route revision for the terminal belonging to the given terminalUid.",
        notes = "Receives the correct route revision with the shortest distance between the requested Geolocation and "
            + "the route revision for the terminal belonging to the given terminalUid."
    )
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "latitude", dataType = "String", required = true),
            @ApiImplicitParam(
                name = "longitude", dataType = "String", required = true
            )
        }
    )
    @RequestMapping(value = "", method = GET, params = { "terminalUid", "latitude", "longitude" })
    public ResponseEntity<RouteDataRevisionDto> get(@RequestParam("terminalUid") String terminalUid,
        @ApiIgnore GeoLocation geoLocation) {

        RouteDataRevisionDto dto = routeDataRevisionDtoService.findNearest(terminalUid, geoLocation);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @ExceptionHandler(RevisionDoesNotExistException.class)
    ResponseEntity<RestApiErrorDto> handleRevisionDoesNotExistException(RevisionDoesNotExistException e) {

        return new ResponseEntity<>(new RestApiErrorDto(e.getCode(), e.getMessage()), NOT_FOUND);
    }
}
