package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;


/**
 * Data Transfer Object for the {@link net.contargo.iris.routedatarevision.RouteDataRevision}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class RouteDataRevisionDto {

    private Long id;

    private Terminal terminal;

    private BigDecimal truckDistanceOneWay;

    private BigDecimal tollDistanceOneWay;

    private BigDecimal airlineDistance;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private BigDecimal radius;

    public RouteDataRevisionDto(RouteDataRevision routeDataRevision) {

        id = routeDataRevision.getId();
        terminal = routeDataRevision.getTerminal();
        truckDistanceOneWay = routeDataRevision.getTruckDistanceOneWay();
        tollDistanceOneWay = routeDataRevision.getTollDistanceOneWay();
        airlineDistance = routeDataRevision.getAirlineDistance();
        latitude = routeDataRevision.getLatitude();
        longitude = routeDataRevision.getLongitude();
        radius = routeDataRevision.getRadius();
    }


    public RouteDataRevisionDto() {
    }

    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public Terminal getTerminal() {

        return terminal;
    }


    public void setTerminal(Terminal terminal) {

        this.terminal = terminal;
    }


    public BigDecimal getTruckDistanceOneWay() {

        return truckDistanceOneWay;
    }


    public void setTruckDistanceOneWay(BigDecimal truckDistanceOneWay) {

        this.truckDistanceOneWay = truckDistanceOneWay;
    }


    public BigDecimal getTollDistanceOneWay() {

        return tollDistanceOneWay;
    }


    public void setTollDistanceOneWay(BigDecimal tollDistanceOneWay) {

        this.tollDistanceOneWay = tollDistanceOneWay;
    }


    public BigDecimal getAirlineDistance() {

        return airlineDistance;
    }


    public void setAirlineDistance(BigDecimal airlineDistance) {

        this.airlineDistance = airlineDistance;
    }


    public BigDecimal getLatitude() {

        return latitude;
    }


    public void setLatitude(BigDecimal latitude) {

        this.latitude = latitude;
    }


    public BigDecimal getLongitude() {

        return longitude;
    }


    public void setLongitude(BigDecimal longitude) {

        this.longitude = longitude;
    }


    public BigDecimal getRadius() {

        return radius;
    }


    public void setRadius(BigDecimal radius) {

        this.radius = radius;
    }


    public RouteDataRevision toEntity() {

        return new RouteDataRevision(id, terminal, truckDistanceOneWay, tollDistanceOneWay, airlineDistance, latitude,
                longitude, radius);
    }
}
