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


    /**
     * Returns the {@link UserClassification} for the given username. If there is no classification defined for the
     * given username {@link UserClassification#UNCLASSIFIED} is returned.
     *
     * @param  username  identifies a user.
     *
     * @return  the users classification.
     */
    UserClassification getUserClassification(String username);
}
