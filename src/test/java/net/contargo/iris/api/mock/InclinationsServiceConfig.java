package net.contargo.iris.api.mock;

import net.contargo.iris.transport.inclinations.service.InclinationsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@Configuration
public class InclinationsServiceConfig {

    @Bean
    public InclinationsService inclinationsService() {

        return mock(InclinationsService.class);
    }
}
