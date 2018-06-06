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
public class TransportSite {

    public SiteType type;
    public String uuid;
    public BigDecimal lon;
    public BigDecimal lat;

    @JsonCreator
    public TransportSite(@JsonProperty("type") SiteType type,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("lat") BigDecimal lat,
        @JsonProperty("lon") BigDecimal lon) {

        this.type = type;
        this.uuid = uuid;
        this.lon = lon;
        this.lat = lat;
    }


    TransportSite(TransportSite site) {

        this.type = site.type;
        this.uuid = site.uuid;
        this.lat = site.lat;
        this.lon = site.lon;
    }
}
