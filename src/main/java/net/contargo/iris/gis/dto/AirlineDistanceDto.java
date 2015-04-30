package net.contargo.iris.gis.dto;

import java.math.BigDecimal;


/**
 * DTO class that encapsulates information about airline distance and its unit. Also contains the scope information that
 * may be accessed when this DTO gets mapped to an object of type
 * {@link net.contargo.iris.gis.api.AirlineDistanceResponse}.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
public class AirlineDistanceDto {

    private final BigDecimal distance;
    private final AirlineDistanceUnit unit;
    private final String scope;

    public AirlineDistanceDto(BigDecimal distance, AirlineDistanceUnit unit, String scope) {

        this.distance = distance;
        this.unit = unit;
        this.scope = scope;
    }

    public BigDecimal getDistance() {

        return distance;
    }


    public AirlineDistanceUnit getUnit() {

        return unit;
    }


    public String getScope() {

        return scope;
    }
}
