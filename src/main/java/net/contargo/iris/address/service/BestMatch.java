package net.contargo.iris.address.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.dto.GeoLocationDto;

import java.math.BigDecimal;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author Ben Antony - antony@synyx.de
 */
public class BestMatch {

    private final String hashKey;
    private final GeoLocationDto geoLocation;
    private final String postalCode;
    private final String city;
    private final String countryCode;
    private final String suburb;

    protected BestMatch(String hashKey, BigDecimal latitude, BigDecimal longitude, String postalCode, String city,
                        String countryCode, String suburb) {

        this.hashKey = hashKey;
        GeoLocationDto geoLocationDto = new GeoLocationDto();
        geoLocationDto.setLatitude(latitude);
        geoLocationDto.setLongitude(longitude);
        this.geoLocation = geoLocationDto;
        this.postalCode = postalCode;
        this.city = city;
        this.countryCode = countryCode;
        this.suburb = suburb;
    }

    static BestMatch of(Address address) {

        String hashKey = address.getHashKey().orElse(null);

        return new BestMatch(hashKey, address.getLatitude(), address.getLongitude(), address.getPostcode(),
                address.getCity(), address.getCountryCode(), address.getSuburb());
    }


    public String getHashKey() {

        return hashKey;
    }


    public GeoLocationDto getGeoLocation() {

        return geoLocation;
    }

    public String getPostalCode() {

        return postalCode;
    }


    public String getCity() {

        return city;
    }


    public String getCountryCode() {

        return countryCode;
    }

    public String getSuburb() {
        return suburb;
    }
}
