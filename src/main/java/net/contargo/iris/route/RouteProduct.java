package net.contargo.iris.route;

/**
 * Represents a type of transport. It's either a oneway transport or a roundtrip transport.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public enum RouteProduct {

    ONEWAY,
    ROUNDTRIP;

    public static RouteProduct fromIsRoundtrip(boolean isRoundtrip) {

        if (isRoundtrip) {
            return ROUNDTRIP;
        } else {
            return ONEWAY;
        }
    }
}
