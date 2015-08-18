package net.contargo.iris.api.mock;

import net.contargo.iris.route.dto.EnricherDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


@Configuration
public class EnricherServiceConfig {

    @Bean
    public EnricherDtoService EnricherDtoService() {

        return mock(EnricherDtoService.class);
    }
}
