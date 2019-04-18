package net.contargo.iris.api.mock;

import net.contargo.iris.transport.elevation.ElevationsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Petra Scherer - scherer@synyx.de
 */
@Configuration
public class ElevationsServiceConfig {

    @Bean
    public ElevationsService elevationsService() {

        return mock(ElevationsService.class);
    }
}
