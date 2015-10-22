package net.contargo.iris.terminal.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.api.ControllerConstants;
import net.contargo.iris.api.NotFoundException;
import net.contargo.iris.connection.dto.SeaportTerminalConnectionDtoService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.seaport.dto.SeaportDtoService;
import net.contargo.iris.terminal.dto.TerminalDto;
import net.contargo.iris.terminal.dto.TerminalDtoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
@Api(value = "/terminals", description = "API endpoint to manage terminal.")
@Controller
@RequestMapping(value = "/terminals")
public class TerminalApiController {

    private static final Logger LOG = LoggerFactory.getLogger(TerminalApiController.class);

    private final TerminalDtoService terminalDtoService;
    private final SeaportDtoService seaportDtoService;
    private final SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService;

    @Autowired
    public TerminalApiController(TerminalDtoService terminalDtoService, SeaportDtoService seaportDtoService,
        SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService) {

        this.terminalDtoService = terminalDtoService;
        this.seaportDtoService = seaportDtoService;
        this.seaportTerminalConnectionDtoService = seaportTerminalConnectionDtoService;
    }

    @ApiOperation(value = "Returns all active terminals.", notes = "Returns all active terminals.")
    @RequestMapping(method = GET)
    @ModelAttribute(ControllerConstants.RESPONSE)
    public TerminalsResponse getTerminals(
        @RequestParam(value = "activeOnly", defaultValue = "true") boolean activeOnly) {

        TerminalsResponse response = new TerminalsResponse();

        response.add(linkTo(methodOn(getClass()).getTerminals(activeOnly)).withSelfRel());

        List<TerminalDto> terminals;

        if (activeOnly) {
            terminals = terminalDtoService.getAllActive();
        } else {
            terminals = terminalDtoService.getAll();
        }

        response.setTerminals(terminals);

        LOG.info("API: Returning {} active terminals", terminals.size());

        return response;
    }


    @ApiOperation(
        value =
            "Returns all terminals that are part of a connection with the given routetype and the specified seaport.",
        notes =
            "Returns all terminals that are part of a connection with the given routetype and the specified seaport."
    )
    @RequestMapping(params = { "seaportUid", }, method = GET)
    @ModelAttribute(ControllerConstants.RESPONSE)
    public TerminalsResponse getTerminalsForSeaportAndRouteType(
        @RequestParam(value = "seaportUid") BigInteger seaportUID,
        @RequestParam(value = "routeType") RouteType routeType) {

        TerminalsResponse response = new TerminalsResponse();

        response.add(linkTo(methodOn(getClass()).getTerminalsForSeaportAndRouteType(seaportUID, routeType))
            .withSelfRel());

        SeaportDto seaportDto = seaportDtoService.getByUid(seaportUID);

        if (seaportDto == null) {
            response.setTerminals(Collections.<TerminalDto>emptyList());

            LOG.info("Cannnot find Seaport for UID {}", seaportUID);
        } else {
            List<TerminalDto> terminals =
                seaportTerminalConnectionDtoService.findTerminalsConnectedToSeaPortByRouteType(seaportDto, routeType);

            response.setTerminals(terminals);

            LOG.info("API: Returning {} terminals connected to seaport {} via {}", terminals.size(),
                seaportDto.getName(), routeType);
        }

        return response;
    }


    @ApiOperation(
        value = "Return the terminal with the given terminalUID.",
        notes = "Return the terminal with the given terminalUID."
    )
    @RequestMapping(value = "/{terminalUid}", method = GET)
    @ModelAttribute(ControllerConstants.RESPONSE)
    public TerminalResponse getTerminalByUid(@PathVariable("terminalUid") BigInteger uid) {

        TerminalResponse response = new TerminalResponse();

        response.add(linkTo(methodOn(getClass()).getTerminalByUid(uid)).withSelfRel());
        response.add(linkTo(methodOn(getClass()).getTerminals(true)).withRel("terminals"));

        TerminalDto t = terminalDtoService.getByUid(uid);

        if (t == null) {
            throw new NotFoundException("Cannot find Terminal for UID " + uid);
        }

        response.setTerminal(t);

        LOG.info("API: Returning terminal {}", t.getName());

        return response;
    }


    @ApiOperation(
        value = "Saves the terminal with the given terminalUID. Needs user role admin.",
        notes = "Saves the terminal with the given terminalUID. Needs user role admin.", response = Void.class
    )
    @RequestMapping(value = "/{terminalUid}", method = PUT)
    public ResponseEntity syncTerminal(@PathVariable("terminalUid") BigInteger terminalUid,
        @Valid @RequestBody TerminalDto terminalDto) {

        ResponseEntity response;

        boolean update = terminalDtoService.existsByUniqueId(terminalUid);

        if (update) {
            terminalDtoService.updateTerminal(terminalUid, terminalDto);

            LOG.info("API: Updating terminal with unique id {}", terminalUid);

            response = new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            terminalDto.setUniqueId(terminalUid.toString());

            terminalDtoService.save(terminalDto);

            LOG.info("API: Creating terminal with unique id {}", terminalUid);

            response = new ResponseEntity(HttpStatus.CREATED);
        }

        return response;
    }
}
