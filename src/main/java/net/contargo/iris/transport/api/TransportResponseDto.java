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
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TransportResponseDto {

    public final List<TransportResponseSegment> transportChain;

    public TransportResponseDto(TransportDescriptionDto template) {

        this.transportChain = template.transportChain.stream().map(TransportResponseSegment::new).collect(toList());
    }

    @JsonInclude(value = NON_NULL)
    public static class TransportResponseSegment {

        public final TransportSite fromSite;
        public final TransportSite toSite;
        public final ContainerState loadingState;
        public final boolean unitAvailable;
        public final ModeOfTransport modeOfTransport;
        public Integer distance;
        public Integer tollDistance;
        public Integer duration;
        public List<String> geometries;
        public BigDecimal co2;

        public TransportResponseSegment(TransportDescriptionDto.TransportDescriptionSegment segment) {

            this.fromSite = new TransportSite(segment.fromSite);
            this.toSite = new TransportSite(segment.toSite);
            this.loadingState = segment.loadingState;
            this.unitAvailable = segment.unitAvailable;
            this.modeOfTransport = segment.modeOfTransport;
        }
    }
}
