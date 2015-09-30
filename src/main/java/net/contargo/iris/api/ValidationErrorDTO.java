package net.contargo.iris.api;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Sandra Thiema - thiene@synyx.de
 */
public class ValidationErrorDTO {

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public void addFieldError(String path, String message) {

        FieldErrorDTO error = new FieldErrorDTO(path, message);
        fieldErrors.add(error);
    }


    public List<FieldErrorDTO> getFieldErrors() {

        return fieldErrors;
    }


    public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {

        this.fieldErrors = fieldErrors;
    }
}
