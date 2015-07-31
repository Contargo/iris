package net.contargo.iris.connection.api;

import org.springframework.validation.Errors;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class ValidationException extends RuntimeException {

    private final Errors errors;

    public ValidationException(Errors errors) {

        this.errors = errors;
    }

    public Errors getErrors() {

        return errors;
    }
}
