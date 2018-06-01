package net.contargo.iris.api.mock;

import net.contargo.iris.transport.service.RouteService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@Configuration
public class RouteServiceConfig {

    @Bean
    public RouteService routeService() {

        return mock(RouteService.class);
    }
}
