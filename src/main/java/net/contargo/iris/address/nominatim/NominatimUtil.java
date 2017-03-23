package net.contargo.iris.address.nominatim;

import java.util.HashMap;
import java.util.Map;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class NominatimUtil {

    public static Map<String, String> parameterMap(String postalCode, String city, String countryCode) {

        return parameterMap(null, postalCode, city, countryCode, null);
    }


    public static Map<String, String> parameterMap(String street, String postalCode, String city, String countryCode,
        String name) {

        Map<String, String> addressDetails = new HashMap<>();

        if (city != null) {
            addressDetails.put(CITY.getKey(), city);
        }

        if (countryCode != null) {
            addressDetails.put(COUNTRY.getKey(), countryCode);
        }

        if (postalCode != null) {
            addressDetails.put(POSTAL_CODE.getKey(), postalCode);
        }

        if (street != null) {
            addressDetails.put(STREET.getKey(), street);
        }

        if (name != null) {
            addressDetails.put(NAME.getKey(), name);
        }

        return addressDetails;
    }
}
