package net.contargo.iris.transport.elevation.client.contargo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class ElevationServicePoint2D {

    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final Long osmId;

    ElevationServicePoint2D(BigDecimal latitude, BigDecimal longitude, Long osmId) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.osmId = osmId;
    }

    @JsonProperty("lat")
    public BigDecimal getLatitude() {

        return latitude;
    }


    @JsonProperty("lon")
    public BigDecimal getLongitude() {

        return longitude;
    }


    @JsonProperty("osmId")
    public Long getOsmId() {

        return osmId;
    }
}
