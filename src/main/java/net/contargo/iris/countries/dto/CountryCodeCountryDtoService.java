package net.contargo.iris.countries.dto;

import net.contargo.iris.countries.service.CountryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author  Arnold Franke - franke@synyx.de
 *
 *          <p>Created: Date: 8/23/13 Time: 8:57 AM</p>
 */
public class CountryCodeCountryDtoService implements CountryDtoService {

    private final CountryService service;

    public CountryCodeCountryDtoService(CountryService service) {

        this.service = service;
    }

    @Override
    public List<CountryDto> getCountries() {

        List<CountryDto> countryDtoList = new ArrayList<>();

        for (Map.Entry<String, String> entry : service.getCountries().entrySet()) {
            countryDtoList.add(new CountryDto(entry.getKey(), entry.getValue()));
        }

        return countryDtoList;
    }
}
