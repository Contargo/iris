package net.contargo.iris.api;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
public class RestApiErrorDto {

    private final String code;
    private final String message;
    private final ValidationErrorDTO validationError;

    public RestApiErrorDto(String code, String message) {

        this.code = code;
        this.message = message;
        this.validationError = new ValidationErrorDTO();
    }


    public RestApiErrorDto(String code, String message, ValidationErrorDTO validationError) {

        this.code = code;
        this.message = message;
        this.validationError = validationError;
    }

    public String getCode() {

        return code;
    }


    public String getMessage() {

        return message;
    }


    public ValidationErrorDTO getValidationError() {

        return validationError;
    }
}
