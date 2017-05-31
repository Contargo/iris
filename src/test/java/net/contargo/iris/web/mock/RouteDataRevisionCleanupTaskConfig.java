package net.contargo.iris.web.mock;

import net.contargo.iris.routedatarevision.service.cleanup.RouteDataRevisionCleanupTask;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class RouteDataRevisionCleanupTaskConfig {

    @Bean
    public RouteDataRevisionCleanupTask routeDataRevisionCleanupTask() {

        return mock(RouteDataRevisionCleanupTask.class);
    }
}
