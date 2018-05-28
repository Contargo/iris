package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.container.ContainerState;

import java.math.BigDecimal;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TransportTemplateDto {

    public List<TransportSegment> transportDescription;

    @JsonCreator
    public TransportTemplateDto(@JsonProperty("transportDescription") List<TransportSegment> transportDescription) {

        this.transportDescription = transportDescription;
    }

    public static class TransportSegment {

        public TransportSite fromSite;
        public TransportSite toSite;
        public ContainerState loadingState;
        public Boolean unitAvailable;
        public ModeOfTransport modeOfTransport;

        @JsonCreator
        public TransportSegment(@JsonProperty("fromSite") TransportSite fromSite,
            @JsonProperty("toSite") TransportSite toSite,
            @JsonProperty("loadingState") ContainerState loadingState,
            @JsonProperty("unitAvailable") Boolean unitAvailable,
            @JsonProperty("modeOfTransport") ModeOfTransport modeOfTransport) {

            this.fromSite = fromSite;
            this.toSite = toSite;
            this.loadingState = loadingState;
            this.unitAvailable = unitAvailable == null ? true : unitAvailable;
            this.modeOfTransport = modeOfTransport;
        }
    }

    public static class TransportSite {

        public SiteType type;
        public String uuid;
        public BigDecimal lon;
        public BigDecimal lat;

        @JsonCreator
        public TransportSite(@JsonProperty("type") SiteType type,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("lon") BigDecimal lon,
            @JsonProperty("lat") BigDecimal lat) {

            this.type = type;
            this.uuid = uuid;
            this.lon = lon;
            this.lat = lat;
        }
    }
}
