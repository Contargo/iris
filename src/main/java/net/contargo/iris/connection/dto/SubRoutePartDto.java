package net.contargo.iris.connection.dto;

import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.SubRoutePart;

import java.math.BigDecimal;


/**
 * Dto object for {@link SubRoutePart}s.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SubRoutePartDto {

    private GeoLocationDto origin;
    private GeoLocationDto destination;
    private RouteType routeType;
    private BigDecimal co2;
    private BigDecimal duration;
    private BigDecimal distance;
    private BigDecimal dieselDistance;
    private BigDecimal electricDistance;
    private BigDecimal bargeDieselDistance;
    private BigDecimal railDieselDistance;

    public SubRoutePartDto() {

        // needed for jackson instantiation
    }


    public SubRoutePartDto(SubRoutePart subRoutePart) {

        origin = GeolocationDtoFactory.createGeolocationDto(subRoutePart.getOrigin());
        destination = GeolocationDtoFactory.createGeolocationDto(subRoutePart.getDestination());
        routeType = subRoutePart.getRouteType();
        co2 = subRoutePart.getCo2();
        duration = subRoutePart.getDuration();
        distance = subRoutePart.getDistance();
        dieselDistance = subRoutePart.getDieselDistance();
        electricDistance = subRoutePart.getElectricDistance();
        bargeDieselDistance = subRoutePart.getBargeDieselDistance();
        railDieselDistance = subRoutePart.getRailDieselDistance();
    }

    public SubRoutePart toSubRoutePart() {

        SubRoutePart subRoutePart = new SubRoutePart();

        if (this.origin != null && this.destination != null) {
            subRoutePart.setDestination(this.destination.toEntity());
            subRoutePart.setOrigin(this.origin.toEntity());
        }

        subRoutePart.setRouteType(this.routeType);
        subRoutePart.setCo2(this.co2);
        subRoutePart.setDuration(this.duration);
        subRoutePart.setDistance(this.distance);
        subRoutePart.setDieselDistance(this.dieselDistance);
        subRoutePart.setElectricDistance(this.electricDistance);
        subRoutePart.setBargeDieselDistance(this.bargeDieselDistance);
        subRoutePart.setRailDieselDistance(this.railDieselDistance);

        return subRoutePart;
    }


    public GeoLocationDto getOrigin() {

        return origin;
    }


    public GeoLocationDto getDestination() {

        return destination;
    }


    public RouteType getRouteType() {

        return routeType;
    }


    public BigDecimal getCo2() {

        return co2;
    }


    public BigDecimal getDuration() {

        return duration;
    }


    public BigDecimal getDistance() {

        return distance;
    }


    public BigDecimal getDieselDistance() {

        return dieselDistance;
    }


    public BigDecimal getElectricDistance() {

        return electricDistance;
    }


    public BigDecimal getBargeDieselDistance() {

        return bargeDieselDistance;
    }


    public BigDecimal getRailDieselDistance() {

        return railDieselDistance;
    }


    public void setOrigin(GeoLocationDto origin) {

        this.origin = origin;
    }


    public void setDestination(GeoLocationDto destination) {

        this.destination = destination;
    }


    public void setRouteType(RouteType routeType) {

        this.routeType = routeType;
    }


    public void setCo2(BigDecimal co2) {

        this.co2 = co2;
    }


    public void setDuration(BigDecimal duration) {

        this.duration = duration;
    }


    public void setDistance(BigDecimal distance) {

        this.distance = distance;
    }


    public void setDieselDistance(BigDecimal dieselDistance) {

        this.dieselDistance = dieselDistance;
    }


    public void setElectricDistance(BigDecimal electricDistance) {

        this.electricDistance = electricDistance;
    }


    public void setBargeDieselDistance(BigDecimal bargeDieselDistance) {

        this.bargeDieselDistance = bargeDieselDistance;
    }


    public void setRailDieselDistance(BigDecimal railDieselDistance) {

        this.railDieselDistance = railDieselDistance;
    }
}
