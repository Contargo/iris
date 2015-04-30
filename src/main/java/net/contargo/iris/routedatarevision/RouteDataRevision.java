package net.contargo.iris.routedatarevision;

import net.contargo.iris.terminal.Terminal;

import net.contargo.validation.bigdecimal.BigDecimalValidate;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;


/**
 * Entity holding the information of route data revisions.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Entity
public class RouteDataRevision {

    private static final int DEG_180 = 180;

    private static final int MAX_DEC_15 = 15;
    private static final int MAX_FRAC_2 = 2;
    private static final int MAX_FRAC_10 = 10;
    private static final int MAX_DEC_3 = 3;
    private static final int MIN_0 = 0;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Terminal terminal;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC_15)
    private BigDecimal truckDistanceOneWay;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC_15)
    private BigDecimal tollDistanceOneWay;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC_15)
    private BigDecimal airlineDistance;

    @NotNull
    @BigDecimalValidate(
        minValue = -DEG_180, maxValue = DEG_180, maxFractionalPlaces = MAX_FRAC_10, maxDecimalPlaces = MAX_DEC_3
    )
    private BigDecimal latitude;

    @NotNull
    @BigDecimalValidate(
        minValue = -DEG_180, maxValue = DEG_180, maxFractionalPlaces = MAX_FRAC_10, maxDecimalPlaces = MAX_DEC_3
    )
    private BigDecimal longitude;

    @NotNull
    @BigDecimalValidate(minValue = MIN_0, maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC_15)
    private BigDecimal radius;

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
}
