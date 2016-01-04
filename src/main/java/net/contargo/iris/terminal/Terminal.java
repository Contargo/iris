package net.contargo.iris.terminal;

import net.contargo.iris.GeoLocation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.Size;


/**
 * Represents a terminal.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Entity(name = "Terminal")
public class Terminal extends GeoLocation {

    private static final int MAX_NAME_SIZE = 254;

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Size(max = MAX_NAME_SIZE)
    private String name;

    private boolean enabled = false;

    @Enumerated(EnumType.STRING)
    private Region region = Region.NOT_SET;

    private BigInteger uniqueId;

    public Terminal() {

        // JPA entity classes must have a no-arg constructor
    }


    public Terminal(GeoLocation geoLocation) {

        this.setLatitude(geoLocation.getLatitude());
        this.setLongitude(geoLocation.getLongitude());
    }

    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public boolean isEnabled() {

        return enabled;
    }


    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }


    public String getName() {

        return name;
    }


    public void setName(String name) {

        this.name = name;
    }


    public BigInteger getUniqueId() {

        return uniqueId;
    }


    public void setUniqueId(BigInteger uniqueId) {

        this.uniqueId = uniqueId;
    }


    public Region getRegion() {

        return region;
    }


    public void setRegion(Region region) {

        this.region = region;
    }


    @Override
    public String getNiceName() {

        return getName();
    }


    @Override
    public boolean equals(Object o) {

        return new EqualsBuilder().appendSuper(super.equals(o)).isEquals();
    }


    @Override
    public int hashCode() {

        return new HashCodeBuilder().appendSuper(super.hashCode()).toHashCode();
    }


    @Override
    public String toString() {

        return "Terminal{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", enabled=" + enabled
            + ", region=" + region
            + ", uniqueId=" + uniqueId
            + "} " + super.toString();
    }
}
