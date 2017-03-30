package net.contargo.iris.address.service;

import net.contargo.iris.address.Address;

import java.math.BigDecimal;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class BestMatch {

    private final String hashKey;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private final String postalcode;
    private final String city;
    private final String countryCode;

    protected BestMatch(String hashKey, BigDecimal latitude, BigDecimal longitude, String postalcode, String city,
        String countryCode) {

        this.hashKey = hashKey;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postalcode = postalcode;
        this.city = city;
        this.countryCode = countryCode;
    }

    static BestMatch of(Address address) {

        String hashKey = address.getHashKey().orElse(null);

        return new BestMatch(hashKey, address.getLatitude(), address.getLongitude(), address.getPostcode(),
                address.getCity(), address.getCountryCode());
    }


    public String getHashKey() {

        return hashKey;
    }


    public BigDecimal getLatitude() {

        return latitude;
    }


    public BigDecimal getLongitude() {

        return longitude;
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
