package net.contargo.iris.api.mock;

import net.contargo.iris.route.service.RouteUrlSerializationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


@Configuration
public class RouteUrlSerializationServiceConfig {

    @Bean
    public RouteUrlSerializationService routeUrlSerializationService() {

        return mock(RouteUrlSerializationService.class);
    }
}
