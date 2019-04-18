package net.contargo.iris.transport.elevation.dto;

import java.math.BigDecimal;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class Point2D {

    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final Long osmId;

    public Point2D(BigDecimal latitude, BigDecimal longitude, Long osmId) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.osmId = osmId;
    }

    public BigDecimal getLatitude() {

        return latitude;
    }


    public BigDecimal getLongitude() {

        return longitude;
    }


    public Long getOsmId() {

        return osmId;
    }
}
