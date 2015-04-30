package net.contargo.iris.seaport.service;

import java.util.List;

import static java.util.Arrays.asList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class NonUniqueSeaportException extends IllegalArgumentException {

    private final List<String> badFields;

    public NonUniqueSeaportException(String... badFields) {

        super("Seaport name and coordinates have to be unique.");
        this.badFields = asList(badFields);
    }

    public List<String> getBadFields() {

        return badFields;
    }
}
