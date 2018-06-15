package net.contargo.iris.api.mock;

import net.contargo.iris.transport.service.TransportChainGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class TransportChainGeneratorConfig {

    @Bean
    public TransportChainGenerator transportChainGenerator() {

        return mock(TransportChainGenerator.class);
    }
}
