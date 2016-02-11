package net.contargo.iris.address.staticsearch.service;

/**
 * Is thrown when a static address was not found in the database.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class StaticAddressNotFoundException extends RuntimeException {

    private static final String ERROR_CODE = "staticaddress.not.found";

    public StaticAddressNotFoundException() {
    }


    public StaticAddressNotFoundException(String message) {

        super(message);
    }

    public String getErrorCode() {

        return ERROR_CODE;
    }
}
