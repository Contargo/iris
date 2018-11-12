package net.contargo.iris.transport.service;

import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportTemplateDto;

import java.util.List;
import java.util.function.Supplier;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public class TransportChainGeneratorStrategyAdvisor {

    private TransportChainGenerator terminalGenerator;
    private TransportChainGenerator addressGenerator;

    public TransportChainGeneratorStrategyAdvisor(TransportChainGenerator terminalGenerator,
        TransportChainGenerator addressGenerator) {

        this.terminalGenerator = terminalGenerator;
        this.addressGenerator = addressGenerator;
    }

    public Supplier<List<TransportDescriptionDto>> advice(TransportTemplateDto template) {

        if (template.isAddressOnly()) {
            return () -> addressGenerator.from(template);
        } else {
            return () -> terminalGenerator.from(template);
        }
    }
}
