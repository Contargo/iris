package net.contargo.iris.countries.service;

/**
 * Enum representing country codes (ISO 3166-1 alpha2 code), e.g. de for Germany.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
enum CountryCode {

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

    private String name;
    private String value;

    private CountryCode(String name, String value) {

        this.name = name;
        this.value = value;
    }

    public String getValue() {

        return value;
    }


    public String getName() {

        return name;
    }
}
