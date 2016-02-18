package net.contargo.iris.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;


/**
 * {@link RequestMatcher} checking if the given request has the header 'Accept' containing 'text/html'
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class RequestContainingAcceptTextHeaderRequestMatcher implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {

        String accept = request.getHeader("Accept");

        return accept != null && accept.contains("text/html");
    }
}
