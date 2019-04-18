package net.contargo.iris.transport.elevation.client.contargo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class ElevationServicePoint3D {

    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final Long osmId;
    private final Integer elevation;

    @JsonCreator
    public ElevationServicePoint3D(@JsonProperty("lat") BigDecimal latitude,
        @JsonProperty("lon") BigDecimal longitude,
        @JsonProperty("osmId") Long osmId,
        @JsonProperty("elevation") int elevation) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.osmId = osmId;
        this.elevation = elevation;
    }

    BigDecimal getLatitude() {

        return latitude;
    }


    BigDecimal getLongitude() {

        return longitude;
    }


    Long getOsmId() {

        return osmId;
    }


    Integer getElevation() {

        return elevation;
    }
}
