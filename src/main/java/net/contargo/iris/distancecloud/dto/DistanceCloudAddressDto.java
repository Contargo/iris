package net.contargo.iris.distancecloud.dto;

import net.contargo.iris.distancecloud.DistanceCloudAddress;

import java.math.BigDecimal;


/**
 * Container for addresses, which are used to describe Addresses within a cloud around a certain GeoLocation.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public final class DistanceCloudAddressDto {

    private final String city;
    private final String postalcode;
    private final String suburb;
    private final BigDecimal distance;
    private final BigDecimal tollDistance;
    private final BigDecimal airLineDistanceMeter;
    private final String country;
    private final String errorMessage;
    private final String hashKey;
    private final String uniqueId;

    public DistanceCloudAddressDto(DistanceCloudAddress address) {

        this.city = address.getCity();
        this.suburb = address.getSuburb();
        this.postalcode = address.getPostalcode();
        this.country = address.getCountry();
        this.hashKey = address.getHashKey();
        this.distance = address.getDistance();
        this.tollDistance = address.getTollDistance();
        this.airLineDistanceMeter = address.getAirLineDistanceMeter();
        this.errorMessage = address.getErrorMessage();
        this.uniqueId = String.valueOf(address.getUniqueId());
    }

    public String getErrorMessage() {

        return errorMessage;
    }


    public String getCity() {

        return city;
    }


    public String getPostalcode() {

        return postalcode;
    }


    public String getSuburb() {

        return suburb;
    }


    public BigDecimal getDistance() {

        return distance;
    }


    public BigDecimal getTollDistance() {

        return tollDistance;
    }


    public BigDecimal getAirLineDistanceMeter() {

        return airLineDistanceMeter;
    }


    public String getCountry() {

        return country;
    }


    public String getHashKey() {

        return hashKey;
    }


    public String getUniqueId() {

        return uniqueId;
    }
}
