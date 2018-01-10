package net.contargo.iris.api.mock;

import net.contargo.iris.address.nominatim.service.AddressService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


@Configuration
public class AddressServiceConfig {

    @Bean
    public AddressService addressService() {

        return mock(AddressService.class);
    }
}
