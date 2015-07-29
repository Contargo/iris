package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;

import java.math.BigDecimal;


/**
 * Represents a part of a {@link RoutePart}. This is only relevant if the part is of {@link RouteType} BARGE_RAIL.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SubRoutePart {

    private GeoLocation origin;
    private GeoLocation destination;
    private RouteType routeType;
    private BigDecimal co2;
    private BigDecimal duration;
    private BigDecimal distance;
    private BigDecimal dieselDistance;
    private BigDecimal electricDistance;
    private BigDecimal bargeDieselDistance;
    private BigDecimal railDieselDistance;

    public GeoLocation getOrigin() {

        return origin;
    }


    public void setOrigin(GeoLocation origin) {

        this.origin = origin;
    }


    public GeoLocation getDestination() {

        return destination;
    }


    public void setDestination(GeoLocation destination) {

        this.destination = destination;
    }


    public RouteType getRouteType() {

        return routeType;
    }


    public void setRouteType(RouteType routeType) {

        this.routeType = routeType;
    }


    public BigDecimal getCo2() {

        return co2;
    }


    public void setCo2(BigDecimal co2) {

        this.co2 = co2;
    }


    public BigDecimal getDuration() {

        return duration;
    }


    public void setDuration(BigDecimal duration) {

        this.duration = duration;
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


    public BigDecimal getBargeDieselDistance() {

        return bargeDieselDistance;
    }


    public void setBargeDieselDistance(BigDecimal bargeDieselDistance) {

        this.bargeDieselDistance = bargeDieselDistance;
    }


    public BigDecimal getRailDieselDistance() {

        return railDieselDistance;
    }


    public void setRailDieselDistance(BigDecimal railDieselDistance) {

        this.railDieselDistance = railDieselDistance;
    }
}
