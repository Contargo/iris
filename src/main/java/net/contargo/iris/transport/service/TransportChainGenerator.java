package net.contargo.iris.transport.service;

import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportTemplateDto;

import java.util.List;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface TransportChainGenerator {

    /**
     * Generates all possible {@link net.contargo.iris.transport.api.TransportDescriptionDto}s for a given
     * {@link net.contargo.iris.transport.api.TransportTemplateDto}.
     *
     * @param  template  the template
     *
     * @return  a list of {@link net.contargo.iris.transport.api.TransportDescriptionDto}
     */
    List<TransportDescriptionDto> from(TransportTemplateDto template);
}
