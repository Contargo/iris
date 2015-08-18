package net.contargo.iris.web;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Controller
public class RootController {

    @RequestMapping(value = "/*", method = GET)
    public String index(Model model) {

        return "index";
    }


    @RequestMapping(value = "/triangle/", method = GET)
    public String triangle() {

        return "routing/triangle";
    }
}
