package net.contargo.iris.routedatarevision.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import net.contargo.validation.bigdecimal.BigDecimalValidate;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Data Transfer Object for the {@link net.contargo.iris.routedatarevision.RouteDataRevision}.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class RouteDataRevisionDto {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final int MAX_DEC = 13;
    private static final int MAX_FRAC_2 = 2;
    private static final int MIN_0 = 0;
    private static final int COMMENT_SIZE = 5000;

    private Long id;

    @JsonIgnore
    private TerminalDto terminal;

    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC)
    @NotNull
    private BigDecimal truckDistanceOneWayInKilometer;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC)
    private BigDecimal tollDistanceOneWayInKilometer;

    @NotNull
    @BigDecimalValidate(maxFractionalPlaces = MAX_FRAC_2, maxDecimalPlaces = MAX_DEC)
    private BigDecimal airlineDistanceInKilometer;

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
    @DateTimeFormat(pattern = DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = "CET")
    private Date validFrom;

    @DateTimeFormat(pattern = DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = "CET")
    private Date validTo;

    @JsonIgnore
    private String country;

    @JsonIgnore
    private String city;

    @JsonIgnore
    private String postalCode;

    public RouteDataRevisionDto(RouteDataRevision routeDataRevision) {

        id = routeDataRevision.getId();
        terminal = new TerminalDto(routeDataRevision.getTerminal());
        truckDistanceOneWayInKilometer = routeDataRevision.getTruckDistanceOneWayInKilometer();
        tollDistanceOneWayInKilometer = routeDataRevision.getTollDistanceOneWayInKilometer();
        airlineDistanceInKilometer = routeDataRevision.getAirlineDistanceInKilometer();
        latitude = routeDataRevision.getLatitude();
        longitude = routeDataRevision.getLongitude();
        radiusInMeter = routeDataRevision.getRadiusInMeter();
        comment = routeDataRevision.getComment();
        validFrom = routeDataRevision.getValidFrom();
        validTo = routeDataRevision.getValidTo();
        country = routeDataRevision.getCountry();
        city = routeDataRevision.getCity();
        postalCode = routeDataRevision.getPostalCode();
    }


    public RouteDataRevisionDto() {

        // for json serialization
    }


    @JsonCreator
    public RouteDataRevisionDto(@JsonProperty("terminalUid") String terminalUniqueId) {

        terminal = new TerminalDto();
        terminal.setUniqueId(terminalUniqueId);
    }

    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public BigDecimal getTruckDistanceOneWayInKilometer() {

        return truckDistanceOneWayInKilometer;
    }


    public void setTruckDistanceOneWayInKilometer(BigDecimal truckDistanceOneWayInKilometer) {

        this.truckDistanceOneWayInKilometer = truckDistanceOneWayInKilometer;
    }


    public BigDecimal getTollDistanceOneWayInKilometer() {

        return tollDistanceOneWayInKilometer;
    }


    public void setTollDistanceOneWayInKilometer(BigDecimal tollDistanceOneWayInKilometer) {

        this.tollDistanceOneWayInKilometer = tollDistanceOneWayInKilometer;
    }


    public BigDecimal getAirlineDistanceInKilometer() {

        return airlineDistanceInKilometer;
    }


    public void setAirlineDistanceInKilometer(BigDecimal airlineDistanceInKilometer) {

        this.airlineDistanceInKilometer = airlineDistanceInKilometer;
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
        routeDataRevision.setTruckDistanceOneWayInKilometer(truckDistanceOneWayInKilometer);
        routeDataRevision.setTollDistanceOneWayInKilometer(tollDistanceOneWayInKilometer);
        routeDataRevision.setAirlineDistanceInKilometer(airlineDistanceInKilometer);
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


    public Date getValidFrom() {

        return null == validFrom ? null : new Date(validFrom.getTime());
    }


    public void setValidFrom(Date validFrom) {

        this.validFrom = validFrom == null ? null : new Date(validFrom.getTime());
    }


    public Date getValidTo() {

        return null == validTo ? null : new Date(validTo.getTime());
    }


    public void setValidTo(Date validTo) {

        this.validTo = validTo == null ? null : new Date(validTo.getTime());
    }


    public String getTerminalUid() {

        return null == terminal ? null : terminal.getUniqueId();
    }


    public String getCountry() {

        return country;
    }


    public String getCity() {

        return city;
    }


    public String getPostalCode() {

        return postalCode;
    }
}
