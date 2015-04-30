package net.contargo.iris.api.mock;

import net.contargo.iris.connection.dto.SeaportTerminalConnectionDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class SeaportTerminalConnectionDtoServiceConfig {

    @Bean
    public SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService() {

        return mock(SeaportTerminalConnectionDtoService.class);
    }
}
