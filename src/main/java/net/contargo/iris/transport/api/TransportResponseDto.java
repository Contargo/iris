package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import net.contargo.iris.container.ContainerState;
import net.contargo.iris.units.Distance;
import net.contargo.iris.units.Duration;
import net.contargo.iris.units.Weight;

import java.util.List;
import java.util.Map;

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
        public Map<String, Distance> distancesByCountry;
        public Distance tollDistance;
        public Duration duration;
        public List<String> geometries;
        public boolean partOfRoundtrip;
        public Weight co2;

        public TransportResponseSegment(TransportDescriptionDto.TransportDescriptionSegment segment) {

            this.from = new TransportStop(segment.from);
            this.to = new TransportStop(segment.to);
            this.loadingState = segment.loadingState;
            this.unitAvailable = segment.unitAvailable;
            this.modeOfTransport = segment.modeOfTransport;
        }

        public boolean isConnectionBetween(StopType one, StopType two) {

            return (this.from.type == one && this.to.type == two) || (this.from.type == two && this.to.type == one);
        }
    }
}
