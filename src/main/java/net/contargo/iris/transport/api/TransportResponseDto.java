package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import net.contargo.iris.container.ContainerState;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
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
        public Integer distance;
        public Integer tollDistance;
        public Integer duration;
        public List<String> geometries;

        public TransportSegment(TransportDescriptionDto.TransportSegment transportSegment) {

            this.fromSite = new TransportSite(transportSegment.fromSite);
            this.toSite = new TransportSite(transportSegment.toSite);
            this.loadingState = transportSegment.loadingState;
            this.unitAvailable = transportSegment.unitAvailable;
            this.modeOfTransport = transportSegment.modeOfTransport;
        }
    }
}
