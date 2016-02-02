package net.contargo.iris.web;

import net.contargo.iris.security.UserAuthenticationService;

import org.slf4j.Logger;

import org.springframework.security.core.Authentication;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * This ExceptionHandler generates detailed error reports if an unexpected error resp. Exception occurs.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class WebExceptionHandler extends SimpleMappingExceptionResolver {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final UserAuthenticationService userAuthenticationService;

    public WebExceptionHandler(UserAuthenticationService userAuthenticationService) {

        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    public ModelAndView doResolveException(HttpServletRequest req, HttpServletResponse resp, Object h, Exception ex) {

        Authentication user = userAuthenticationService.getCurrentUser();

        LOG.error("Web Exception by {}:", user, ex);

        return super.doResolveException(req, resp, h, ex);
    }
}
