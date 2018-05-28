package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.container.ContainerState;

import java.math.BigDecimal;

import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TransportDescriptionDto {

    public List<TransportSegment> transportDescription;

    public TransportDescriptionDto(TransportDescriptionDto template) {

        this.transportDescription = template.transportDescription.stream()
                .map(TransportDescriptionDto.TransportSegment::new)
                .collect(toList());
    }


    public TransportDescriptionDto(TransportTemplateDto template) {

        this.transportDescription = template.transportDescription.stream()
                .map(TransportDescriptionDto.TransportSegment::new)
                .collect(toList());
    }


    @JsonCreator
    public TransportDescriptionDto(
        @JsonProperty("transportDescription") List<TransportDescriptionDto.TransportSegment> transportDescription) {

        this.transportDescription = transportDescription;
    }

    public static class TransportSegment {

        public TransportSite fromSite;
        public TransportSite toSite;
        public ContainerState loadingState;
        public Boolean unitAvailable;
        public ModeOfTransport modeOfTransport;

        public TransportSegment(TransportDescriptionDto.TransportSegment transportSegment) {

            this.fromSite = new TransportDescriptionDto.TransportSite(transportSegment.fromSite);
            this.toSite = new TransportDescriptionDto.TransportSite(transportSegment.toSite);
            this.loadingState = transportSegment.loadingState;
            this.unitAvailable = transportSegment.unitAvailable;
            this.modeOfTransport = transportSegment.modeOfTransport;
        }


        public TransportSegment(TransportTemplateDto.TransportSegment transportSegment) {

            this.fromSite = new TransportDescriptionDto.TransportSite(transportSegment.fromSite);
            this.toSite = new TransportDescriptionDto.TransportSite(transportSegment.toSite);
            this.loadingState = transportSegment.loadingState;
            this.unitAvailable = transportSegment.unitAvailable;
            this.modeOfTransport = transportSegment.modeOfTransport;
        }


        @JsonCreator
        public TransportSegment(@JsonProperty("fromSite") TransportDescriptionDto.TransportSite fromSite,
            @JsonProperty("toSite") TransportDescriptionDto.TransportSite toSite,
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

        public TransportSite(TransportDescriptionDto.TransportSite site) {

            this.type = site.type;
            this.uuid = site.uuid;
            this.lon = site.lon;
            this.lat = site.lat;
        }


        public TransportSite(TransportTemplateDto.TransportSite site) {

            this.type = site.type;
            this.uuid = site.uuid;
            this.lon = site.lon;
            this.lat = site.lat;
        }


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
