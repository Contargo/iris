package net.contargo.iris.transport.elevation.dto;

import java.math.BigDecimal;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class Point3D {

    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final Integer elevation;

    public Point3D(Integer elevation, BigDecimal latitude, BigDecimal longitude) {

        this.elevation = elevation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getElevation() {

        return elevation;
    }


    public BigDecimal getLatitude() {

        return latitude;
    }


    public BigDecimal getLongitude() {

        return longitude;
    }
}
