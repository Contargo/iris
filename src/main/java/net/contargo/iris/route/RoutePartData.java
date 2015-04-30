package net.contargo.iris.route;

import java.math.BigDecimal;


/**
 * This class represents a collection of attributes associated with a {@link RoutePart}. Any of these attributes might
 * be set by (one or more) enricher object(s).
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @see  RoutePart
 */
public class RoutePartData {

    private BigDecimal airLineDistance;
    private BigDecimal distance;
    private BigDecimal dieselDistance;
    private BigDecimal electricDistance;
    private BigDecimal tollDistance;
    private BigDecimal duration;

    private BigDecimal co2;

    public BigDecimal getAirLineDistance() {

        return airLineDistance;
    }


    public void setAirLineDistance(BigDecimal airLineDistance) {

        this.airLineDistance = airLineDistance;
    }


    public BigDecimal getDistance() {

        return distance;
    }


    public void setDistance(BigDecimal distance) {

        this.distance = distance;
    }


    public BigDecimal getDieselDistance() {

        return dieselDistance;
    }


    public void setDieselDistance(BigDecimal dieselDistance) {

        this.dieselDistance = dieselDistance;
    }


    public BigDecimal getElectricDistance() {

        return electricDistance;
    }


    public void setElectricDistance(BigDecimal electricDistance) {

        this.electricDistance = electricDistance;
    }


    public BigDecimal getTollDistance() {

        return tollDistance;
    }


    public void setTollDistance(BigDecimal tollDistance) {

        this.tollDistance = tollDistance;
    }


    public BigDecimal getDuration() {

        return duration;
    }


    public void setDuration(BigDecimal duration) {

        this.duration = duration;
    }


    public BigDecimal getCo2() {

        return co2;
    }


    public void setCo2(BigDecimal co2) {

        this.co2 = co2;
    }
}
