package net.contargo.iris.api.mock;

import net.contargo.iris.distancecloud.dto.DistanceCloudAddressDtoService;
import net.contargo.iris.distancecloud.dto.DistanceCloudAddressDtoServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class DistanceCloudAddressDtoServiceConfig {

    @Bean
    public DistanceCloudAddressDtoService distanceCloudAddressDtoService() {

        return mock(DistanceCloudAddressDtoServiceImpl.class);
    }
}
