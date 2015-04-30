package net.contargo.iris.api.mock;

import net.contargo.iris.gis.dto.AirlineDistanceDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
class AirlineDistanceDtoServiceConfig {

    @Bean
    AirlineDistanceDtoService gisDtoService() {

        return mock(AirlineDistanceDtoService.class);
    }
}
