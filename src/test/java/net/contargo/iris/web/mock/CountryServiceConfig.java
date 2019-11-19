package net.contargo.iris.web.mock;

import net.contargo.iris.countries.service.CountryService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@Configuration
public class CountryServiceConfig {

    @Bean
    public CountryService countryService() {

        return mock(CountryService.class);
    }
}
