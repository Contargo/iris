package net.contargo.iris.api.mock;

import net.contargo.iris.connection.dto.SeaportConnectionRoutesDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class SeaportTerminalConnectionRoutesDtoServiceConfig {

    @Bean
    public SeaportConnectionRoutesDtoService seaportConnectionRoutesDtoService() {

        return mock(SeaportConnectionRoutesDtoService.class);
    }
}
