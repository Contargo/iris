package net.contargo.iris.truck.service;

/**
 * Thrown when a OSRMJsonResponse getting a non routable route.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class OSRMNonRoutableRouteException extends RuntimeException {

    private static final long serialVersionUID = -4222863212840480759L;

    public OSRMNonRoutableRouteException(String message) {

        super(message);
    }


    public OSRMNonRoutableRouteException(String message, Exception cause) {

        super(message, cause);
    }
}
