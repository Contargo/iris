package net.contargo.iris.api.mock;

import net.contargo.iris.seaport.dto.SeaportDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


@Configuration
public class SeaportDtoServiceConfig {

    @Bean
    public SeaportDtoService seaportDtoService() {

        return mock(SeaportDtoService.class);
    }
}
