package net.contargo.iris.seaport.web;

import net.contargo.iris.Message;
import net.contargo.iris.api.ControllerConstants;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.NonUniqueSeaportException;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.sequence.service.UniqueIdSequenceServiceException;

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

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Controller Class for all operations with {@link Seaport}s.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Controller
@RequestMapping("/seaports")
public class SeaportController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private static final String CONTROLLER_CONTEXT = "seaportManagement/";

    private static final String SEAPORTS_ATTRIBUTE = "seaports";
    private static final String SEAPORT_ATTRIBUTE = "seaport";

    private static final String SEAPORTS_VIEW = "seaports";
    private static final String SEAPORT_VIEW = "seaport";

    private static final Message SAVE_SUCCESS_MESSAGE = Message.success("seaport.success.save.message");
    private static final Message UPDATE_SUCCESS_MESSAGE = Message.success("seaport.success.update.message");

    private final SeaportService seaportService;

    @Autowired
    public SeaportController(SeaportService seaportService) {

        this.seaportService = seaportService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAllSeaports(Model model) {

        model.addAttribute(SEAPORTS_ATTRIBUTE, seaportService.getAll());

        return CONTROLLER_CONTEXT + SEAPORTS_VIEW;
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String prepareForCreate(Model model) {

        model.addAttribute(SEAPORT_ATTRIBUTE, new Seaport());

        return CONTROLLER_CONTEXT + SEAPORT_VIEW;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String saveSeaport(@Valid @ModelAttribute Seaport seaport, BindingResult result, Model model,
        RedirectAttributes redirectAttributes) {

        return saveOrUpdateSeaport(model, result, seaport, redirectAttributes, SAVE_SUCCESS_MESSAGE);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getSeaport(@PathVariable Long id, Model model) {

        model.addAttribute(SEAPORT_ATTRIBUTE, seaportService.getById(id));

        return CONTROLLER_CONTEXT + SEAPORT_VIEW;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateSeaport(@Valid @ModelAttribute Seaport seaport, BindingResult result, @PathVariable Long id,
        Model model, RedirectAttributes redirectAttributes) {

        seaport.setId(seaportService.getById(id).getId());

        return saveOrUpdateSeaport(model, result, seaport, redirectAttributes, UPDATE_SUCCESS_MESSAGE);
    }


    private String saveOrUpdateSeaport(Model model, BindingResult result, Seaport seaport,
        RedirectAttributes redirectAttributes, Message successMessage) {

        if (result.hasErrors()) {
            model.addAttribute(SEAPORT_ATTRIBUTE, seaport);

            return CONTROLLER_CONTEXT + SEAPORT_VIEW;
        }

        try {
            Long id = seaportService.save(seaport).getId();

            redirectAttributes.addFlashAttribute("message", successMessage);

            return ControllerConstants.REDIRECT + ControllerConstants.WEBAPI_ROOT_URL + "seaports/" + id;
        } catch (NonUniqueSeaportException e) {
            for (String fieldName : e.getBadFields()) {
                result.rejectValue(fieldName, "seaport.nonunique." + fieldName);
            }

            model.addAttribute(SEAPORT_ATTRIBUTE, seaport);

            return CONTROLLER_CONTEXT + SEAPORT_VIEW;
        } catch (UniqueIdSequenceServiceException e) {
            model.addAttribute(SEAPORT_ATTRIBUTE, seaport);
            model.addAttribute(Message.MESSAGE, ControllerConstants.UNIQUEID_ERROR_MESSAGE);
            LOG.error(e.getMessage());

            return CONTROLLER_CONTEXT + SEAPORT_VIEW;
        }
    }
}
