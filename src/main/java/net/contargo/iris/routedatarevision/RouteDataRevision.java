package net.contargo.iris.routedatarevision;

import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.util.DateUtil;

import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Entity holding the information of route data revisions.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Entity
public class RouteDataRevision {

    private static final int MAX_VALUE_COORD = 180;
    private static final int MIN_VALUE_COORD = -180;
    private static final int COMMENT_SIZE = 5000;
    private static final int MAX_SIZE_CITY = 255;
    private static final int MAX_SIZE_POSTAL_CODE = 10;
    private static final int MAX_SIZE_COUNTRY = 5;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Terminal terminal;

    @Column(name = "truckDistanceOneWay")
    private BigDecimal truckDistanceOneWayInKilometer;

    @Column(name = "truckDistanceOneWayDe")
    private BigDecimal truckDistanceOneWayInKilometerDe;
    @Column(name = "truckDistanceOneWayNl")
    private BigDecimal truckDistanceOneWayInKilometerNl;
    @Column(name = "truckDistanceOneWayBe")
    private BigDecimal truckDistanceOneWayInKilometerBe;
    @Column(name = "truckDistanceOneWayLu")
    private BigDecimal truckDistanceOneWayInKilometerLu;
    @Column(name = "truckDistanceOneWayFr")
    private BigDecimal truckDistanceOneWayInKilometerFr;
    @Column(name = "truckDistanceOneWayCh")
    private BigDecimal truckDistanceOneWayInKilometerCh;
    @Column(name = "truckDistanceOneWayLi")
    private BigDecimal truckDistanceOneWayInKilometerLi;
    @Column(name = "truckDistanceOneWayAt")
    private BigDecimal truckDistanceOneWayInKilometerAt;
    @Column(name = "truckDistanceOneWayCz")
    private BigDecimal truckDistanceOneWayInKilometerCz;
    @Column(name = "truckDistanceOneWayPl")
    private BigDecimal truckDistanceOneWayInKilometerPl;
    @Column(name = "truckDistanceOneWayDk")
    private BigDecimal truckDistanceOneWayInKilometerDk;

    @Column(name = "tollDistanceOneWay")
    private BigDecimal tollDistanceOneWayInKilometer;

    @Column(name = "airlineDistance")
    private BigDecimal airlineDistanceInKilometer;

    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal latitude;

    @Range(min = MIN_VALUE_COORD, max = MAX_VALUE_COORD)
    private BigDecimal longitude;

    @Column(name = "radius")
    private BigDecimal radiusInMeter;

    @Size(max = COMMENT_SIZE)
    private String comment;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date validFrom;

    @Temporal(TemporalType.DATE)
    private Date validTo;

    @Size(max = MAX_SIZE_CITY)
    private String city;

    @Size(max = MAX_SIZE_CITY)
    private String cityNormalized;

    @Size(max = MAX_SIZE_POSTAL_CODE)
    private String postalCode;

    @Size(max = MAX_SIZE_COUNTRY)
    private String country;

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


    public BigDecimal getTruckDistanceOneWayInKilometer() {

        return truckDistanceOneWayInKilometer;
    }


    public void setTruckDistanceOneWayInKilometer(BigDecimal truckDistanceOneWayInKilometer) {

        this.truckDistanceOneWayInKilometer = truckDistanceOneWayInKilometer;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerDe() {

        return truckDistanceOneWayInKilometerDe;
    }


    public void setTruckDistanceOneWayInKilometerDe(BigDecimal truckDistanceOneWayInKilometerDe) {

        this.truckDistanceOneWayInKilometerDe = truckDistanceOneWayInKilometerDe;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerNl() {

        return truckDistanceOneWayInKilometerNl;
    }


    public void setTruckDistanceOneWayInKilometerNl(BigDecimal truckDistanceOneWayInKilometerNl) {

        this.truckDistanceOneWayInKilometerNl = truckDistanceOneWayInKilometerNl;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerBe() {

        return truckDistanceOneWayInKilometerBe;
    }


    public void setTruckDistanceOneWayInKilometerBe(BigDecimal truckDistanceOneWayInKilometerBe) {

        this.truckDistanceOneWayInKilometerBe = truckDistanceOneWayInKilometerBe;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerLu() {

        return truckDistanceOneWayInKilometerLu;
    }


    public void setTruckDistanceOneWayInKilometerLu(BigDecimal truckDistanceOneWayInKilometerLu) {

        this.truckDistanceOneWayInKilometerLu = truckDistanceOneWayInKilometerLu;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerFr() {

        return truckDistanceOneWayInKilometerFr;
    }


    public void setTruckDistanceOneWayInKilometerFr(BigDecimal truckDistanceOneWayInKilometerFr) {

        this.truckDistanceOneWayInKilometerFr = truckDistanceOneWayInKilometerFr;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerCh() {

        return truckDistanceOneWayInKilometerCh;
    }


    public void setTruckDistanceOneWayInKilometerCh(BigDecimal truckDistanceOneWayInKilometerCh) {

        this.truckDistanceOneWayInKilometerCh = truckDistanceOneWayInKilometerCh;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerLi() {

        return truckDistanceOneWayInKilometerLi;
    }


    public void setTruckDistanceOneWayInKilometerLi(BigDecimal truckDistanceOneWayInKilometerLi) {

        this.truckDistanceOneWayInKilometerLi = truckDistanceOneWayInKilometerLi;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerAt() {

        return truckDistanceOneWayInKilometerAt;
    }


    public void setTruckDistanceOneWayInKilometerAt(BigDecimal truckDistanceOneWayInKilometerAt) {

        this.truckDistanceOneWayInKilometerAt = truckDistanceOneWayInKilometerAt;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerCz() {

        return truckDistanceOneWayInKilometerCz;
    }


    public void setTruckDistanceOneWayInKilometerCz(BigDecimal truckDistanceOneWayInKilometerCz) {

        this.truckDistanceOneWayInKilometerCz = truckDistanceOneWayInKilometerCz;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerPl() {

        return truckDistanceOneWayInKilometerPl;
    }


    public void setTruckDistanceOneWayInKilometerPl(BigDecimal truckDistanceOneWayInKilometerPl) {

        this.truckDistanceOneWayInKilometerPl = truckDistanceOneWayInKilometerPl;
    }


    public BigDecimal getTruckDistanceOneWayInKilometerDk() {

        return truckDistanceOneWayInKilometerDk;
    }


    public void setTruckDistanceOneWayInKilometerDk(BigDecimal truckDistanceOneWayInKilometerDk) {

        this.truckDistanceOneWayInKilometerDk = truckDistanceOneWayInKilometerDk;
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

        this.validFrom = null == validFrom ? null : new Date(validFrom.getTime());
    }


    public Date getValidTo() {

        return null == validTo ? null : new Date(validTo.getTime());
    }


    public void setValidTo(Date validTo) {

        this.validTo = null == validTo ? null : new Date(validTo.getTime());
    }


    public ValidityRange getValidityRange() {

        return new ValidityRange(DateUtil.asLocalDate(validFrom), DateUtil.asLocalDate(validTo));
    }


    public String getCity() {

        return city;
    }


    public void setCity(String city) {

        if (city != null && city.length() > MAX_SIZE_CITY) {
            throw new IllegalArgumentException("City cannot be greater then " + MAX_SIZE_CITY);
        }

        this.city = city;
    }


    public String getCityNormalized() {

        return cityNormalized;
    }


    public void setCityNormalized(String cityNormalized) {

        if (cityNormalized != null && cityNormalized.length() > MAX_SIZE_CITY) {
            throw new IllegalArgumentException("City cannot be greater then " + MAX_SIZE_CITY);
        }

        this.cityNormalized = cityNormalized;
    }


    public String getPostalCode() {

        return postalCode;
    }


    public void setPostalCode(String postalCode) {

        if (postalCode != null && postalCode.length() > MAX_SIZE_POSTAL_CODE) {
            throw new IllegalArgumentException("Country cannot be greater then " + MAX_SIZE_POSTAL_CODE);
        }

        this.postalCode = postalCode;
    }


    public String getCountry() {

        return country;
    }


    public void setCountry(String country) {

        if (country != null && country.length() > MAX_SIZE_COUNTRY) {
            throw new IllegalArgumentException("Country cannot be greater then " + MAX_SIZE_COUNTRY);
        }

        this.country = country;
    }
}
