package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.units.Distance;

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

        public final TransportStop from;
        public final TransportStop to;
        public final ContainerState loadingState;
        public final boolean unitAvailable;
        public final ModeOfTransport modeOfTransport;
        public Distance distance;
        public Distance tollDistance;
        public Integer duration;
        public List<String> geometries;
        public BigDecimal co2;

        public TransportResponseSegment(TransportDescriptionDto.TransportDescriptionSegment segment) {

            this.from = new TransportStop(segment.from);
            this.to = new TransportStop(segment.to);
            this.loadingState = segment.loadingState;
            this.unitAvailable = segment.unitAvailable;
            this.modeOfTransport = segment.modeOfTransport;
        }
    }
}
