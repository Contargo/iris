package net.contargo.iris.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Customized {@code BasicAuthenticationEntryPoint} that overrides {@code BasicAuthenticationEntryPoint#commence} in
 * order to prevent response header 'WWW-Authenticate' to be set to 'Basic realms=...'. Otherwise browsers would show a
 * native login prompt.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class IRISBasicAuthEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        // Suppress Basic Auth Header when client is a browser.
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null || !userAgent.toLowerCase().contains("mozilla")) {
            response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
