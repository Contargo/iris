package net.contargo.iris.web.mock;

import net.contargo.iris.security.UserAuthenticationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@Configuration
public class UserAuthenticationServiceConfig {

    @Bean
    public UserAuthenticationService userAuthenticationService() {

        return mock(UserAuthenticationService.class);
    }
}
