package net.contargo.iris.address.staticsearch;

import com.google.common.base.Strings;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import org.hibernate.validator.constraints.NotBlank;

import org.springframework.util.StringUtils;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.Size;


/**
 * Entity for static addresses (csv list).
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
@Entity(name = "StaticAddress")
// NOSONAR Don't want to override equals
public class StaticAddress extends GeoLocation {

    public static final String STATIC_ID = "static_id";
    public static final String HASH_KEY = "hashkey";

    private static final int POSTAL_CODE_MAX_SIZE = 10;
    private static final int COUNTRY_MAX_SIZE = 5;
    private static final int HASHKEY_MAX_SIZE = 5;
    private static final int CITY_MAX_SIZE = 255;
    private static final int SUBURB_MAX_SIZE = 255;
    private static final int HASH_KEY_SIZE = 5;
    private static final int BASE_36 = 36;
    private static final int BASE_10 = 10;
    private static final int BEGIN_INDEX_0 = 0;
    private static final int END_INDEX_4 = 4;
    private static final int SHIFT_20 = 20;
    private static final int LENGTH_AUTOINCREMENTED_PART = 12;
    private static final int BINARY_111111 = 63;
    private static final int SIZE_TO_THE_PAD = 16;

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = COUNTRY_MAX_SIZE)
    private String country;

    @NotBlank
    @Size(max = POSTAL_CODE_MAX_SIZE)
    private String postalcode;

    @NotBlank
    @Size(max = CITY_MAX_SIZE)
    private String city;

    @NotBlank
    @Size(max = CITY_MAX_SIZE)
    private String cityNormalized;

    @Size(max = SUBURB_MAX_SIZE)
    private String suburb;

    @Size(max = SUBURB_MAX_SIZE)
    private String suburbNormalized;

    @Size(max = HASHKEY_MAX_SIZE)
    private String hashKey;

    private BigInteger uniqueId;

    public Address toAddress() {

        Address address = new Address();

        injectDisplayName(address);
        address.getAddress().put(Address.COUNTRY_CODE, getCountry());

        if (postalcode != null) {
            address.getAddress().put("postcode", postalcode);
        }

        if (city != null) {
            address.getAddress().put("city", city);
        }

        if (suburb != null) {
            address.getAddress().put("suburb", suburb);
        }

        address.getAddress().put(HASH_KEY, getHashKey());
        address.getAddress().put(STATIC_ID, uniqueId == null ? null : uniqueId.toString());
        address.setLongitude(getLongitude());
        address.setLatitude(getLatitude());

        return address;
    }


    private void injectDisplayName(Address address) {

        if (StringUtils.hasText(suburb)) {
            address.setDisplayName(String.format("%s %s (%s)", extractString(postalcode), extractString(city),
                    extractString(suburb)));
        } else {
            address.setDisplayName(String.format("%s %s", extractString(postalcode), extractString(city)));
        }
    }


    private String extractString(String value) {

        return value == null || value.isEmpty() ? "" : value;
    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public String getCountry() {

        return country;
    }


    public void setCountry(String country) {

        this.country = country;
    }


    public String getPostalcode() {

        return postalcode;
    }


    public void setPostalcode(String postalcode) {

        this.postalcode = postalcode;
    }


    public String getCity() {

        return city;
    }


    public void setCity(String city) {

        this.city = city;
    }


    public String getCityNormalized() {

        return cityNormalized;
    }


    public void setCityNormalized(String cityNormalized) {

        this.cityNormalized = cityNormalized;
    }


    public String getSuburb() {

        return suburb;
    }


    public void setSuburb(String suburb) {

        this.suburb = suburb;
    }


    public String getSuburbNormalized() {

        return suburbNormalized;
    }


    public void setSuburbNormalized(String suburbNormalized) {

        this.suburbNormalized = suburbNormalized;
    }


    public String getHashKey() {

        if (hashKey == null || "".equals(hashKey)) {
            hashKey = generateHashKey();
        }

        return hashKey;
    }


    /**
     * Generates a quasi unique "hashKey" from this {@link StaticAddress}'s uniqueId. The Hash Key is no real hash but
     * a mapping. The Hash Key consists of a 5 digit Base36 number. 5 digit Base36 fits into 26 bit binary. The Hash
     * Key is assembled in binary by Java bitwise operators and then converted to Base36.
     *
     * <p>Assembly of the 26 bit binary HashKey:</p>
     *
     * <p>The first 6 bits are the first 4 digits of the uniqueId ( representing the systemId) mapped to a 6 digit
     * binary number. This is only duplicate-free for a small number of possible systemIds.</p>
     *
     * <p>The last 20 bits are the autoincremented last 12 digits of the uniqueId. This works up to 1048575 (2^20)</p>
     *
     * @return  the uniqueId mapped to a Base36 "hashKey"
     */
    private String generateHashKey() {

        if (this.uniqueId == null) {
            return "";
        }

        String uniqueIdString = this.uniqueId.toString();
        uniqueIdString = org.apache.commons.lang.StringUtils.leftPad(uniqueIdString, SIZE_TO_THE_PAD, '0');

        long systemId = Long.valueOf(uniqueIdString.substring(BEGIN_INDEX_0, END_INDEX_4));
        long systemIdInBinary = mapToSixBits(systemId);
        long uniqueIdAutoIncrementedPartInBinary = getAutoIncrementedPart(uniqueIdString);

        long hashKeyInBinary = joinBinaryNumbers(systemIdInBinary, uniqueIdAutoIncrementedPartInBinary);

        return convertToBase36(hashKeyInBinary);
    }


    private String convertToBase36(long hashkeyInBinary) {

        String hashKeyInBase36 = conv(String.valueOf(hashkeyInBinary), BASE_10, BASE_36);
        hashKeyInBase36 = Strings.padStart(hashKeyInBase36, HASH_KEY_SIZE, '0');

        return hashKeyInBase36.toUpperCase();
    }


    private long getAutoIncrementedPart(String uniqueIdString) {

        return Long.valueOf(uniqueIdString.substring(uniqueIdString.length() - LENGTH_AUTOINCREMENTED_PART + 1,
                    uniqueIdString.length()));
    }


    private long joinBinaryNumbers(long systemIdInBit, long uniqueIdAutoIncrementedPart) {

        // Append 20 0-bits to the systemId so there is room for adding a larger number (up to 20 bits)
        long finalNumberOfBitsWithSystemId = systemIdInBit << SHIFT_20;

        // fill the 0-bits with the AutoIncremented part of the uniqueId. Works for every number not larger than 2^20
        return finalNumberOfBitsWithSystemId | uniqueIdAutoIncrementedPart;
    }


    private long mapToSixBits(long systemId) {

        // Has to map the systemId to exactly 6 bits without duplication
        // Get the last 6 bits of the systemId by applying a binary AND with 111111. We need the 6 last bits because
        // the currently known systemIds have conflicts in the last 5 bits.
        return systemId & BINARY_111111;
    }


    public BigInteger getUniqueId() {

        return uniqueId;
    }


    public void setUniqueId(BigInteger uniqueId) {

        this.uniqueId = uniqueId;
        hashKey = generateHashKey();
    }


    String conv(String input, int fromBase, int toBase) {

        try {
            int inputInFromBase = Integer.parseInt(input, fromBase);

            return Integer.toString(inputInFromBase, toBase);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    @Override
    public boolean equals(Object obj) {

        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }


    @Override
    public int hashCode() {

        return new HashCodeBuilder().appendSuper(super.hashCode()).toHashCode();
    }


    @Override
    public String toString() {

        return "StaticAddress [id=" + id + ", country=" + country
            + ", postalCode=" + postalcode + ", city=" + city
            + ", cityNormalized=" + cityNormalized + ", suburb=" + suburb
            + ", suburbNormalized=" + suburbNormalized + ", latitude=" + getLatitude() + ", longitude="
            + getLongitude()
            + "]";
    }


    /**
     * Check for changed address parameters (suburb, city, postalcode).
     *
     * @param  staticAddress  to check
     *
     * @return  true, if address parameters are different
     */
    public boolean areAddressParametersDifferent(StaticAddress staticAddress) {

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.suburb, staticAddress.getSuburb());
        builder.append(this.city, staticAddress.getCity());
        builder.append(this.postalcode, staticAddress.getPostalcode());

        return !builder.isEquals();
    }


    /**
     * Check for changed parameters longitude and latitude.
     *
     * @param  staticAddress  to check
     *
     * @return  true, if parameters are different
     */
    public boolean areLatitudeAndLongitudeDifferent(StaticAddress staticAddress) {

        boolean latitudeEqual = this.getLatitude().compareTo(staticAddress.getLatitude()) == 0;
        boolean longitudeEqual = this.getLongitude().compareTo(staticAddress.getLongitude()) == 0;

        return !(latitudeEqual && longitudeEqual);
    }
}
