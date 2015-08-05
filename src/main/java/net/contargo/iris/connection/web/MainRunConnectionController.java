package net.contargo.iris.connection.web;

import net.contargo.iris.api.AbstractController;
import net.contargo.iris.connection.service.MainRunConnectionService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    private static final String MAINRUN_CONNECTIONS_ATTRIBUTE = "mainRunConnections";

    private static final String MAINRUN_CONNECTIONS_VIEW = "mainrunconnections";
    private static final String MAINRUN_CONNECTION_VIEW = "mainrunconnection";

    private final MainRunConnectionService mainRunConnectionService;

    @Autowired
    public MainRunConnectionController(MainRunConnectionService mainRunConnectionService) {

        this.mainRunConnectionService = mainRunConnectionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAllConnections(Model model) {

        model.addAttribute(MAINRUN_CONNECTIONS_ATTRIBUTE, mainRunConnectionService.getAll());

        return CONTROLLER_CONTEXT + MAINRUN_CONNECTIONS_VIEW;
    }


    @RequestMapping(value = SLASH + ID_PARAM, method = RequestMethod.GET)
    public String prepareForUpdate() {

        return CONTROLLER_CONTEXT + MAINRUN_CONNECTION_VIEW;
    }
}
