package net.contargo.iris.address.nominatim.service;

/**
 * @author  Arnold Franke - franke@synyx.de *
 */
public enum AddressDetailKey {

    STREET("street"),
    POSTAL_CODE("postalcode"),
    CITY("city"),
    COUNTRY("country"),
    NAME("name");

    private String key;

    private AddressDetailKey(String key) {

        this.key = key;
    }

    public String getKey() {

        return key;
    }
}
