package net.contargo.iris.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This ExceptionHandler handles thrown Exceptions of our public API and responds with Exception specific HTTP status
 * codes.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class PublicAPIExceptionHandler implements HandlerExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(PublicAPIExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex) {

        ModelAndView modelAndView = new ModelAndView();

        LOG.error("REST-API Exception: " + ex.getMessage());

        try {
            if (ex instanceof IllegalArgumentException) {
                modelAndView = handleIllegalArgumentException(response, (IllegalArgumentException) ex);
            } else if (ex instanceof IllegalStateException) {
                modelAndView = handleIllegalStateException(response, (IllegalStateException) ex);
            } else if (ex instanceof HttpClientErrorException) {
                modelAndView = handleHttpClientErrorException(response, (HttpClientErrorException) ex);
            } else if (ex instanceof NotFoundException) {
                modelAndView = handleNotFoundException(response, (NotFoundException) ex);
            } else {
                modelAndView = handleGenericException(response, ex);
            }
        } catch (IOException handlerException) {
            LOG.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }

        return modelAndView;
    }


    private ModelAndView handleIllegalArgumentException(HttpServletResponse response, IllegalArgumentException ex)
        throws IOException {

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request. " + ex.getMessage());

        return new ModelAndView();
    }


    private ModelAndView handleIllegalStateException(HttpServletResponse response, IllegalStateException ex)
        throws IOException {

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request. " + ex.getMessage());

        return new ModelAndView();
    }


    private ModelAndView handleHttpClientErrorException(HttpServletResponse response, HttpClientErrorException ex)
        throws IOException {

        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
            "Service is temporary not available, please try again later. " + ex.getMessage());

        return new ModelAndView();
    }


    private ModelAndView handleGenericException(HttpServletResponse response, Exception ex) throws IOException {

        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error. " + ex.getMessage());

        return new ModelAndView();
    }


    private ModelAndView handleNotFoundException(HttpServletResponse response, NotFoundException ex)
        throws IOException {

        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found. " + ex.getMessage());

        return new ModelAndView();
    }
}
