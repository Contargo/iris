package net.contargo.iris.address.w3w;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.GeoLocation;

import java.math.BigDecimal;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
class ForwardW3wResponse {

    private final W3wResponseGeometry geometry;
    private final W3wResponseStatus status;

    @JsonCreator
    ForwardW3wResponse(@JsonProperty("geometry") W3wResponseGeometry geometry,
        @JsonProperty("status") W3wResponseStatus status) {

        this.geometry = geometry;
        this.status = status;
    }

    GeoLocation toGeolocation() {

        return new GeoLocation(geometry.lat, geometry.lon);
    }


    boolean error() {

        return status.code != null;
    }


    Integer errorCode() {

        return status.code;
    }


    String errorMessage() {

        return status.message;
    }

    private static class W3wResponseGeometry {

        private final BigDecimal lat;
        private final BigDecimal lon;

        @JsonCreator
        private W3wResponseGeometry(@JsonProperty("lat") BigDecimal lat,
            @JsonProperty("lng") BigDecimal lon) {

            this.lat = lat;
            this.lon = lon;
        }
    }

    private static class W3wResponseStatus {

        private final Integer code;
        private final String message;

        @JsonCreator
        private W3wResponseStatus(@JsonProperty("code") Integer code,
            @JsonProperty("message") String message) {

            this.code = code;
            this.message = message;
        }
    }
}
