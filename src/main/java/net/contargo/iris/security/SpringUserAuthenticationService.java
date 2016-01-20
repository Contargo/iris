package net.contargo.iris.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

import static java.util.stream.Collectors.toMap;


/**
 * Implementation of {@link UserAuthenticationService} which access {@link SecurityContextHolder}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class SpringUserAuthenticationService implements UserAuthenticationService {

    private final Map<String, UserClassification> userClassifications;

    public SpringUserAuthenticationService(Map<String, String> classificationsByUser) {

        userClassifications = classificationsByUser.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, entry -> UserClassification.getByName(entry.getValue())));
    }

    @Override
    public Authentication getCurrentUser() {

        SecurityContext context = SecurityContextHolder.getContext();

        return context == null ? null : context.getAuthentication();
    }


    @Override
    public UserClassification getUserClassification(String username) {

        UserClassification userClassification = userClassifications.get(username);

        return userClassification == null ? UserClassification.UNCLASSIFIED : userClassification;
    }
}
