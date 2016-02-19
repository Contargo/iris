package net.contargo.iris.address.staticsearch.service;

/**
 * Is thrown when a static address was not found in the database.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class StaticAddressNotFoundException extends RuntimeException {

    public StaticAddressNotFoundException(String message) {

        super(message);
    }
}
