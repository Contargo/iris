package net.contargo.iris.countries.service;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * Enum representing country codes (ISO 3166-1 alpha2 code), e.g. de for Germany.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public enum CountryCode {

    OPTIONAL("Country (optional)", ""),
    GERMANY("Germany", "DE"),
    NETHERLANDS("Netherlands", "NL"),
    BELGIUM("Belgium", "BE"),
    LUXEMBOURG("Luxembourg", "LU"),
    FRANCE("France", "FR"),
    SWITZERLAND("Switzerland", "CH"),
    LIECHTENSTEIN("Liechtenstein", "LI"),
    AUSTRIA("Austria", "AT"),
    CZECH_REPUBLIC("Czech Republic", "CZ"),
    POLAND("Poland", "PL"),
    DENMARK("Denmark", "DK");

    private final String name;
    private final String value;

    CountryCode(String name, String value) {

        this.name = name;
        this.value = value;
    }

    public String getValue() {

        return value;
    }


    public String getName() {

        return name;
    }


    public static List<CountryCode> countries() {

        return Arrays.stream(values()).filter(c -> OPTIONAL != c).collect(toList());
    }
}
