package net.contargo.iris.gis.api;

import net.contargo.iris.gis.dto.AirlineDistanceDto;

import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
class AirlineDistanceResponse extends ResourceSupport {

    private final BigDecimal distance;
    private final String unit;

    public AirlineDistanceResponse(AirlineDistanceDto airlineDistanceDto) {

        this.distance = airlineDistanceDto.getDistance();
        this.unit = airlineDistanceDto.getUnit().getName();
    }

    public BigDecimal getDistance() {

        return distance;
    }


    public String getUnit() {

        return unit;
    }
}
