package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import net.contargo.validation.bigdecimal.BigDecimalValidate;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;


/**
 * Data Transfer Object for the {@link net.contargo.iris.routedatarevision.RouteDataRevision}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class RouteDataRevisionDto {

    private static final int MAX_DEC_15 = 15;
    private static final int MAX_FRAC_2 = 2;
    private static final int MIN_0 = 0;

    private Long id;

    @NotNull
    private Terminal terminal;

    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC_15)
    @NotNull
    private BigDecimal truckDistanceOneWay;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC_15)
    private BigDecimal tollDistanceOneWay;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC_15)
    private BigDecimal airlineDistance;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    @BigDecimalValidate(minValue = MIN_0, maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC_15)
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
