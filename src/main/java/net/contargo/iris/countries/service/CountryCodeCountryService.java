package net.contargo.iris.countries.service;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class CountryCodeCountryService implements CountryService {

    @Override
    public Map<String, String> getCountries() {

        Map<String, String> countryMap = new LinkedHashMap<>();

        for (CountryCode countryCode : CountryCode.values()) {
            countryMap.put(countryCode.getName(), countryCode.getValue());
        }

        return countryMap;
    }
}
