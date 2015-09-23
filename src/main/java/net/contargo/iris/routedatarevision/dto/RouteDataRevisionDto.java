package net.contargo.iris.routedatarevision.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import net.contargo.validation.bigdecimal.BigDecimalValidate;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Data Transfer Object for the {@link net.contargo.iris.routedatarevision.RouteDataRevision}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class RouteDataRevisionDto {

    private static final int MAX_DEC = 13;
    private static final int MAX_FRAC_2 = 2;
    private static final int MIN_0 = 0;
    private static final int COMMENT_SIZE = 5000;
    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private Long id;

    @NotNull
    @JsonIgnore
    private TerminalDto terminal;

    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC)
    @NotNull
    private BigDecimal truckDistanceOneWayInMeter;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC)
    private BigDecimal tollDistanceOneWayInMeter;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC)
    private BigDecimal airlineDistanceInMeter;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    @BigDecimalValidate(minValue = MIN_0, maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC)
    private BigDecimal radiusInMeter;

    @Size(max = COMMENT_SIZE)
    private String comment;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date validFrom;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date validTo;

    public RouteDataRevisionDto(RouteDataRevision routeDataRevision) {

        id = routeDataRevision.getId();
        terminal = new TerminalDto(routeDataRevision.getTerminal());
        truckDistanceOneWayInMeter = routeDataRevision.getTruckDistanceOneWayInMeter();
        tollDistanceOneWayInMeter = routeDataRevision.getTollDistanceOneWayInMeter();
        airlineDistanceInMeter = routeDataRevision.getAirlineDistanceInMeter();
        latitude = routeDataRevision.getLatitude();
        longitude = routeDataRevision.getLongitude();
        radiusInMeter = routeDataRevision.getRadiusInMeter();
        comment = routeDataRevision.getComment();
        validFrom = routeDataRevision.getValidFrom();
        validTo = routeDataRevision.getValidTo();
    }


    public RouteDataRevisionDto() {

        // for json serialization
    }

    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
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


    public RouteDataRevision toEntity(Long terminalId) {

        Terminal terminalEntity = terminal.toEntity();
        terminalEntity.setId(terminalId);

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setId(id);
        routeDataRevision.setTerminal(terminalEntity);
        routeDataRevision.setTruckDistanceOneWayInMeter(truckDistanceOneWayInMeter);
        routeDataRevision.setTollDistanceOneWayInMeter(tollDistanceOneWayInMeter);
        routeDataRevision.setAirlineDistanceInMeter(airlineDistanceInMeter);
        routeDataRevision.setLatitude(latitude);
        routeDataRevision.setLongitude(longitude);
        routeDataRevision.setRadiusInMeter(radiusInMeter);
        routeDataRevision.setComment(comment);
        routeDataRevision.setValidFrom(validFrom);
        routeDataRevision.setValidTo(validTo);

        return routeDataRevision;
    }


    public TerminalDto getTerminal() {

        return terminal;
    }


    public void setTerminal(TerminalDto terminal) {

        this.terminal = terminal;
    }


    public String getComment() {

        return comment;
    }


    public void setComment(String comment) {

        this.comment = comment;
    }


    public String getTerminalUid() {

        return terminal.getUniqueId();
    }


    public Date getValidFrom() {

        return null == validFrom ? null : new Date(validFrom.getTime());
    }


    public void setValidFrom(Date validFrom) {

        this.validFrom = new Date(validFrom.getTime());
    }


    public Date getValidTo() {

        return null == validTo ? null : new Date(validTo.getTime());
    }


    public void setValidTo(Date validTo) {

        this.validTo = new Date(validTo.getTime());
    }
}
