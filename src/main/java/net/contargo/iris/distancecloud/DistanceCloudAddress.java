package net.contargo.iris.distancecloud;

import net.contargo.iris.address.staticsearch.StaticAddress;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Container for addresses, which are used to describe Addresses within a cloud around a certain GeoLocation.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class DistanceCloudAddress {

    private String city;
    private String postalcode;
    private String suburb;
    private BigDecimal distance;
    private BigDecimal tollDistance;
    private BigDecimal airLineDistanceMeter;
    private String country;
    private String errorMessage = "";
    private String hashKey;
    private BigInteger uniqueId;

    public DistanceCloudAddress(StaticAddress staticAddress) {

        this.city = staticAddress.getCity();
        this.suburb = staticAddress.getSuburb();
        this.postalcode = staticAddress.getPostalcode();
        this.country = staticAddress.getCountry();
        this.hashKey = staticAddress.getHashKey();
        this.uniqueId = staticAddress.getUniqueId();
    }

    public String getErrorMessage() {

        return errorMessage;
    }


    public void setErrorMessage(String errorMessage) {

        this.errorMessage = errorMessage;
    }


    public String getCity() {

        return city;
    }


    public void setCity(String city) {

        this.city = city;
    }


    public String getPostalcode() {

        return postalcode;
    }


    public void setPostalcode(String postalcode) {

        this.postalcode = postalcode;
    }


    public String getSuburb() {

        return suburb;
    }


    public void setSuburb(String suburb) {

        this.suburb = suburb;
    }


    public BigDecimal getDistance() {

        return distance;
    }


    public void setDistance(BigDecimal distance) {

        this.distance = distance;
    }


    public BigDecimal getTollDistance() {

        return tollDistance;
    }


    public void setTollDistance(BigDecimal tollDistance) {

        this.tollDistance = tollDistance;
    }


    public BigDecimal getAirLineDistanceMeter() {

        return airLineDistanceMeter;
    }


    public void setAirLineDistanceMeter(BigDecimal airLineDistanceMeter) {

        this.airLineDistanceMeter = airLineDistanceMeter;
    }


    public String getCountry() {

        return country;
    }


    public void setCountry(String country) {

        this.country = country;
    }


    public String getHashKey() {

        return hashKey;
    }


    public void setHashKey(String hashKey) {

        this.hashKey = hashKey;
    }


    public BigInteger getUniqueId() {

        return uniqueId;
    }


    public void setUniqueId(BigInteger uniqueId) {

        this.uniqueId = uniqueId;
    }
}
