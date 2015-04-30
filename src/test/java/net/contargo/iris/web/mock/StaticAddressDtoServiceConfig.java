package net.contargo.iris.web.mock;

import net.contargo.iris.address.staticsearch.dto.StaticAddressDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class StaticAddressDtoServiceConfig {

    @Bean
    public StaticAddressDtoService staticAddressDtoService() {

        return mock(StaticAddressDtoService.class);
    }
}
