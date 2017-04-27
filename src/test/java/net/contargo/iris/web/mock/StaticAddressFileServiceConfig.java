package net.contargo.iris.web.mock;

import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class StaticAddressFileServiceConfig {

    @Bean
    public StaticAddressFileService staticAddressFileService() {

        return mock(StaticAddressFileService.class);
    }
}
