package net.contargo.iris.web.mock;

import net.contargo.iris.routedatarevision.service.cleanup.RouteDataRevisionCleanupService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class RouteDataRevisionCleanupServiceConfig {

    @Bean
    public RouteDataRevisionCleanupService routeDataRevisionCleanupService() {

        return mock(RouteDataRevisionCleanupService.class);
    }
}
