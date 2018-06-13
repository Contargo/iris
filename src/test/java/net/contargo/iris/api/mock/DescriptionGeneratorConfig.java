package net.contargo.iris.api.mock;

import net.contargo.iris.transport.service.DescriptionGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class DescriptionGeneratorConfig {

    @Bean
    public DescriptionGenerator descriptionGenerator() {

        return mock(DescriptionGenerator.class);
    }
}
