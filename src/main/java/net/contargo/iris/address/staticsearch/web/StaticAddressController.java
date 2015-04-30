package net.contargo.iris.address.staticsearch.web;

import net.contargo.iris.Message;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressCoordinatesDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.api.AbstractController;
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

import java.lang.invoke.MethodHandles;

import java.util.List;

import javax.validation.Valid;

import static net.contargo.iris.Message.error;
import static net.contargo.iris.Message.success;
import static net.contargo.iris.api.AbstractController.SLASH;
import static net.contargo.iris.api.AbstractController.STATIC_ADDRESSES;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Controller for the web API of {@link net.contargo.iris.address.staticsearch.StaticAddress}s.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
@Controller
@RequestMapping(SLASH + STATIC_ADDRESSES)
public class StaticAddressController extends AbstractController {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private static final String CONTROLLER_CONTEXT = "staticAddressManagement" + SLASH;

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

    @Autowired
    public StaticAddressController(StaticAddressService staticAddressService) {

        this.staticAddressService = staticAddressService;
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


    @RequestMapping(value = SLASH + ID_PARAM, method = RequestMethod.GET)
    public String getStaticAddress(Model model, @PathVariable Long id) {

        model.addAttribute(ENTITY_ATTRIBUTE, staticAddressService.findbyId(id));

        return CONTROLLER_CONTEXT + ENTITY_FORM_VIEW;
    }


    @RequestMapping(value = SLASH + "new", method = RequestMethod.GET)
    public String prepareForCreate(Model model) {

        model.addAttribute(ENTITY_ATTRIBUTE, new StaticAddress());

        return CONTROLLER_CONTEXT + ENTITY_FORM_VIEW;
    }


    @ModelAttribute("staticAddress")
    public void prepareSaveStaticAddress(StaticAddress staticAddress) {

        staticAddressService.normalizeFields(staticAddress);
    }


    @RequestMapping(value = SLASH, method = RequestMethod.POST)
    public String saveStaticAddress(@Valid @ModelAttribute StaticAddress staticAddress, BindingResult result,
        Model model) {

        return saveOrUpdateStaticAddress(staticAddress, result, model, SAVE_SUCCESS_MESSAGE);
    }


    @RequestMapping(value = SLASH + ID_PARAM, method = RequestMethod.PUT)
    public String updateStaticAddress(@Valid @ModelAttribute StaticAddress staticAddress, BindingResult result,
        Model model) {

        return saveOrUpdateStaticAddress(staticAddress, result, model, UPDATE_SUCCESS_MESSAGE);
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
            model.addAttribute(AbstractController.MESSAGE, message);
        } catch (StaticAddressDuplicationException e) {
            model.addAttribute(ENTITY_ATTRIBUTE, staticAddress);
            model.addAttribute(AbstractController.MESSAGE, DUPLICATION_ERROR_MESSAGE);
        } catch (StaticAddressCoordinatesDuplicationException e) {
            model.addAttribute(ENTITY_ATTRIBUTE, staticAddress);
            model.addAttribute(AbstractController.MESSAGE, DUPLICATION_GEOCOORDINATES_ERROR_MESSAGE);
        } catch (UniqueIdSequenceServiceException e) {
            model.addAttribute(ENTITY_ATTRIBUTE, staticAddress);
            model.addAttribute(AbstractController.MESSAGE, UNIQUEID_ERROR_MESSAGE);
            LOG.error(e.getMessage());
        }

        return CONTROLLER_CONTEXT + ENTITY_FORM_VIEW;
    }
}
