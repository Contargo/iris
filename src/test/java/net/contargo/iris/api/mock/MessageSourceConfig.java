package net.contargo.iris.api.mock;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {

        return mock(MessageSource.class);
    }
}
