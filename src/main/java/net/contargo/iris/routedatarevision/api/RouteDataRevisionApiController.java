package net.contargo.iris.routedatarevision.api;

import com.mangofactory.swagger.annotations.ApiIgnore;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.api.RestApiErrorDto;
import net.contargo.iris.api.RestApiException;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.routedatarevision.service.RevisionDoesNotExistException;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.invoke.MethodHandles;

import java.util.Date;

import javax.validation.Valid;

import static net.contargo.iris.util.DateUtil.asLocalDate;

import static org.slf4j.LoggerFactory.getLogger;

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
 * @author  Oliver Messner - messner@synyx.de
 */
@Api(value = "/routerevisions", description = "API endpoint to retrieve routerevisions.")
@Controller
@RequestMapping(value = "/routerevisions")
public class RouteDataRevisionApiController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

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
            + "the route revision for the terminal belonging to the given terminalUid and valid for the given date.",
        notes = "Receives the correct route revision with the shortest distance between the requested Geolocation and "
            + "the route revision for the terminal belonging to the given terminalUid and valid for the given date.."
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
        @ApiIgnore GeoLocation geoLocation,
        @RequestParam(value = "date", required = false)
        @DateTimeFormat(pattern = RouteDataRevisionDto.DATE_FORMAT)
        Date date) {

        RouteDataRevisionDto dto = routeDataRevisionDtoService.findNearest(terminalUid, geoLocation, date);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @ApiOperation(
        value = "Creates a new route revision. Needs user role admin.",
        notes = "Creates a new route revision. Needs user role admin."
    )
    @RequestMapping(value = "", method = POST)
    public ResponseEntity<RouteDataRevisionDto> create(@Valid @RequestBody RouteDataRevisionDto revision) {

        LOG.debug("Processing POST request on route revision with id {}", revision.getId());

        revision.setId(null);

        return save(revision, CREATED);
    }


    @ApiOperation(
        value = "Replaces the route revision with the given id. Needs user role admin.",
        notes = "Replaces the route revision with the given id. Needs user role admin."
    )
    @RequestMapping(value = "/{id}", method = PUT)
    public ResponseEntity<RouteDataRevisionDto> update(@Valid @RequestBody RouteDataRevisionDto revision,
        @ApiParam(value = "ID identifying a route revision.", required = true)
        @PathVariable("id")
        long id) {

        LOG.debug("Processing PUT request on route revision with id {}", revision.getId());

        revision.setId(id);

        return save(revision, OK);
    }


    private ResponseEntity<RouteDataRevisionDto> save(RouteDataRevisionDto revision, HttpStatus statusCode) {

        ValidityRange validityRange;

        try {
            // may throw an IllegalArgumentException
            validityRange = new ValidityRange(asLocalDate(revision.getValidFrom()), asLocalDate(revision.getValidTo()));
        } catch (IllegalArgumentException e) {
            throw new RestApiException(messageSource.getMessage("routerevision.validityrange", null, getLocale()), e,
                "routerevision.validityrange", BAD_REQUEST);
        }

        if (routeDataRevisionDtoService.existsEntry(revision.getTerminal().getUniqueId(), revision.getLatitude(),
                    revision.getLongitude(), validityRange, revision.getId())) {
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
