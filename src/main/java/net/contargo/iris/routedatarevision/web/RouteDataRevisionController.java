package net.contargo.iris.routedatarevision.web;

import net.contargo.iris.Message;
import net.contargo.iris.routedatarevision.RouteRevisionRequest;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.routedatarevision.service.cleanup.RouteDataRevisionCleanupTask;
import net.contargo.iris.terminal.service.TerminalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.invoke.MethodHandles;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import static net.contargo.iris.Message.success;
import static net.contargo.iris.util.DateUtil.asLocalDate;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * Controller for the Route Data Revision overview management page.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Controller
@RequestMapping("/routerevisions")
public class RouteDataRevisionController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Message SAVE_SUCCESS_MESSAGE = success("routerevision.success.save.message");
    private static final Message UPDATE_SUCCESS_MESSAGE = success("routerevision.success.update.message");

    private static final String CONTROLLER_CONTEXT = "routeRevisionManagement/";
    private static final String ROUTE_REVISION = "routeRevision";
    private static final String CLEANUP = "cleanup";
    private static final String ROUTEREVISION_EXISTS = "routerevision.exists";
    private static final String ROUTEREVISION_VALIDITYRANGE = "routerevision.validityrange";
    private static final String CLEANUP_REQUEST = "cleanupRequest";

    private final RouteDataRevisionDtoService routeDataRevisionDtoService;
    private final TerminalService terminalService;
    private final RouteDataRevisionCleanupTask cleanupTask;

    @Autowired
    public RouteDataRevisionController(RouteDataRevisionDtoService routeDataRevisionDtoService,
        TerminalService terminalService, RouteDataRevisionCleanupTask cleanupTask) {

        this.routeDataRevisionDtoService = routeDataRevisionDtoService;
        this.terminalService = terminalService;
        this.cleanupTask = cleanupTask;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(RouteDataRevisionDto.DATE_FORMAT, getLocale());
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    @RequestMapping(method = GET)
    public String search(Model model, @ModelAttribute RouteRevisionRequest routeRevisionRequest) {

        if (!routeRevisionRequest.isEmpty()) {
            if (routeRevisionRequest.isValid()) {
                List<RouteDataRevisionDto> routeRevisions = routeDataRevisionDtoService.search(routeRevisionRequest);

                model.addAttribute("routeRevisions", routeRevisions);
            } else {
                model.addAttribute("message", Message.error("routerevision.parameter.count"));
            }
        }

        model.addAttribute("terminals", terminalService.getAll());
        model.addAttribute("request", routeRevisionRequest);

        return CONTROLLER_CONTEXT + "routeRevisions";
    }


    @RequestMapping(value = "/new", method = GET)
    public String createSkeleton(Model model) {

        model.addAttribute(ROUTE_REVISION, new RouteDataRevisionDto());
        addCollectionsToModel(model);

        return CONTROLLER_CONTEXT + ROUTE_REVISION;
    }


    @RequestMapping(value = "/cleanup", method = GET)
    public String cleanupForm(Model model) {

        model.addAttribute(CLEANUP_REQUEST, new RouteDataRevisionCleanupRequest());

        return CONTROLLER_CONTEXT + CLEANUP;
    }


    @RequestMapping(value = "/cleanup", method = POST)
    public String cleanup(@Valid
        @ModelAttribute(CLEANUP_REQUEST)
        RouteDataRevisionCleanupRequest cleanupRequest, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute(CLEANUP_REQUEST, cleanupRequest);

            return CONTROLLER_CONTEXT + CLEANUP;
        }

        if (cleanupTask.isRunning()) {
            model.addAttribute("message", Message.error("A cleanup is already running"));
            model.addAttribute(CLEANUP_REQUEST, cleanupRequest);
        } else {
            model.addAttribute("message",
                success("Cleanup is running, report will be sent to " + cleanupRequest.getEmail()));
            cleanupTask.submit(cleanupRequest);
        }

        return CONTROLLER_CONTEXT + CLEANUP;
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

        if (routeDataRevisionDto.getValidFrom() != null) {
            try {
                // may throw an IllegalArgumentException
                ValidityRange validityRange = new ValidityRange(asLocalDate(routeDataRevisionDto.getValidFrom()),
                        asLocalDate(routeDataRevisionDto.getValidTo()));

                if (routeDataRevisionDtoService.existsEntry(routeDataRevisionDto.getTerminal().getUniqueId(),
                            routeDataRevisionDto.getLatitude(), routeDataRevisionDto.getLongitude(), validityRange,
                            routeDataRevisionDto.getId())) {
                    result.rejectValue("terminal.uniqueId", ROUTEREVISION_EXISTS);
                    result.rejectValue("longitude", ROUTEREVISION_EXISTS);
                    result.rejectValue("latitude", ROUTEREVISION_EXISTS);
                    result.rejectValue("validFrom", ROUTEREVISION_EXISTS);
                    result.rejectValue("validTo", ROUTEREVISION_EXISTS);
                }
            } catch (IllegalArgumentException e) {
                result.rejectValue("validTo", ROUTEREVISION_VALIDITYRANGE);
                result.rejectValue("validFrom", ROUTEREVISION_VALIDITYRANGE);
            }
        }

        if (result.hasErrors()) {
            model.addAttribute(ROUTE_REVISION, routeDataRevisionDto);
            model.addAttribute("terminals", terminalService.getAll());

            return CONTROLLER_CONTEXT + ROUTE_REVISION;
        }

        RouteDataRevisionDto savedDto = routeDataRevisionDtoService.save(routeDataRevisionDto);

        redirectAttributes.addFlashAttribute(Message.MESSAGE, successMessage);

        return "redirect:/web/routerevisions/" + savedDto.getId();
    }


    private void addCollectionsToModel(Model model) {

        model.addAttribute("terminals", terminalService.getAll());
    }
}
