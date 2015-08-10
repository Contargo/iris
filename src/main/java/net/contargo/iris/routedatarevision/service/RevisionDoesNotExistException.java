package net.contargo.iris.routedatarevision.service;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RevisionDoesNotExistException extends RuntimeException {

    private final String code;

    public RevisionDoesNotExistException(String message, String code) {

        super(message);
        this.code = code;
    }

    public String getCode() {

        return code;
    }
}
