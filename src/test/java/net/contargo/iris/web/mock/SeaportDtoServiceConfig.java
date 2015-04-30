package net.contargo.iris.web.mock;

import net.contargo.iris.seaport.dto.SeaportDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class SeaportDtoServiceConfig {

    @Bean
    public SeaportDtoService seaportDtoService() {

        return mock(SeaportDtoService.class);
    }
}
