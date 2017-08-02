package net.contargo.iris.connection.service;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public class DuplicateMainRunConnectionException extends RuntimeException {

    private static final String ERROR_CODE = "mainrunconnection.duplicate";

    public DuplicateMainRunConnectionException() {

        super("Mainrun connection with given seaport, terminal and route type exists");
    }

    public String getErrorCode() {

        return ERROR_CODE;
    }
}
