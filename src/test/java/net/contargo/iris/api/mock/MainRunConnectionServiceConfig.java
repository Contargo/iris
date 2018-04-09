package net.contargo.iris.api.mock;

import net.contargo.iris.connection.service.MainRunConnectionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@Configuration
public class MainRunConnectionServiceConfig {

    @Bean
    public MainRunConnectionService mainRunConnectionService() {

        return mock(MainRunConnectionService.class);
    }
}
