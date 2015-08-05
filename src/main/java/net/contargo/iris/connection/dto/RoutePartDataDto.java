package net.contargo.iris.connection.dto;

import net.contargo.iris.route.RoutePartData;

import java.math.BigDecimal;


/**
 * Dto for {@link RoutePartData}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RoutePartDataDto {

    private BigDecimal airlineDistance;
    private BigDecimal distance;
    private BigDecimal dieselDistance;
    private BigDecimal bargeDieselDistance;
    private BigDecimal railDieselDistance;
    private BigDecimal electricDistance;
    private BigDecimal tollDistance;
    private BigDecimal duration;
    private BigDecimal co2;

    public RoutePartDataDto() {

        // needed for Spring MVC instantiation of Controller parameter
    }


    public RoutePartDataDto(RoutePartData data) {

        if (data != null) {
            this.airlineDistance = data.getAirLineDistance();
            this.distance = data.getDistance();
            this.dieselDistance = data.getDieselDistance();
            this.bargeDieselDistance = data.getBargeDieselDistance();
            this.railDieselDistance = data.getRailDieselDistance();
            this.electricDistance = data.getElectricDistance();
            this.tollDistance = data.getTollDistance();
            this.duration = data.getDuration();
            this.co2 = data.getCo2();
        }
    }

    public RoutePartData toRoutePartData() {

        RoutePartData routePartData = new RoutePartData();
        routePartData.setAirLineDistance(airlineDistance);
        routePartData.setDistance(distance);
        routePartData.setDieselDistance(dieselDistance);
        routePartData.setBargeDieselDistance(bargeDieselDistance);
        routePartData.setRailDieselDistance(railDieselDistance);
        routePartData.setElectricDistance(electricDistance);
        routePartData.setTollDistance(tollDistance);
        routePartData.setDuration(duration);
        routePartData.setCo2(co2);

        return routePartData;
    }


    public BigDecimal getAirlineDistance() {

        return airlineDistance;
    }


    public BigDecimal getDistance() {

        return distance;
    }


    public BigDecimal getDieselDistance() {

        return dieselDistance;
    }


    public BigDecimal getBargeDieselDistance() {

        return bargeDieselDistance;
    }


    public BigDecimal getRailDieselDistance() {

        return railDieselDistance;
    }


    public BigDecimal getElectricDistance() {

        return electricDistance;
    }


    public BigDecimal getTollDistance() {

        return tollDistance;
    }


    public BigDecimal getDuration() {

        return duration;
    }


    public BigDecimal getCo2() {

        return co2;
    }
}
