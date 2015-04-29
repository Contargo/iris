package net.contargo.iris.routedatarevision.web;

import net.contargo.iris.Message;
import net.contargo.iris.api.AbstractController;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
public class RouteDataRevisionController extends AbstractController {

    private static final Message SAVE_SUCCESS_MESSAGE = Message.success("routerevision.success.save.message");
    private static final Message UPDATE_SUCCESS_MESSAGE = Message.success("routerevision.success.update.message");

    private static final String CONTROLLER_CONTEXT = "routeRevisionManagement/";

    private final RouteDataRevisionDtoService routeDataRevisionDtoService;
    private final TerminalService terminalService;

    @Autowired
    public RouteDataRevisionController(RouteDataRevisionDtoService routeDataRevisionDtoService,
        TerminalService terminalService) {

        this.routeDataRevisionDtoService = routeDataRevisionDtoService;
        this.terminalService = terminalService;
    }

    @RequestMapping(value = "", method = GET)
    public String getAll(Model model) {

        model.addAttribute("routeRevisions", routeDataRevisionDtoService.getRouteDataRevisions());

        return CONTROLLER_CONTEXT + "routeRevisions";
    }


    @RequestMapping(value = "/new", method = GET)
    public String createSkeleton(Model model) {

        model.addAttribute("routeRevision", new RouteDataRevisionDto());
        addCollectionsToModel(model);

        return CONTROLLER_CONTEXT + "routeRevision";
    }


    @RequestMapping(value = "/{id}", method = GET)
    public String get(@PathVariable Long id, Model model) {

        model.addAttribute("routeRevision", routeDataRevisionDtoService.getRouteDataRevision(id));
        addCollectionsToModel(model);

        return CONTROLLER_CONTEXT + "routeRevision";
    }


    @RequestMapping(value = "", method = POST)
    public String create(@Valid
        @ModelAttribute("routeRevision")
        RouteDataRevisionDto routeDataRevisionDto, BindingResult result, RedirectAttributes redirectAttributes,
        Model model) {

        return createOrUpdate(routeDataRevisionDto, redirectAttributes, result, model, SAVE_SUCCESS_MESSAGE);
    }


    @RequestMapping(value = "/{id}", method = PUT)
    public String update(@Valid
        @ModelAttribute("routeRevision")
        RouteDataRevisionDto routeDataRevisionDto, BindingResult result, RedirectAttributes redirectAttributes,
        Model model) {

        return createOrUpdate(routeDataRevisionDto, redirectAttributes, result, model, UPDATE_SUCCESS_MESSAGE);
    }


    private String createOrUpdate(RouteDataRevisionDto routeDataRevisionDto, RedirectAttributes redirectAttributes,
        BindingResult result, Model model, Message successMessage) {

        if (routeDataRevisionDtoService.existsEntry(routeDataRevisionDto.getTerminal(),
                    routeDataRevisionDto.getLatitude(), routeDataRevisionDto.getLongitude())) {
            result.rejectValue("terminal.id", "routerevision.exists");
            result.rejectValue("longitude", "routerevision.exists");
            result.rejectValue("latitude", "routerevision.exists");
        }

        if (result.hasErrors()) {
            model.addAttribute("routeRevision", routeDataRevisionDto);
            model.addAttribute("terminals", terminalService.getAll());

            return CONTROLLER_CONTEXT + "routeRevision";
        }

        RouteDataRevisionDto savedDto = routeDataRevisionDtoService.save(routeDataRevisionDto);

        redirectAttributes.addFlashAttribute(MESSAGE, successMessage);

        return "redirect:/web/routerevisions/" + savedDto.getId();
    }


    private void addCollectionsToModel(Model model) {

        model.addAttribute("terminals", terminalService.getAll());
    }
}
