package net.contargo.iris.api.mock;

import net.contargo.iris.countries.dto.CountryDto;
import net.contargo.iris.countries.dto.CountryDtoService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


/**
 * @author  Arnold Franke - franke@synyx.de
 *
 *          <p>Created: Date: 8/27/13 Time: 9:28 AM</p>
 */

@Configuration
public class CountryDtoServiceConfig {

    private static final CountryDto GERMANY = new CountryDto("Germany", "DE");
    private static final CountryDto BELGIUM = new CountryDto("Belgium", "BE");

    @Bean
    public CountryDtoService countryDtoService() {

        return mock(CountryDtoService.class);
    }
}
