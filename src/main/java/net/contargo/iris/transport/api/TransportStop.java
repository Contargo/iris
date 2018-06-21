package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@JsonInclude(NON_NULL)
public class TransportStop {

    public StopType type;
    public String uuid;
    public BigDecimal lon;
    public BigDecimal lat;

    @JsonCreator
    public TransportStop(@JsonProperty("type") StopType type,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("lat") BigDecimal lat,
        @JsonProperty("lon") BigDecimal lon) {

        this.type = type;
        this.uuid = uuid;
        this.lon = lon;
        this.lat = lat;
    }


    TransportStop(TransportStop stop) {

        this.type = stop.type;
        this.uuid = stop.uuid;
        this.lat = stop.lat;
        this.lon = stop.lon;
    }
}
