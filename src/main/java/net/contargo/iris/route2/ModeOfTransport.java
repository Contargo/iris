package net.contargo.iris.route2;

/**
 * @author  Ben Antony - antony@synyx.de
 */
public enum ModeOfTransport {

    ROAD("driving"),
    WATER("water"),
    RAIL("rail");

    private final String osrmProfile;

    ModeOfTransport(String osrmProfile) {

        this.osrmProfile = osrmProfile;
    }

    public String getOsrmProfile() {

        return osrmProfile;
    }
}
