package net.contargo.iris.address.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.nominatim.NominatimUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class BestMatchServiceImpl implements BestMatchService {

    private final AddressServiceWrapper addressServiceWrapper;

    public BestMatchServiceImpl(AddressServiceWrapper addressServiceWrapper) {

        this.addressServiceWrapper = addressServiceWrapper;
    }

    @Override
    public Optional<BestMatch> bestMatch(String postalCode, String city, String countryCode) {

        List<AddressList> addressLists;
        addressLists = addressServiceWrapper.getAddressesBasedOnStaticAddressResolution(postalCode, city, countryCode);

        Optional<BestMatch> bestMatch = bestMatch(addressLists);

        if (bestMatch.isPresent()) {
            return bestMatch;
        }

        Map<String, String> parameters = NominatimUtil.parameterMap(postalCode, city, countryCode);
        addressLists = addressServiceWrapper.getAddressesBasedOnNominatimResolution(parameters);

        return bestMatch(addressLists);
    }


    private Optional<BestMatch> bestMatch(List<AddressList> addressLists) {

        if (addressLists.isEmpty()) {
            return Optional.empty();
        }

        Optional<Address> address = addressLists.get(0).firstAddress();

        return address.map(BestMatch::of);
    }
}
