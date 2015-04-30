package net.contargo.iris.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Implementation of {@link UserAuthenticationService} which access {@link SecurityContextHolder}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class SpringUserAuthenticationService implements UserAuthenticationService {

    @Override
    public Authentication getCurrentUser() {

        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null) {
            return null;
        }

        return context.getAuthentication();
    }
}
