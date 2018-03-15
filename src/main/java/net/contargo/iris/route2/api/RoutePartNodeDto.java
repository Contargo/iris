package net.contargo.iris.route2.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class RoutePartNodeDto {

    private final RoutePartNodeDtoType type;
    private final BigInteger uuid;
    private final String hashKey;
    private final BigDecimal lat;
    private final BigDecimal lon;

    @JsonCreator
    public RoutePartNodeDto(@JsonProperty("type") RoutePartNodeDtoType type,
        @JsonProperty("uuid") BigInteger uuid,
        @JsonProperty("hashKey") String hashKey,
        @JsonProperty("lat") BigDecimal lat,
        @JsonProperty("lon") BigDecimal lon) {

        this.type = type;
        this.uuid = uuid;
        this.hashKey = hashKey;
        this.lat = lat;
        this.lon = lon;
    }

    public RoutePartNodeDtoType getType() {

        return type;
    }


    public BigInteger getUuid() {

        return uuid;
    }


    public String getHashKey() {

        return hashKey;
    }


    public BigDecimal getLat() {

        return lat;
    }


    public BigDecimal getLon() {

        return lon;
    }


    @Override
    public String toString() {

        String result;

        switch (type) {
            case TERMINAL:
                result = "{terminal: '" + uuid + "'}";
                break;

            case SEAPORT:

                result = "{seaport: '" + uuid + "'}";
                break;

            case ADDRESS:
                result = "{address: '" + hashKey + "'}";
                break;

            case GEOLOCATION:
                result = "{geolocation: {lat: " + lat + ", lon: " + lon + "}}";
                break;

            default:
                throw new IllegalStateException("Unknown RoutePartNodeDtoType: " + type);
        }

        return result;
    }
}
