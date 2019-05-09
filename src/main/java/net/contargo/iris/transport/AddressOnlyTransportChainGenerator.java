package net.contargo.iris.transport;

import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportTemplateDto;

import java.util.List;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class AddressOnlyTransportChainGenerator implements TransportChainGenerator {

    @Override
    public List<TransportDescriptionDto> from(TransportTemplateDto template) {

        TransportDescriptionDto description = new TransportDescriptionDto(template);
        description.transportChain.forEach(s -> s.modeOfTransport = ROAD);

        return new IntermediateDescriptions(description).get();
    }
}
