package net.contargo.iris.actuator;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.endpoint.InfoEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HealthMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collection;


/**
 * Spring Configuration class for actuator endpoints.
 *
 * @author  David Schilling - schilling@synyx.de
 */

@Configuration
@Import({ EndpointAutoConfiguration.class, HealthIndicatorAutoConfiguration.class })
public class ActuatorConfig {

    @Bean
    @Autowired
    public EndpointHandlerMapping endpointHandlerMapping(Collection<? extends MvcEndpoint> endpoints) {

        return new EndpointHandlerMapping(endpoints);
    }


    @Bean
    @Autowired
    public HealthMvcEndpoint healthMvcEndpoint(HealthEndpoint delegate) {

        return new HealthMvcEndpoint(delegate, true);
    }


    @Bean
    @Autowired
    public EndpointMvcAdapter infoMvcEndPoint(InfoEndpoint delegate) {

        return new EndpointMvcAdapter(delegate);
    }
}
