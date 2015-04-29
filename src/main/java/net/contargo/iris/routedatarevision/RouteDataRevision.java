package net.contargo.iris.routedatarevision;

import net.contargo.iris.terminal.Terminal;

import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 * Entity holding the information of route data revisions.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Entity
public class RouteDataRevision {

    private static final int MAX_DEC_15 = 15;
    private static final int MAX_FRAC_2 = 2;
    private static final int MIN_0 = 0;

    private static final int MAX_VALUE_COORD = 180;
    private static final int MIN_VALUE_COORD = -180;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Terminal terminal;

    private BigDecimal truckDistanceOneWay;

    private BigDecimal tollDistanceOneWay;

    private BigDecimal airlineDistance;

    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal latitude;

    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal longitude;

    private BigDecimal radius;

    public RouteDataRevision() {
    }


    public RouteDataRevision(Long id, Terminal terminal, BigDecimal truckDistanceOneWay, BigDecimal tollDistanceOneWay,
        BigDecimal airlineDistance, BigDecimal latitude, BigDecimal longitude, BigDecimal radius) {

        this.id = id;
        this.terminal = terminal;
        this.truckDistanceOneWay = truckDistanceOneWay;
        this.tollDistanceOneWay = tollDistanceOneWay;
        this.airlineDistance = airlineDistance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
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
}
