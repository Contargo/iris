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

    public List<TransportDescriptionSegment> transportDescription;

    public TransportDescriptionDto(TransportDescriptionDto template) {

        this.transportDescription = template.transportDescription.stream().map(s ->
                        new TransportDescriptionSegment(s.fromSite, s.toSite, s.loadingState, s.unitAvailable,
                            s.modeOfTransport)).collect(toList());
    }


    public TransportDescriptionDto(TransportTemplateDto template) {

        this.transportDescription = template.transportDescription.stream().map(s ->
                        new TransportDescriptionSegment(s.fromSite, s.toSite, s.loadingState, s.unitAvailable,
                            s.modeOfTransport)).collect(toList());
    }


    @JsonCreator
    public TransportDescriptionDto(
        @JsonProperty("transportDescription") List<TransportDescriptionSegment> transportDescription) {

        this.transportDescription = transportDescription;
    }

    public static class TransportDescriptionSegment {

        public TransportSite fromSite;
        public TransportSite toSite;
        public ContainerState loadingState;
        public Boolean unitAvailable;
        public ModeOfTransport modeOfTransport;

        @JsonCreator
        public TransportDescriptionSegment(@JsonProperty("fromSite") TransportSite fromSite,
            @JsonProperty("toSite") TransportSite toSite,
            @JsonProperty("loadingState") ContainerState loadingState,
            @JsonProperty("unitAvailable") Boolean unitAvailable,
            @JsonProperty("modeOfTransport") ModeOfTransport modeOfTransport) {

            this.fromSite = new TransportSite(fromSite);
            this.toSite = new TransportSite(toSite);
            this.loadingState = loadingState;
            this.unitAvailable = unitAvailable == null ? true : unitAvailable;
            this.modeOfTransport = modeOfTransport;
        }
    }
}
