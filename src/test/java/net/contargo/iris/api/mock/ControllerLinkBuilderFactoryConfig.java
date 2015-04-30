package net.contargo.iris.api.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class ControllerLinkBuilderFactoryConfig {

    @Bean
    public ControllerLinkBuilderFactory controllerLinkBuilderFactory() {

        return mock(ControllerLinkBuilderFactory.class);
    }
}
