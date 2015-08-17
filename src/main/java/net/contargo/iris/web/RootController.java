package net.contargo.iris.web;

import net.contargo.iris.api.AbstractController;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Controller
public class RootController extends AbstractController {

    @RequestMapping(value = SLASH + STAR, method = GET)
    public String index(Model model) {

        return INDEX;
    }


    @RequestMapping(value = SLASH + TRIANGLE + SLASH, method = GET)
    public String triangle() {

        return TRIANGLE_VIEW;
    }
}
