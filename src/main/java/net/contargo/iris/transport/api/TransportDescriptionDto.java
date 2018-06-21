package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.container.ContainerState;

import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TransportDescriptionDto {

    public List<TransportDescriptionSegment> transportChain;

    public TransportDescriptionDto(TransportDescriptionDto template) {

        this.transportChain = template.transportChain.stream().map(s ->
                        new TransportDescriptionSegment(s.from, s.to, s.loadingState, s.unitAvailable,
                            s.modeOfTransport)).collect(toList());
    }


    public TransportDescriptionDto(TransportTemplateDto template) {

        this.transportChain = template.transportChain.stream().map(s ->
                        new TransportDescriptionSegment(s.from, s.to, s.loadingState, s.unitAvailable,
                            s.modeOfTransport)).collect(toList());
    }


    @JsonCreator
    public TransportDescriptionDto(@JsonProperty("transportChain") List<TransportDescriptionSegment> transportChain) {

        this.transportChain = transportChain;
    }

    public static class TransportDescriptionSegment {

        public TransportStop from;
        public TransportStop to;
        public ContainerState loadingState;
        public Boolean unitAvailable;
        public ModeOfTransport modeOfTransport;

        @JsonCreator
        public TransportDescriptionSegment(@JsonProperty("from") TransportStop from,
            @JsonProperty("to") TransportStop to,
            @JsonProperty("loadingState") ContainerState loadingState,
            @JsonProperty("unitAvailable") Boolean unitAvailable,
            @JsonProperty("modeOfTransport") ModeOfTransport modeOfTransport) {

            this.from = new TransportStop(from);
            this.to = new TransportStop(to);
            this.loadingState = loadingState;
            this.unitAvailable = unitAvailable == null ? true : unitAvailable;
            this.modeOfTransport = modeOfTransport;
        }
    }
}
