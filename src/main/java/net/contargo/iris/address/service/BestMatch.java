package net.contargo.iris.address.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import java.math.BigDecimal;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class BestMatch extends GeoLocation {

    private final String hashKey;
    private final String postalcode;
    private final String city;
    private final String countryCode;

    protected BestMatch(String hashKey, BigDecimal latitude, BigDecimal longitude, String postalcode, String city,
        String countryCode) {

        super(latitude, longitude);

        this.hashKey = hashKey;
        this.postalcode = postalcode;
        this.city = city;
        this.countryCode = countryCode;
    }

    static BestMatch of(StaticAddress staticAddress) {

        return new BestMatch(staticAddress.getHashKey(), staticAddress.getLatitude(), staticAddress.getLongitude(),
                staticAddress.getPostalcode(), staticAddress.getCity(), staticAddress.getCountry());
    }


    static BestMatch of(Address address) {

        return new BestMatch(null, address.getLatitude(), address.getLongitude(), address.getPostcode(),
                address.getCity(), address.getCountryCode());
    }


    public String getHashKey() {

        return hashKey;
    }


    public String getPostalcode() {

        return postalcode;
    }


    public String getCity() {

        return city;
    }


    public String getCountryCode() {

        return countryCode;
    }
}
