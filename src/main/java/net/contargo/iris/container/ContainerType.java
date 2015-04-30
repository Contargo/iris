package net.contargo.iris.container;

/**
 * Enum for type of containers.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */

public enum ContainerType {

    NOT_SET(""),
    TWENTY_LIGHT("twenty_light"),
    TWENTY_HEAVY("twenty_heavy"),
    THIRTY(""),
    FORTY("forty"),
    FORTYFIVE("");

    private String csvString;

    private ContainerType(String csvString) {

        this.csvString = csvString;
    }

    public boolean isUnknown() {

        return this.equals(NOT_SET);
    }


    public String getCsvString() {

        return this.csvString;
    }
}
