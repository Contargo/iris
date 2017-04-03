package net.contargo.iris.api.mock;

import net.contargo.iris.address.service.BestMatchService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@Configuration
public class BestMatchConfig {

    @Bean
    public BestMatchService bestMatchService() {

        return mock(BestMatchService.class);
    }
}
