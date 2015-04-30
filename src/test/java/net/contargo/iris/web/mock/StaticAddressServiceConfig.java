package net.contargo.iris.web.mock;

import net.contargo.iris.address.staticsearch.service.StaticAddressService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Configuration
public class StaticAddressServiceConfig {

    @Bean
    public StaticAddressService staticAddressService() {

        return mock(StaticAddressService.class);
    }
}
