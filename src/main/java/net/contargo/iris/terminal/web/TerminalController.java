package net.contargo.iris.terminal.web;

import net.contargo.iris.Message;
import net.contargo.iris.api.AbstractController;
import net.contargo.iris.sequence.service.UniqueIdSequenceServiceException;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.NonUniqueTerminalException;
import net.contargo.iris.terminal.service.TerminalService;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.invoke.MethodHandles;

import javax.validation.Valid;

import static net.contargo.iris.api.AbstractController.SLASH;
import static net.contargo.iris.api.AbstractController.TERMINALS;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Controller Class for all operations with {@link net.contargo.iris.terminal.Terminal}s.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Controller
@RequestMapping(SLASH + TERMINALS)
public class TerminalController extends AbstractController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private static final String CONTROLLER_CONTEXT = "terminalManagement" + SLASH;

    private static final String TERMINALS_ATTRIBUTE = TERMINALS;
    private static final String TERMINAL_ATTRIBUTE = "terminal";

    private static final String TERMINALS_VIEW = TERMINALS;
    private static final String TERMINAL_FORM_VIEW = "terminal";

    private static final Message SAVE_SUCCESS_MESSAGE = Message.success("terminal.success.save.message");
    private static final Message UPDATE_SUCCESS_MESSAGE = Message.success("terminal.success.update.message");
    private static final String REGIONS_ATTRIBUTE = "regions";

    private final TerminalService terminalService;

    @Autowired
    public TerminalController(TerminalService terminalService) {

        this.terminalService = terminalService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAllTerminals(Model model) {

        model.addAttribute(TERMINALS_ATTRIBUTE, terminalService.getAll());

        return CONTROLLER_CONTEXT + TERMINALS_VIEW;
    }


    @RequestMapping(value = SLASH + "new", method = RequestMethod.GET)
    public String prepareForCreate(Model model) {

        model.addAttribute(TERMINAL_ATTRIBUTE, new Terminal());
        model.addAttribute(REGIONS_ATTRIBUTE, Region.values());

        return CONTROLLER_CONTEXT + TERMINAL_FORM_VIEW;
    }


    @RequestMapping(value = SLASH, method = RequestMethod.POST)
    public String saveTerminal(@Valid @ModelAttribute Terminal terminal, BindingResult result, Model model,
        RedirectAttributes redirectAttributes) {

        return saveOrUpdateTerminal(model, result, terminal, redirectAttributes, SAVE_SUCCESS_MESSAGE);
    }


    @RequestMapping(value = SLASH + ID_PARAM, method = RequestMethod.GET)
    public String getTerminal(@PathVariable Long id, Model model) {

        model.addAttribute(TERMINAL_ATTRIBUTE, terminalService.getById(id));
        model.addAttribute(REGIONS_ATTRIBUTE, Region.values());

        return CONTROLLER_CONTEXT + TERMINAL_FORM_VIEW;
    }


    @RequestMapping(value = SLASH + ID_PARAM, method = RequestMethod.PUT)
    public String updateTerminal(@Valid @ModelAttribute Terminal terminal, BindingResult result, @PathVariable Long id,
        Model model, RedirectAttributes redirectAttributes) {

        Terminal dbTerminal = terminalService.getById(id);

        terminal.setId(dbTerminal.getId());

        return saveOrUpdateTerminal(model, result, terminal, redirectAttributes, UPDATE_SUCCESS_MESSAGE);
    }


    private String saveOrUpdateTerminal(Model model, BindingResult result, Terminal terminal,
        RedirectAttributes redirectAttributes, Message successMessage) {

        if (result.hasErrors()) {
            model.addAttribute(TERMINAL_ATTRIBUTE, terminal);

            return CONTROLLER_CONTEXT + TERMINAL_FORM_VIEW;
        }

        try {
            redirectAttributes.addFlashAttribute(MESSAGE, successMessage);

            Long id = terminalService.save(terminal).getId();

            return REDIRECT + WEBAPI_ROOT_URL + TERMINALS + SLASH + id;
        } catch (NonUniqueTerminalException e) {
            for (String fieldName : e.getBadFields()) {
                result.rejectValue(fieldName, "terminal.nonunique." + fieldName);
            }

            model.addAttribute(TERMINAL_ATTRIBUTE, terminal);

            return CONTROLLER_CONTEXT + TERMINAL_FORM_VIEW;
        } catch (UniqueIdSequenceServiceException e) {
            model.addAttribute(TERMINAL_ATTRIBUTE, terminal);
            model.addAttribute(AbstractController.MESSAGE, UNIQUEID_ERROR_MESSAGE);
            LOG.error(e.getMessage());

            return CONTROLLER_CONTEXT + TERMINAL_FORM_VIEW;
        }
    }
}
