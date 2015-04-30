package net.contargo.iris.gis.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@Configuration
public class AirlineDistanceResponseAssemblerConfig {

    @Bean
    AirlineDistanceResponseAssembler airlineDistanceDtoAssembler() {

        return new AirlineDistanceResponseAssembler();
    }
}
