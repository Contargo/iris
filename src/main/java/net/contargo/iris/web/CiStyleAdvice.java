package net.contargo.iris.web;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@ControllerAdvice
public class CiStyleAdvice {

    @Value("${style.CI}")
    private String ciCssFileName;

    @ModelAttribute("ciCssFileName")
    public String cssFileName() {

        return ciCssFileName;
    }
}
