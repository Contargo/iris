package net.contargo.iris.api.mock;

import net.contargo.iris.connection.dto.MainRunConnectionDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class MainRunConnectionDtoServiceConfig {

    @Bean
    public MainRunConnectionDtoService connectionDtoService() {

        return mock(MainRunConnectionDtoService.class);
    }
}
