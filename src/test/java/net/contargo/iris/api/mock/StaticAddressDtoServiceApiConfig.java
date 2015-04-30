package net.contargo.iris.api.mock;

import net.contargo.iris.address.staticsearch.dto.StaticAddressDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
@Configuration
public class StaticAddressDtoServiceApiConfig {

    @Bean
    public StaticAddressDtoService staticAddressDtoService() {

        return mock(StaticAddressDtoService.class);
    }
}
