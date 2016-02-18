package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import java.util.List;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class PostcodeStaticAddressMappingProcessor extends StaticAddressMappingProcessor {

    private final StaticAddressService staticAddressService;

    PostcodeStaticAddressMappingProcessor(StaticAddressMappingProcessor next,
        StaticAddressService staticAddressService) {

        super(next);
        this.staticAddressService = staticAddressService;
    }

    @Override
    List<StaticAddress> map(Address address) {

        return staticAddressService.findByPostalcodeAndCountry(address.getPostcode(), address.getCountryCode());
    }
}
