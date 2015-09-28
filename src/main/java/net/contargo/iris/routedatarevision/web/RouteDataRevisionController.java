package net.contargo.iris.routedatarevision.web;

import net.contargo.iris.Message;
import net.contargo.iris.api.ControllerConstants;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * Controller for the Route Data Revision overview management page.
 *
 * @author  David Schilling - schilling@synyx.de
 */
@Controller
@RequestMapping("/routerevisions")
public class RouteDataRevisionController {

    private static final Message SAVE_SUCCESS_MESSAGE = Message.success("routerevision.success.save.message");
    private static final Message UPDATE_SUCCESS_MESSAGE = Message.success("routerevision.success.update.message");

    private static final String CONTROLLER_CONTEXT = "routeRevisionManagement/";
    private static final String ROUTE_REVISION = "routeRevision";

    private final RouteDataRevisionDtoService routeDataRevisionDtoService;
    private final TerminalService terminalService;

    @Autowired
    public RouteDataRevisionController(RouteDataRevisionDtoService routeDataRevisionDtoService,
        TerminalService terminalService) {

        this.routeDataRevisionDtoService = routeDataRevisionDtoService;
        this.terminalService = terminalService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(RouteDataRevisionDto.DATE_FORMAT);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    @RequestMapping(value = "", method = GET)
    public String getAll(Model model,
        @RequestParam(value = "terminalId", required = false) Long terminalId) {

        List<RouteDataRevisionDto> routeDataRevisions;

        if (terminalId == null) {
            routeDataRevisions = routeDataRevisionDtoService.getRouteDataRevisions();
        } else {
            routeDataRevisions = routeDataRevisionDtoService.getRouteDataRevisions(terminalId);
            model.addAttribute("selectedTerminal", terminalId);
        }

        model.addAttribute("routeRevisions", routeDataRevisions);
        model.addAttribute("terminals", terminalService.getAll());

        return CONTROLLER_CONTEXT + "routeRevisions";
    }


    @RequestMapping(value = "/new", method = GET)
    public String createSkeleton(Model model) {

        model.addAttribute(ROUTE_REVISION, new RouteDataRevisionDto());
        addCollectionsToModel(model);

        return CONTROLLER_CONTEXT + ROUTE_REVISION;
    }


    @RequestMapping(value = "/{id}", method = GET)
    public String get(@PathVariable Long id, Model model) {

        model.addAttribute(ROUTE_REVISION, routeDataRevisionDtoService.getRouteDataRevision(id));
        addCollectionsToModel(model);

        return CONTROLLER_CONTEXT + ROUTE_REVISION;
    }


    @RequestMapping(value = "", method = POST)
    public String create(@Valid
        @ModelAttribute(ROUTE_REVISION)
        RouteDataRevisionDto routeDataRevisionDto, BindingResult result, RedirectAttributes redirectAttributes,
        Model model) {

        return save(routeDataRevisionDto, redirectAttributes, result, model, SAVE_SUCCESS_MESSAGE);
    }


    @RequestMapping(value = "/{id}", method = PUT)
    public String update(@Valid
        @ModelAttribute(ROUTE_REVISION)
        RouteDataRevisionDto routeDataRevisionDto, BindingResult result, RedirectAttributes redirectAttributes,
        Model model) {

        return save(routeDataRevisionDto, redirectAttributes, result, model, UPDATE_SUCCESS_MESSAGE);
    }


    private String save(RouteDataRevisionDto routeDataRevisionDto, RedirectAttributes redirectAttributes,
        BindingResult result, Model model, Message successMessage) {

        if (routeDataRevisionDto.getId() == null
                && routeDataRevisionDtoService.existsEntry(routeDataRevisionDto.getTerminal().getUniqueId(),
                    routeDataRevisionDto.getLatitude(), routeDataRevisionDto.getLongitude(),
                    routeDataRevisionDto.getValidFrom(), routeDataRevisionDto.getValidTo())) {
            result.rejectValue("terminal.uniqueId", "routerevision.exists");
            result.rejectValue("longitude", "routerevision.exists");
            result.rejectValue("latitude", "routerevision.exists");
            result.rejectValue("validFrom", "routerevision.exists");
            result.rejectValue("validTo", "routerevision.exists");
        }

        if (result.hasErrors()) {
            model.addAttribute(ROUTE_REVISION, routeDataRevisionDto);
            model.addAttribute("terminals", terminalService.getAll());

            return CONTROLLER_CONTEXT + ROUTE_REVISION;
        }

        RouteDataRevisionDto savedDto = routeDataRevisionDtoService.save(routeDataRevisionDto);

        redirectAttributes.addFlashAttribute(ControllerConstants.MESSAGE, successMessage);

        return "redirect:/web/routerevisions/" + savedDto.getId();
    }


    private void addCollectionsToModel(Model model) {

        model.addAttribute("terminals", terminalService.getAll());
    }
}
