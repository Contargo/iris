package net.contargo.iris.countries.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * View Bean for exposing countries over the API.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
public class CountryDto {

    private String name;
    private String value;

    public CountryDto(String name, String value) {

        this.name = name;
        this.value = value;
    }


    public CountryDto() {

        // Needed for Jackson Mapping
    }

    public String getName() {

        return name;
    }


    public String getValue() {

        return value;
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof CountryDto)) {
            return false;
        }

        CountryDto other = (CountryDto) obj;

        return new EqualsBuilder().append(this.hashCode(), other.hashCode()).append(this.name, other.name).append(
                this.value, other.value).isEquals();
    }


    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(this.name).append(this.value).toHashCode();
    }
}
