package net.contargo.iris.connection.web;

import net.contargo.iris.Message;
import net.contargo.iris.api.AbstractController;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

import javax.validation.Valid;

import static net.contargo.iris.api.AbstractController.CONNECTIONS;
import static net.contargo.iris.api.AbstractController.SLASH;


/**
 * Controller to handle {@link net.contargo.iris.connection.MainRunConnection}'s.
 *
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Controller
@RequestMapping(SLASH + CONNECTIONS)
public class MainRunConnectionController extends AbstractController {

    private static final String CONTROLLER_CONTEXT = "connectionManagement" + SLASH;

    private static final String SEAPORTS_ATTRIBUTE = SEAPORTS;
    private static final String TERMINALS_ATTRIBUTE = TERMINALS;
    private static final String ROUTE_TYPES_ATTRIBUTE = ROUTE_TYPES;

    private static final String MAINRUN_CONNECTIONS_ATTRIBUTE = "mainRunConnections";
    private static final String MAINRUN_CONNECTION_ATTRIBUTE = "mainRunConnection";

    private static final String MAINRUN_CONNECTIONS_VIEW = "mainrunconnections";
    private static final String MAINRUN_CONNECTION_VIEW = "mainrunconnection";

    private static final Message SAVE_SUCCESS_MESSAGE = Message.success("mainrunconnection.success.save.message");
    private static final Message EDIT_SUCCESS_MESSAGE = Message.success("mainrunconnection.success.edit.message");

    private final MainRunConnectionService mainRunConnectionService;
    private final TerminalService terminalService;
    private final SeaportService seaportService;

    @Autowired
    public MainRunConnectionController(MainRunConnectionService mainRunConnectionService,
        TerminalService terminalService, SeaportService seaportService) {

        this.mainRunConnectionService = mainRunConnectionService;
        this.terminalService = terminalService;
        this.seaportService = seaportService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAllConnections(Model model) {

        model.addAttribute(MAINRUN_CONNECTIONS_ATTRIBUTE, mainRunConnectionService.getAll());

        return CONTROLLER_CONTEXT + MAINRUN_CONNECTIONS_VIEW;
    }


    @RequestMapping(value = SLASH + "new", method = RequestMethod.GET)
    public String prepareForCreate(Model model) {

        model.addAttribute(MAINRUN_CONNECTION_ATTRIBUTE, new MainRunConnection());
        addCollectionsForDropdowns(model);

        return CONTROLLER_CONTEXT + MAINRUN_CONNECTION_VIEW;
    }


    @RequestMapping(value = SLASH, method = RequestMethod.POST)
    public String createConnection(@Valid @ModelAttribute MainRunConnection mainRunConnection, BindingResult result,
        Model model, RedirectAttributes redirectAttributes) {

        if (mainRunConnectionService.isAlreadyApplied(mainRunConnection)) {
            model.addAttribute(MAINRUN_CONNECTION_ATTRIBUTE, mainRunConnection);
            addCollectionsForDropdowns(model);

            result.rejectValue("seaport.id", "mainrunconnection.duplicate.connection.seaport");
            result.rejectValue("terminal.id", "mainrunconnection.duplicate.connection.terminal");
            result.rejectValue("routeType", "mainrunconnection.duplicate.connection.routetype");

            return CONTROLLER_CONTEXT + MAINRUN_CONNECTION_VIEW;
        }

        return createOrUpdate(model, result, mainRunConnection, redirectAttributes, SAVE_SUCCESS_MESSAGE);
    }


    @RequestMapping(value = SLASH + ID_PARAM, method = RequestMethod.GET)
    public String prepareForUpdate(@PathVariable Long id, Model model) {

        model.addAttribute(MAINRUN_CONNECTION_ATTRIBUTE, mainRunConnectionService.getById(id));
        addCollectionsForDropdowns(model);

        return CONTROLLER_CONTEXT + MAINRUN_CONNECTION_VIEW;
    }


    @RequestMapping(value = SLASH + ID_PARAM, method = RequestMethod.PUT)
    public String updateConnection(@Valid @ModelAttribute MainRunConnection mainRunConnection, BindingResult result,
        @PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

        mainRunConnection.setId(mainRunConnectionService.getById(id).getId());

        if (mainRunConnectionService.isAlreadyAppliedAndNotThis(mainRunConnection)) {
            model.addAttribute(MAINRUN_CONNECTION_ATTRIBUTE, mainRunConnection);
            addCollectionsForDropdowns(model);

            result.rejectValue("seaport.id", "mainrunconnection.duplicate.connection.seaport");
            result.rejectValue("terminal.id", "mainrunconnection.duplicate.connection.terminal");
            result.rejectValue("routeType", "mainrunconnection.duplicate.connection.routetype");

            return CONTROLLER_CONTEXT + MAINRUN_CONNECTION_VIEW;
        }

        return createOrUpdate(model, result, mainRunConnection, redirectAttributes, EDIT_SUCCESS_MESSAGE);
    }


    private String createOrUpdate(Model model, BindingResult result, MainRunConnection mainRunConnection,
        RedirectAttributes redirectAttributes, Message successMessage) {

        if (result.hasErrors()) {
            addCollectionsForDropdowns(model);

            return CONTROLLER_CONTEXT + MAINRUN_CONNECTION_VIEW;
        }

        Long id = mainRunConnectionService.save(mainRunConnection).getId();

        redirectAttributes.addFlashAttribute(MESSAGE, successMessage);

        return REDIRECT + WEBAPI_ROOT_URL + CONNECTIONS + SLASH + id;
    }


    private void addCollectionsForDropdowns(Model model) {

        model.addAttribute(SEAPORTS_ATTRIBUTE, seaportService.getAll());
        model.addAttribute(TERMINALS_ATTRIBUTE, terminalService.getAll());
        model.addAttribute(ROUTE_TYPES_ATTRIBUTE, Arrays.asList(RouteType.BARGE, RouteType.RAIL));
    }
}
