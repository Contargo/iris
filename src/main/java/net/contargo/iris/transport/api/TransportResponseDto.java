package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import net.contargo.iris.container.ContainerState;

import java.math.BigDecimal;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TransportResponseDto {

    public final List<TransportSegment> transportDescription;

    public TransportResponseDto(TransportDescriptionDto template) {

        this.transportDescription = template.transportDescription.stream()
                .map(TransportSegment::new)
                .collect(toList());
    }

    @JsonInclude(value = NON_NULL)
    public static class TransportSegment {

        public final TransportSite fromSite;
        public final TransportSite toSite;
        public final ContainerState loadingState;
        public final boolean unitAvailable;
        public final ModeOfTransport modeOfTransport;
        public BigDecimal distance;
        public BigDecimal tollDistance;
        public BigDecimal duration;
        public List<String> geometries;

        public TransportSegment(TransportDescriptionDto.TransportSegment transportSegment) {

            this.fromSite = new TransportSite(transportSegment.fromSite);
            this.toSite = new TransportSite(transportSegment.toSite);
            this.loadingState = transportSegment.loadingState;
            this.unitAvailable = transportSegment.unitAvailable;
            this.modeOfTransport = transportSegment.modeOfTransport;
        }
    }

    @JsonInclude(NON_NULL)
    public static class TransportSite {

        public final SiteType type;
        public final String uuid;
        public final BigDecimal lon;
        public final BigDecimal lat;

        public TransportSite(TransportDescriptionDto.TransportSite site) {

            this.type = site.type;
            this.uuid = site.uuid;
            this.lon = site.lon;
            this.lat = site.lat;
        }
    }
}
