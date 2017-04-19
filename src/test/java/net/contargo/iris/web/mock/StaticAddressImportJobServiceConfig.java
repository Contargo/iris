package net.contargo.iris.web.mock;

import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportJobService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class StaticAddressImportJobServiceConfig {

    @Bean
    public StaticAddressImportJobService staticAddressImportJobService() {

        return mock(StaticAddressImportJobService.class);
    }
}
