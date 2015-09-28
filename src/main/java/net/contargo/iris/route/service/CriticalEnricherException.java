package net.contargo.iris.route.service;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class CriticalEnricherException extends Exception {

    public CriticalEnricherException(String message) {

        super(message);
    }


    public CriticalEnricherException(String message, Throwable t) {

        super(message, t);
    }
}
