package net.contargo.iris.address.staticsearch.upload.csv;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressErrorRecord {

    private final String postalCode;
    private final String city;
    private final String country;
    private final String error;

    public StaticAddressErrorRecord(String postalCode, String city, String country, String error) {

        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.error = error;
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


    public String getError() {

        return error;
    }
}
