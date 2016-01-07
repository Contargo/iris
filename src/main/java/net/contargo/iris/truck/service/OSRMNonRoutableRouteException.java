package net.contargo.iris.truck.service;

/**
 * Thrown when a OSRMJsonResponse getting a non routable route.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class OSRMNonRoutableRouteException extends RuntimeException {

    public OSRMNonRoutableRouteException(String message) {

        super(message);
    }
}
