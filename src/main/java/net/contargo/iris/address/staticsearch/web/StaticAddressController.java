package net.contargo.iris.address.staticsearch.web;

import net.contargo.iris.Message;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressCoordinatesDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;
import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileService;
import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileStorageException;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportJobService;
import net.contargo.iris.api.ControllerConstants;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.invoke.MethodHandles;

import java.util.List;

import javax.validation.Valid;

import static net.contargo.iris.Message.error;
import static net.contargo.iris.Message.success;

import static org.apache.commons.lang.StringUtils.isBlank;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Controller for the web API of {@link net.contargo.iris.address.staticsearch.StaticAddress}s.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Controller
@RequestMapping("/staticaddresses")
public class StaticAddressController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private static final String CONTROLLER_CONTEXT = "staticAddressManagement/";

    private static final String STATIC_ADDRESS_REQUEST_BEAN = "request";
    private static final String ENTITY_ATTRIBUTE = "staticAddress";
    private static final String ENTITYS_ATTRIBUTE = "staticAddresses";

    private static final String ENTITY_VIEW = "staticaddresses";
    private static final String ENTITY_FORM_VIEW = "staticaddressForm";

    private static final Message SAVE_SUCCESS_MESSAGE = success("staticaddress.save.success");
    private static final Message UPDATE_SUCCESS_MESSAGE = success("staticaddress.update.success");
    private static final Message DUPLICATION_ERROR_MESSAGE = error("staticaddress.error.duplicate");
    private static final Message DUPLICATION_GEOCOORDINATES_ERROR_MESSAGE = error(
            "staticaddress.error.coordinates.duplicate");

    private final StaticAddressService staticAddressService;
    private final StaticAddressFileService staticAddressFileService;
    private final StaticAddressImportJobService jobService;

    @Autowired
    public StaticAddressController(StaticAddressService staticAddressService,
        StaticAddressFileService staticAddressFileService, StaticAddressImportJobService jobService) {

        this.staticAddressService = staticAddressService;
        this.staticAddressFileService = staticAddressFileService;
        this.jobService = jobService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getByDetails(Model model, @ModelAttribute StaticAddressRequest staticAddressRequest) {

        if (!staticAddressRequest.isEmpty()) {
            List<StaticAddress> staticAddressList = staticAddressService.getAddressesByDetailsWithFallbacks(
                    staticAddressRequest.getPostalcode(), staticAddressRequest.getCity(), null);

            model.addAttribute(ENTITYS_ATTRIBUTE, staticAddressList);
        }

        model.addAttribute(STATIC_ADDRESS_REQUEST_BEAN, staticAddressRequest);

        return CONTROLLER_CONTEXT + ENTITY_VIEW;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getStaticAddress(Model model, @PathVariable Long id) {

        model.addAttribute(ENTITY_ATTRIBUTE, staticAddressService.findById(id));

        return CONTROLLER_CONTEXT + ENTITY_FORM_VIEW;
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String prepareForCreate(Model model) {

        model.addAttribute(ENTITY_ATTRIBUTE, new StaticAddress());

        return CONTROLLER_CONTEXT + ENTITY_FORM_VIEW;
    }


    @ModelAttribute("staticAddress")
    public void prepareSaveStaticAddress(StaticAddress staticAddress) {

        staticAddressService.normalizeFields(staticAddress);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String saveStaticAddress(@Valid @ModelAttribute StaticAddress staticAddress, BindingResult result,
        Model model) {

        return saveOrUpdateStaticAddress(staticAddress, result, model, SAVE_SUCCESS_MESSAGE);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateStaticAddress(@Valid @ModelAttribute StaticAddress staticAddress, BindingResult result,
        Model model) {

        return saveOrUpdateStaticAddress(staticAddress, result, model, UPDATE_SUCCESS_MESSAGE);
    }


    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String getImportForm() {

        return CONTROLLER_CONTEXT + "importForm";
    }


    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String uploadImportCsv(@RequestParam("file") MultipartFile file,
        @RequestParam("email") String email, Model model, RedirectAttributes redirectAttributes) {

        if (isBlank(email)) {
            model.addAttribute("message", error("An email address is necessary."));

            return CONTROLLER_CONTEXT + "importForm";
        }

        String filename = file.getOriginalFilename();

        try {
            staticAddressFileService.saveFile(file);
        } catch (StaticAddressFileStorageException e) {
            LOG.error("StaticAddress file upload failed", e);
            redirectAttributes.addFlashAttribute("message",
                Message.error("There was an error uploading " + filename + "; please try again later."));

            return "redirect:" + "/web/staticaddresses/";
        }

        jobService.addJob(new StaticAddressImportJob(email, filename));

        redirectAttributes.addFlashAttribute("message",
            Message.success("Successfully uploaded " + filename + "; it will be processed shortly."));

        return "redirect:" + "/web/staticaddresses/";
    }


    private String saveOrUpdateStaticAddress(StaticAddress staticAddress, BindingResult result, Model model,
        Message message) {

        if (result.hasErrors()) {
            model.addAttribute(ENTITY_ATTRIBUTE, staticAddress);

            return CONTROLLER_CONTEXT + ENTITY_FORM_VIEW;
        }

        try {
            StaticAddress savedStaticAddress = staticAddressService.saveStaticAddress(staticAddress);

            model.addAttribute(ENTITY_ATTRIBUTE, savedStaticAddress);
            model.addAttribute(Message.MESSAGE, message);
        } catch (StaticAddressDuplicationException e) {
            model.addAttribute(ENTITY_ATTRIBUTE, staticAddress);
            model.addAttribute(Message.MESSAGE, DUPLICATION_ERROR_MESSAGE);
        } catch (StaticAddressCoordinatesDuplicationException e) {
            model.addAttribute(ENTITY_ATTRIBUTE, staticAddress);
            model.addAttribute(Message.MESSAGE, DUPLICATION_GEOCOORDINATES_ERROR_MESSAGE);
        } catch (UniqueIdSequenceServiceException e) {
            model.addAttribute(ENTITY_ATTRIBUTE, staticAddress);
            model.addAttribute(Message.MESSAGE, ControllerConstants.UNIQUEID_ERROR_MESSAGE);
            LOG.error(e.getMessage());
        }

        return CONTROLLER_CONTEXT + ENTITY_FORM_VIEW;
    }
}
