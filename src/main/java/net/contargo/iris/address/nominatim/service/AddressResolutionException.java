package net.contargo.iris.address.nominatim.service;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class AddressResolutionException extends RuntimeException {

    public AddressResolutionException(String msg, Throwable t) {

        super(msg, t);
    }
}
