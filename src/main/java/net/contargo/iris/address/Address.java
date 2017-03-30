package net.contargo.iris.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.staticsearch.StaticAddress;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Represents an address, which is delivered by an address resolution provider (nominatim).
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address extends GeoLocation {

    public static final String COUNTRY_CODE = "country_code";
    private static final String DISPLAY_NAME = "display_name";
    private static final String PLACE_ID = "place_id";
    private static final String OSM_ID = "osm_id";
    private static final String SHORT_NAME = "short_name";
    private static final String SUBURB = "suburb";
    private static final String CITY = "city";
    private static final String POSTCODE = "postcode";

    @JsonProperty(DISPLAY_NAME)
    private String displayName;

    @JsonProperty(OSM_ID)
    private long osmId;

    @JsonProperty(PLACE_ID)
    private long placeId;

    @JsonProperty(SHORT_NAME)
    private String shortName;

    private Map<String, String> address = new HashMap<>();

    public Address(String displayName) {

        super();
        this.displayName = displayName;
    }


    public Address() {

        // needed in test.
    }


    public Address(BigDecimal latitude, BigDecimal longitude) {

        super(latitude, longitude);
    }

    public String getDisplayName() {

        return displayName;
    }


    public void setDisplayName(String displayName) {

        this.displayName = displayName;
    }


    public String getShortName() {

        return shortName;
    }


    public void setShortName(String shortName) {

        this.shortName = shortName;
    }


    public Map<String, String> getAddress() {

        return address;
    }


    public void setAddress(Map<String, String> address) {

        this.address = address;
    }


    public long getOsmId() {

        return osmId;
    }


    public void setOsmId(long osmId) {

        this.osmId = osmId;
    }


    public long getPlaceId() {

        return placeId;
    }


    public void setPlaceId(long placeId) {

        this.placeId = placeId;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Address oth = (Address) o;

        return new EqualsBuilder().append(displayName, oth.displayName)
            .append(osmId, oth.osmId)
            .append(placeId, oth.placeId)
            .append(shortName, oth.shortName)
            .appendSuper(super.equals(o))
            .isEquals();
    }


    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(displayName)
            .append(osmId)
            .append(placeId)
            .append(shortName)
            .appendSuper(super.hashCode())
            .toHashCode();
    }


    public String getCountryCode() {

        return this.address.get(COUNTRY_CODE);
    }


    public String getPostcode() {

        return this.address.get(POSTCODE);
    }


    public String getCity() {

        String village = this.address.get("village");
        String town = this.address.get("town");
        String city = this.address.get(CITY);

        return Stream.of(city, town, village).filter(value -> !StringUtils.isEmpty(value)).findFirst().orElse(null);
    }


    /**
     * Combines city, town and village by joining them with a comma if present.
     *
     * @return  the joined string
     */
    public String getCombinedCity() {

        String village = this.address.get("village");
        String town = this.address.get("town");
        String suburb = this.address.get(SUBURB);
        String city = this.address.get(CITY);

        return Stream.of(city, town, village, suburb)
            .filter(value -> !StringUtils.isEmpty(value))
            .collect(Collectors.joining(", "));
    }


    public String getSuburb() {

        return this.address.get(SUBURB);
    }


    @Override
    public String getNiceName() {

        String addressText = getAddressText();

        if (StringUtils.hasLength(addressText)) {
            return addressText;
        }

        String name = getShortName();

        if (StringUtils.hasLength(name)) {
            return name;
        }

        return getDisplayName();
    }


    private String getAddressText() {

        String postalCode = address.get("postcode");

        if (postalCode == null) {
            postalCode = address.get("boundary");
        }

        if (address.containsKey(CITY) && postalCode != null) {
            if (StringUtils.hasText(address.get(SUBURB))) {
                return String.format("%s %s (%s)", postalCode, address.get(CITY), address.get(SUBURB));
            } else {
                return String.format("%s %s", postalCode, address.get(CITY));
            }
        }

        return null;
    }


    public boolean isStatic() {

        return address.get(StaticAddress.STATIC_ID) != null;
    }


    public Optional<String> getHashKey() {

        return Optional.ofNullable(address.get(StaticAddress.HASH_KEY));
    }


    public boolean inSwitzerland() {

        return this.getCountryCode() != null && "CH".equals(this.getCountryCode().toUpperCase());
    }


    @Override
    public String toString() {

        return "Address [displayName=" + displayName + ", osmId=" + osmId
            + ", placeId=" + placeId + ", shortName=" + shortName
            + ", address=" + address + "]";
    }
}
