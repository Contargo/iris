package net.contargo.iris.security;

import org.springframework.security.core.Authentication;


/**
 * Service to get information about the current User.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public interface UserAuthenticationService {

    /**
     * @return  the current user as {@link org.springframework.security.core.Authentication}
     */
    Authentication getCurrentUser();
}
