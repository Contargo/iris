package net.contargo.iris.api;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@ControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    ResponseEntity<RestApiErrorDto> handleRevisionDoesNotExistException(RestApiException e) {

        return new ResponseEntity<>(new RestApiErrorDto(e.getCode(), e.getMessage()), e.getHttpStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<RestApiErrorDto> processValidationError(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        ValidationErrorDTO validationErrorDTO = processFieldErrors(fieldErrors);

        return new ResponseEntity<>(new RestApiErrorDto("validation.error",
                    "Validation of request body detected errors", validationErrorDTO), BAD_REQUEST);
    }


    private ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {

        ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();

        for (FieldError fieldError : fieldErrors) {
            validationErrorDTO.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return validationErrorDTO;
    }
}
