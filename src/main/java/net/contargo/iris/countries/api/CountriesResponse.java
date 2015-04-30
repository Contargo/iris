package net.contargo.iris.countries.api;

import net.contargo.iris.countries.dto.CountryDto;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
class CountriesResponse extends ResourceSupport {

    private List<CountryDto> countries;

    public List<CountryDto> getCountries() {

        return countries;
    }


    public void setCountries(List<CountryDto> countries) {

        this.countries = countries;
    }
}
