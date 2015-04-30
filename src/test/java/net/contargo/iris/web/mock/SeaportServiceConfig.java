package net.contargo.iris.web.mock;

import net.contargo.iris.seaport.service.SeaportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class SeaportServiceConfig {

    @Bean
    public SeaportService seaportService() {

        return mock(SeaportService.class);
    }
}
