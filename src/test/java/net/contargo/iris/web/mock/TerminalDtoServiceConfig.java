package net.contargo.iris.web.mock;

import net.contargo.iris.terminal.dto.TerminalDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class TerminalDtoServiceConfig {

    @Bean
    public TerminalDtoService terminalDtoService() {

        return mock(TerminalDtoService.class);
    }
}
