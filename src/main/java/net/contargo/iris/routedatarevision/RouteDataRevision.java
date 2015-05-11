package net.contargo.iris.routedatarevision;

import net.contargo.iris.terminal.Terminal;

import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

import javax.persistence.Column;
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

    private static final int MAX_VALUE_COORD = 180;
    private static final int MIN_VALUE_COORD = -180;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Terminal terminal;

    @Column(name = "truckDistanceOneWay")
    private BigDecimal truckDistanceOneWayInMeter;

    @Column(name = "tollDistanceOneWay")
    private BigDecimal tollDistanceOneWayInMeter;

    @Column(name = "airlineDistance")
    private BigDecimal airlineDistanceInMeter;

    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal latitude;

    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal longitude;

    @Column(name = "radius")
    private BigDecimal radiusInMeter;

    public RouteDataRevision() {

        // for jpa
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


    public BigDecimal getTruckDistanceOneWayInMeter() {

        return truckDistanceOneWayInMeter;
    }


    public void setTruckDistanceOneWayInMeter(BigDecimal truckDistanceOneWayInMeter) {

        this.truckDistanceOneWayInMeter = truckDistanceOneWayInMeter;
    }


    public BigDecimal getTollDistanceOneWayInMeter() {

        return tollDistanceOneWayInMeter;
    }


    public void setTollDistanceOneWayInMeter(BigDecimal tollDistanceOneWayInMeter) {

        this.tollDistanceOneWayInMeter = tollDistanceOneWayInMeter;
    }


    public BigDecimal getAirlineDistanceInMeter() {

        return airlineDistanceInMeter;
    }


    public void setAirlineDistanceInMeter(BigDecimal airlineDistanceInMeter) {

        this.airlineDistanceInMeter = airlineDistanceInMeter;
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


    public BigDecimal getRadiusInMeter() {

        return radiusInMeter;
    }


    public void setRadiusInMeter(BigDecimal radiusInMeter) {

        this.radiusInMeter = radiusInMeter;
    }
}
