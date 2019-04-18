package net.contargo.iris.api.mock;

import net.contargo.iris.transport.AddressOnlyTransportChainGenerator;
import net.contargo.iris.transport.TerminalTransportChainGenerator;
import net.contargo.iris.transport.TransportChainGeneratorStrategyAdvisor;

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
    public TerminalTransportChainGenerator transportChainGenerator() {

        return mock(TerminalTransportChainGenerator.class);
    }


    @Bean
    public AddressOnlyTransportChainGenerator addressOnlyTransportChainGeneratorGenerator() {

        return mock(AddressOnlyTransportChainGenerator.class);
    }


    @Bean
    public TransportChainGeneratorStrategyAdvisor transportChainGeneratorStrategyAdvisor() {

        return mock(TransportChainGeneratorStrategyAdvisor.class);
    }
}
