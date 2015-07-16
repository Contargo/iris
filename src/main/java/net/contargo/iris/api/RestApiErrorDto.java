package net.contargo.iris.api;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RestApiErrorDto {

    private final String code;

    private final String message;

    public RestApiErrorDto(String code, String message) {

        this.code = code;
        this.message = message;
    }

    public String getCode() {

        return code;
    }


    public String getMessage() {

        return message;
    }
}
