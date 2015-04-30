package net.contargo.iris.web;

import net.contargo.iris.api.AbstractController;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Controller
public class RootController extends AbstractController {

    public static final String ROOT = SLASH;

    @RequestMapping(value = SLASH + STAR, method = RequestMethod.GET)
    public String index(Model model) {

        return INDEX;
    }


    @RequestMapping(value = SLASH + TRIANGLE + SLASH, method = RequestMethod.GET)
    public String triangle() {

        return TRIANGLE_VIEW;
    }
}
