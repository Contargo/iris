package net.contargo.iris.address.dto;

import net.contargo.iris.GeoLocation;

import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;


/**
 * Data Transfer Object for {@link GeoLocation}.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
public class GeoLocationDto {

    private static final int MAX_VALUE_COORD = 180;
    private static final int MIN_VALUE_COORD = -180;
    private static final String TYPE = "GEOLOCATION";

    @NotNull
    @Range(min = -MAX_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal latitude;

    @NotNull
    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal longitude;

    public GeoLocationDto(GeoLocation geoLocation) {

        if (geoLocation != null) {
            this.latitude = geoLocation.getLatitude();
            this.longitude = geoLocation.getLongitude();
        }
    }


    public GeoLocationDto() {

        // Needed for Jackson Mapping
    }

    public GeoLocation toEntity() {

        return new GeoLocation(this.latitude, this.longitude);
    }


    // Setters are needed so this DTO can be used as @ModelAttribute in Spring MVC
    public void setLatitude(BigDecimal latitude) {

        this.latitude = latitude;
    }


    public void setLongitude(BigDecimal longitude) {

        this.longitude = longitude;
    }


    public BigDecimal getLatitude() {

        return latitude;
    }


    public BigDecimal getLongitude() {

        return longitude;
    }


    public String getType() {

        return TYPE;
    }
}
