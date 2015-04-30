package net.contargo.iris.terminal.service;

import java.util.List;

import static java.util.Arrays.asList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class NonUniqueTerminalException extends IllegalArgumentException {

    private final List<String> badFields;

    public NonUniqueTerminalException(String... badFields) {

        super("Terminal name and coordinates have to be unique.");
        this.badFields = asList(badFields);
    }

    public List<String> getBadFields() {

        return badFields;
    }
}
