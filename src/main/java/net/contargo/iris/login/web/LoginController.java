package net.contargo.iris.login.web;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @RequestMapping(value = "", method = GET)
    public String getLogin() {

        return "login/login";
    }
}
