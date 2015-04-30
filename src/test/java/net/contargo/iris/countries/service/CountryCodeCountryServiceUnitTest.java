package net.contargo.iris.countries.service;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class CountryCodeCountryServiceUnitTest {

    public static final int NUMBER_OF_COUNTRIES = 12;
    private final CountryService sut = new CountryCodeCountryService();

    @Test
    public void testGetCountries() {

        assertThat(sut.getCountries().size(), is(NUMBER_OF_COUNTRIES));
    }


    @Test
    public void testGetSpecificCountry() {

        assertThat(sut.getCountries().get("Austria"), is("AT"));
    }
}
