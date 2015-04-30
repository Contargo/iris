package net.contargo.iris.api.mock;

import net.contargo.iris.address.dto.AddressDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


@Configuration
public class AddressDtoServiceConfig {

    @Bean
    public AddressDtoService addressDtoService() {

        return mock(AddressDtoService.class);
    }
}
