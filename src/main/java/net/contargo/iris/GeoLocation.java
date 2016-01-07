package net.contargo.iris;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.util.BigDecimalComparator;

import org.apache.commons.lang.builder.HashCodeBuilder;

import org.hibernate.validator.constraints.Range;

import org.springframework.format.number.NumberStyleFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.NumberFormat;

import java.util.Locale;

import javax.persistence.MappedSuperclass;

import javax.validation.constraints.NotNull;


/**
 * This class represents a Geolocation.
 *
 * <p>The equal method tests if the gps coordinates are the same.</p>
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@MappedSuperclass
public class GeoLocation {

    private static final int SCALE = 10;
    private static final int MAX_VALUE_COORD = 180;
    private static final int MIN_VALUE_COORD = -180;

    @JsonProperty("lat")
    @NotNull
    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal latitude;

    @JsonProperty("lon")
    @NotNull
    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal longitude;

    public GeoLocation() {

        // default constructor to call from subclasses
    }


    public GeoLocation(BigDecimal latitude, BigDecimal longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }


    public GeoLocation(double plat, double plon) {

        double lat = plat;
        double lon = plon;

        while (lon > MAX_VALUE_COORD) {
            lon = lon - MAX_VALUE_COORD;
        }

        while (lat > MAX_VALUE_COORD) {
            lat = lat - MAX_VALUE_COORD;
        }

        while (lon < 0) {
            lon = lon + MAX_VALUE_COORD;
        }

        while (lat < 0) {
            lat = lat + MAX_VALUE_COORD;
        }

        this.latitude = new BigDecimal(lat);
        this.longitude = new BigDecimal(lon);
    }

    public BigDecimal getLatitude() {

        if (latitude == null) {
            return null;
        }

        return latitude.setScale(SCALE, RoundingMode.HALF_EVEN);
    }


    public void setLatitude(BigDecimal latitude) {

        this.latitude = latitude;
    }


    public BigDecimal getLongitude() {

        if (longitude == null) {
            return null;
        }

        return longitude.setScale(SCALE, RoundingMode.HALF_EVEN);
    }


    public void setLongitude(BigDecimal longitude) {

        this.longitude = longitude;
    }


    public String getNiceName() {

        NumberStyleFormatter nf = new NumberStyleFormatter("#0.0#####");
        NumberFormat numberFormat = nf.getNumberFormat(Locale.getDefault());

        return numberFormat.format(getLatitude()) + ":" + numberFormat.format(getLongitude());
    }


    @Override
    public int hashCode() {

        HashCodeBuilder builder = new HashCodeBuilder();

        if (latitude != null) {
            builder.append(latitude.doubleValue());
        }

        if (longitude != null) {
            builder.append(longitude.doubleValue());
        }

        return builder.toHashCode();
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GeoLocation)) {
            return false;
        }

        GeoLocation other = (GeoLocation) obj;

        BigDecimalComparator comparator = new BigDecimalComparator();

        return !(comparator.compare(this.getLatitude(), other.getLatitude()) != 0
                || comparator.compare(this.getLongitude(), other.getLongitude()) != 0);
    }


    @Override
    public String toString() {

        return "GeoLocationImpl [latitude=" + latitude + ", longitude=" + longitude + "]";
    }


    /**
     * Calculates the Bounding-Box with this as center and a "radius" of given distance in kilometers.
     *
     * <p>This returns two GeoLocations as BoundingBox that wraps the lower left and upper right of the box</p>
     *
     * <pre>
     +-------------------------X   result[1]
     |                         |
     |            X            |
     |                         |
     X-------------------------+   result[0]

     * </pre>
     *
     * @param  distanceInKm  distance in km
     *
     * @return  BoundingBox of the given distanceInKm
     */
    public BoundingBox getBoundingBox(Double distanceInKm) {

        return new BoundingBox(this, distanceInKm);
    }
}
