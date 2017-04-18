package net.contargo.iris.address.staticsearch.upload.csv;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressErrorRecord {

    private final String postalCode;
    private final String city;
    private final String error;

    public StaticAddressErrorRecord(String postalCode, String city, String error) {

        this.postalCode = postalCode;
        this.city = city;
        this.error = error;
    }

    public String getPostalCode() {

        return postalCode;
    }


    public String getCity() {

        return city;
    }


    public String getError() {

        return error;
    }
}
