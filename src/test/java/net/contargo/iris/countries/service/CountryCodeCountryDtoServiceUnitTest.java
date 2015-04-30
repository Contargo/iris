package net.contargo.iris.countries.service;

import net.contargo.iris.countries.dto.CountryCodeCountryDtoService;
import net.contargo.iris.countries.dto.CountryDto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class CountryCodeCountryDtoServiceUnitTest {

    private final CountryService countryServiceMock = mock(CountryCodeCountryService.class);
    private final CountryCodeCountryDtoService sut = new CountryCodeCountryDtoService(countryServiceMock);

    @Before
    public void setUp() {

        Map<String, String> countryMap = new HashMap<>();
        countryMap.put("Tatooine", "TA");
        countryMap.put("Coruscant", "CO");
        when(countryServiceMock.getCountries()).thenReturn(countryMap);
    }


    @Test
    public void testGetCountriesSize() {

        List<CountryDto> countryDtoList = sut.getCountries();
        assertThat(countryDtoList.size(), is(2));
    }


    @Test
    public void testGetCountriesElements() {

        List<CountryDto> actualList = sut.getCountries();
        List<CountryDto> expectedList = Arrays.asList(new CountryDto("Tatooine", "TA"),
                new CountryDto("Coruscant", "CO"));
        Assert.assertTrue(actualList.containsAll(expectedList));
    }
}
