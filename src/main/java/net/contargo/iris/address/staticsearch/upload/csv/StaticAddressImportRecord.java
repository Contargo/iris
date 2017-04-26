package net.contargo.iris.address.staticsearch.upload.csv;

import net.contargo.iris.address.nominatim.service.AddressDetailKey;

import java.util.HashMap;
import java.util.Map;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public final class StaticAddressImportRecord {

    private final String postalCode;
    private final String city;
    private final String country;

    public StaticAddressImportRecord(String postalCode, String city, String country) {

        this.postalCode = postalCode;
        this.city = city;
        this.country = country.toUpperCase();
    }

    public String getPostalCode() {

        return postalCode;
    }


    public String getCity() {

        return city;
    }


    public String getCountry() {

        return country;
    }


    public Map<String, String> toAddressDetails() {

        Map<String, String> details = new HashMap<>();
        details.put(AddressDetailKey.POSTAL_CODE.getKey(), postalCode);
        details.put(AddressDetailKey.CITY.getKey(), city);
        details.put(AddressDetailKey.COUNTRY.getKey(), country);

        return details;
    }
}
