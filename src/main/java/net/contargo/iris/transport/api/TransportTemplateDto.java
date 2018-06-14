package net.contargo.iris.transport.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.container.ContainerState;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TransportTemplateDto {

    public List<TransportTemplateSegment> transportChain;

    @JsonCreator
    public TransportTemplateDto(@JsonProperty("transportChain") List<TransportTemplateSegment> transportChain) {

        this.transportChain = transportChain;
    }

    public static class TransportTemplateSegment {

        public TransportSite fromSite;
        public TransportSite toSite;
        public ContainerState loadingState;
        public Boolean unitAvailable;
        public ModeOfTransport modeOfTransport;

        @JsonCreator
        public TransportTemplateSegment(@JsonProperty("fromSite") TransportSite fromSite,
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
}
