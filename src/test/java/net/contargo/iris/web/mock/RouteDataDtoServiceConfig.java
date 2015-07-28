package net.contargo.iris.web.mock;

import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@Configuration
public class RouteDataDtoServiceConfig {

    @Bean
    public RouteDataRevisionDtoService routeDataRevisionDtoService() {

        return mock(RouteDataRevisionDtoService.class);
    }
}
