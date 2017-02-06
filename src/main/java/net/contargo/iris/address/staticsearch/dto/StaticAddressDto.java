package net.contargo.iris.address.staticsearch.dto;

import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.address.staticsearch.StaticAddress;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class StaticAddressDto {

    private String uniqueId;
    private GeoLocationDto geoLocation;
    private String country;
    private String postalcode;
    private String city;
    private String suburb;
    private String hashKey;

    StaticAddressDto(StaticAddress staticAddress) {

        this.uniqueId = staticAddress.getUniqueId().toString();
        this.geoLocation = new GeoLocationDto(staticAddress);
        this.country = staticAddress.getCountry();
        this.postalcode = staticAddress.getPostalcode();
        this.city = staticAddress.getCity();
        this.suburb = staticAddress.getSuburb();
        this.hashKey = staticAddress.getHashKey();
    }


    public StaticAddressDto(String uniqueId, GeoLocationDto geoLocation) {

        this.uniqueId = uniqueId;
        this.geoLocation = geoLocation;
    }

    public String getUniqueId() {

        return uniqueId;
    }


    public GeoLocationDto getGeoLocation() {

        return geoLocation;
    }


    public String getCountry() {

        return country;
    }


    public String getPostalcode() {

        return postalcode;
    }


    public String getCity() {

        return city;
    }


    public String getSuburb() {

        return suburb;
    }


    public String getHashKey() {

        return hashKey;
    }
}
