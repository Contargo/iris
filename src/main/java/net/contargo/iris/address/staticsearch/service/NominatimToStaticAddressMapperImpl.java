package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import java.util.List;

import javax.transaction.Transactional;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Transactional
public class NominatimToStaticAddressMapperImpl implements NominatimToStaticAddressMapper {

    private final StaticAddressMappingProcessor mappingProcessor;
    private final StaticAddressSelector selector;

    public NominatimToStaticAddressMapperImpl(StaticAddressMappingProcessor mappingProcessor,
        StaticAddressSelector selector) {

        this.mappingProcessor = mappingProcessor;
        this.selector = selector;
    }

    @Override
    public StaticAddress map(Address address) {

        List<StaticAddress> staticAddresses = mappingProcessor.process(address);

        if (staticAddresses.isEmpty()) {
            throw new NominatimToStaticAddressMapperException(address);
        }

        if (staticAddresses.size() == 1) {
            return staticAddresses.get(0);
        }

        return selector.select(staticAddresses, address);
    }
}
