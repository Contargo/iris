package net.contargo.iris.web.mock;

import net.contargo.iris.connection.service.MainRunConnectionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class MainRunConnectionServiceConfig {

    @Bean
    public MainRunConnectionService mainrunConnectionService() {

        return mock(MainRunConnectionService.class);
    }
}
