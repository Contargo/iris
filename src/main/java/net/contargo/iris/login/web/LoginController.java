package net.contargo.iris.login.web;

import net.contargo.iris.api.AbstractController;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import static net.contargo.iris.api.AbstractController.LOGIN;
import static net.contargo.iris.api.AbstractController.SLASH;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
@Controller
@RequestMapping(value = SLASH + LOGIN)
public class LoginController extends AbstractController {

    private static final String CONTROLLER_CONTEXT = "login" + SLASH;

    @RequestMapping(value = "", method = GET)
    public String getLogin() {

        return CONTROLLER_CONTEXT + LOGIN;
    }
}
