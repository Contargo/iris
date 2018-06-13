package net.contargo.iris.api.mock;

import net.contargo.iris.transport.service.TransportDescriptionExtender;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class TransportDescriptionExtenderConfig {

    @Bean
    public TransportDescriptionExtender transportDescriptionExtender() {

        return mock(TransportDescriptionExtender.class);
    }
}
