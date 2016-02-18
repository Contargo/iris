package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.normalizer.NormalizerService;

import java.util.List;

import static net.contargo.iris.address.staticsearch.service.PostcodeMappingProcessorUtil.byCity;
import static net.contargo.iris.address.staticsearch.service.PostcodeMappingProcessorUtil.bySuburb;

import static java.util.stream.Collectors.toList;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class PostcodeCitySuburbStaticAddressMappingProcessor extends StaticAddressMappingProcessor {

    private final StaticAddressService staticAddressService;
    private final NormalizerService normalizerService;

    PostcodeCitySuburbStaticAddressMappingProcessor(StaticAddressMappingProcessor next,
        StaticAddressService staticAddressService, NormalizerService normalizerService) {

        super(next);
        this.staticAddressService = staticAddressService;
        this.normalizerService = normalizerService;
    }

    @Override
    List<StaticAddress> map(Address address) {

        String postalcode = address.getPostcode();
        String country = address.getCountryCode();
        String normalizedCity = normalizerService.normalize(address.getCity());
        String normalizedSuburb = normalizerService.normalize(address.getSuburb());

        List<StaticAddress> addresses = staticAddressService.findByPostalcodeAndCountry(postalcode, country);

        return addresses.stream().filter(byCity(normalizedCity)).filter(bySuburb(normalizedSuburb)).collect(toList());
    }
}
