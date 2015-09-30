package net.contargo.iris.routedatarevision.api;

import com.mangofactory.swagger.annotations.ApiIgnore;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.api.RestApiErrorDto;
import net.contargo.iris.api.RestApiException;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.routedatarevision.service.RevisionDoesNotExistException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import static net.contargo.iris.util.DateUtil.asLocalDate;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Api(value = "/routerevisions", description = "API endpoint to retrieve routerevisions.")
@Controller
@RequestMapping(value = "/routerevisions")
public class RouteDataRevisionApiController {

    private final RouteDataRevisionDtoService routeDataRevisionDtoService;
    private final MessageSource messageSource;

    @Autowired
    public RouteDataRevisionApiController(RouteDataRevisionDtoService routeDataRevisionDtoService,
        MessageSource messageSource) {

        this.routeDataRevisionDtoService = routeDataRevisionDtoService;
        this.messageSource = messageSource;
    }

    @ApiOperation(
        value = "Receives the correct route revision with the shortest distance between the requested Geolocation and "
            + "the route revision for the terminal belonging to the given terminalUid.",
        notes = "Receives the correct route revision with the shortest distance between the requested Geolocation and "
            + "the route revision for the terminal belonging to the given terminalUid."
    )
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "latitude", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(
                name = "longitude", dataType = "String", required = true, paramType = "query"
            )
        }
    )
    @RequestMapping(value = "", method = GET, params = { "terminalUid", "latitude", "longitude" })
    public ResponseEntity<RouteDataRevisionDto> get(@RequestParam("terminalUid") String terminalUid,
        @ApiIgnore GeoLocation geoLocation) {

        RouteDataRevisionDto dto = routeDataRevisionDtoService.findNearest(terminalUid, geoLocation);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @ApiOperation(value = "Creates a new route revision.", notes = "Creates a new route revision.")
    @RequestMapping(value = "", method = POST)
    public ResponseEntity<RouteDataRevisionDto> create(@Valid @RequestBody RouteDataRevisionDto revision) {

        revision.setId(null);

        return save(revision, CREATED);
    }


    @ApiOperation(
        value = "Replaces the route revision with the given id.",
        notes = "Replaces the route revision with the given id."
    )
    @RequestMapping(value = "/{id}", method = PUT)
    public ResponseEntity<RouteDataRevisionDto> update(@Valid @RequestBody RouteDataRevisionDto revision) {

        return save(revision, OK);
    }


    private ResponseEntity<RouteDataRevisionDto> save(RouteDataRevisionDto revision, HttpStatus statusCode) {

        ValidityRange validityRange;

        try {
            validityRange = new ValidityRange(asLocalDate(revision.getValidFrom()), asLocalDate(revision.getValidTo())); // may throw an IllegalArgumentException
        } catch (IllegalArgumentException e) {
            throw new RestApiException(messageSource.getMessage("routerevision.validityrange", null, getLocale()),
                "routerevision.validityrange", BAD_REQUEST);
        }

        if (routeDataRevisionDtoService.existsEntry(revision.getTerminal().getUniqueId(), revision.getLatitude(),
                    revision.getLongitude(), validityRange, null)) {
            throw new RestApiException(messageSource.getMessage("routerevision.exists", null, getLocale()),
                "routerevision.exists", BAD_REQUEST);
        }

        RouteDataRevisionDto savedRevision = routeDataRevisionDtoService.save(revision);

        return new ResponseEntity<>(savedRevision, statusCode);
    }


    @ExceptionHandler(RevisionDoesNotExistException.class)
    ResponseEntity<RestApiErrorDto> handleRevisionDoesNotExistException(RevisionDoesNotExistException e) {

        return new ResponseEntity<>(new RestApiErrorDto(e.getCode(), e.getMessage()), NOT_FOUND);
    }
}
