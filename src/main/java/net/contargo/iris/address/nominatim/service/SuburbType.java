package net.contargo.iris.address.nominatim.service;

/**
 * Enum for suburb types [suburb|administrative|village].
 *
 * @author  Michael Herbold - herbold@synyx.de
 */
enum SuburbType {

    ADDRESSES("addresses"),
    SUBURB("suburb"),
    ADMINISTRATIVE("administrative"),
    VILLAGE("village");

    private String type;

    private SuburbType(String type) {

        this.type = type;
    }

    public String getType() {

        return this.type;
    }
}
