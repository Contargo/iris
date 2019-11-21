package net.contargo.iris.api.mock;

import net.contargo.iris.address.staticsearch.service.ClosestStaticAddressService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@Configuration
public class ClosestStaticAddressServiceConfig {

    @Bean
    public ClosestStaticAddressService closestStaticAddressService() {

        return mock(ClosestStaticAddressService.class);
    }
}
