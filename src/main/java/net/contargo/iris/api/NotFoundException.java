package net.contargo.iris.api;

/**
 * Should be thrown when an entity cannot be found. Will be mapped to an HTTP 404 error.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {

        super(message);
    }
}
