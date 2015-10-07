package net.contargo.iris.api;

import org.springframework.http.HttpStatus;


/**
 * Should be thrown when error occurs in Rest calls. This expection will be handled and transformed to a
 * {@link RestApiErrorDto}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class RestApiException extends RuntimeException {

    private final String code;
    private final HttpStatus httpStatus;

    public RestApiException(String message, String code, HttpStatus httpStatus) {

        super(message);
        this.code = code;

        this.httpStatus = httpStatus;
    }


    public RestApiException(String message, Throwable cause, String code, HttpStatus httpStatus) {

        super(message, cause);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {

        return code;
    }


    public HttpStatus getHttpStatus() {

        return httpStatus;
    }
}
