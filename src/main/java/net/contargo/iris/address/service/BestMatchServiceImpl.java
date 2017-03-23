package net.contargo.iris.address.service;

import net.contargo.iris.address.nominatim.NominatimUtil;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class BestMatchServiceImpl implements BestMatchService {

    private final StaticAddressService staticAddressService;
    private final AddressService addressService;

    public BestMatchServiceImpl(StaticAddressService staticAddressService, AddressService addressService) {

        this.staticAddressService = staticAddressService;
        this.addressService = addressService;
    }

    @Override
    public Optional<BestMatch> bestMatch(String postalCode, String city, String countryCode) {

        List<StaticAddress> addresses = staticAddressService.getAddressesByDetails(postalCode, city, countryCode);

        Optional<BestMatch> bestMatch = addresses.stream().map(BestMatch::of).findFirst();

        if (bestMatch.isPresent()) {
            return bestMatch;
        }

        Map<String, String> parameterMap = NominatimUtil.parameterMap(postalCode, city, countryCode);

        return addressService.getAddressesByDetails(parameterMap).stream().map(BestMatch::of).findFirst();
    }
}
