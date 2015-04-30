package net.contargo.iris.web.mock;

import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class TerminalServiceConfig {

    @Bean
    public TerminalService terminalService() {

        return mock(TerminalService.class);
    }
}
