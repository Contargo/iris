package net.contargo.iris.address.dto;

import net.contargo.iris.address.Address;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;


/**
 * Data Transfer Object for {@link Address}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public final class AddressDto extends GeoLocationDto {

    private static final String TYPE = "ADDRESS";

    private final String displayName;
    private final String shortName;
    private final String niceName;
    private final String countryCode;
    private final long osmId;
    private final long placeId;
    private final Map<String, String> address;
    private final String type;

    public AddressDto(Address address) {

        super(address);
        this.countryCode = address.getCountryCode();
        this.niceName = address.getNiceName();
        this.displayName = address.getDisplayName();
        this.osmId = address.getOsmId();
        this.placeId = address.getPlaceId();
        this.shortName = address.getShortName();
        this.address = unmodifiableMap(address.getAddress());
        this.type = TYPE;
    }

    public String getShortName() {

        return shortName;
    }


    public String getNiceName() {

        return niceName;
    }


    public Map<String, String> getAddress() {

        return address;
    }


    public String getDisplayName() {

        return displayName;
    }


    public long getOsmId() {

        return osmId;
    }


    public long getPlaceId() {

        return placeId;
    }


    public String getCountryCode() {

        return countryCode;
    }


    @Override
    public String getType() {

        return type;
    }


    @Override
    public boolean equals(Object obj) { // NOSONAR We don't want to change the behaviour of base class equals

        return super.equals(obj); // NOSONAR We don't want to change the behaviour of base class equals
    }


    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(super.hashCode()).toHashCode();
    }
}
