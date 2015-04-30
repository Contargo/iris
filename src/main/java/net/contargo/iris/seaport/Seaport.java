package net.contargo.iris.seaport;

import net.contargo.iris.GeoLocation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.Size;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Entity(name = "Seaport")
public class Seaport extends GeoLocation {

    private static final int MAX_NAME_SIZE = 254;

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Size(max = MAX_NAME_SIZE)
    private String name;

    private boolean enabled = false;

    private BigInteger uniqueId;

    public Seaport() {

        // JPA entity classes must have a no-arg constructor
    }


    public Seaport(GeoLocation geoLocation) {

        this.setLatitude(geoLocation.getLatitude());
        this.setLongitude(geoLocation.getLongitude());
    }

    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public String getName() {

        return name;
    }


    public void setName(String name) {

        this.name = name;
    }


    public boolean isEnabled() {

        return enabled;
    }


    public BigInteger getUniqueId() {

        return uniqueId;
    }


    public void setUniqueId(BigInteger uniqueId) {

        this.uniqueId = uniqueId;
    }


    @Override
    public String getNiceName() {

        return getName();
    }


    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
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

        return new ToStringBuilder(this).append("id", id).append("name", name).append("enabled", enabled).toString();
    }
}
