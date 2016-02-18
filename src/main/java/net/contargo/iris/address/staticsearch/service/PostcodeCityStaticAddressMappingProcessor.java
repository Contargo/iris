package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.normalizer.NormalizerService;

import java.util.List;

import static net.contargo.iris.address.staticsearch.service.PostcodeMappingProcessorUtil.byCity;
import static net.contargo.iris.address.staticsearch.service.PostcodeMappingProcessorUtil.isEmptySuburb;

import static java.util.stream.Collectors.toList;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class PostcodeCityStaticAddressMappingProcessor extends StaticAddressMappingProcessor {

    private final StaticAddressService staticAddressService;
    private final NormalizerService normalizerService;

    PostcodeCityStaticAddressMappingProcessor(StaticAddressMappingProcessor next,
        StaticAddressService staticAddressService, NormalizerService normalizerService) {

        super(next);
        this.normalizerService = normalizerService;
        this.staticAddressService = staticAddressService;
    }

    @Override
    List<StaticAddress> map(Address address) {

        String postcode = address.getPostcode();
        String country = address.getCountryCode();
        String normalizedCity = normalizerService.normalize(address.getCity());

        List<StaticAddress> addresses = staticAddressService.findByPostalcodeAndCountry(postcode, country);

        return addresses.stream().filter(byCity(normalizedCity)).filter(isEmptySuburb()).collect(toList());
    }
}
