package net.contargo.iris.address.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class AddressListFilterImpl implements AddressListFilter {

    @Override
    public List<AddressList> filterOutByCountryCode(List<AddressList> addressLists, String countryCode) {

        return addressLists.stream().filter(byFirstCountryCode(countryCode)).collect(Collectors.toList());
    }


    private Predicate<? super AddressList> byFirstCountryCode(String countryCode) {

        String countryCodeUpper = countryCode.toUpperCase();

        return
            addressList -> {
            if (addressList.getAddresses().size() > 0) {
                Address address = addressList.getAddresses().get(0);

                String addressCountryCode = address.getAddress().get(Address.COUNTRY_CODE);

                if (addressCountryCode != null) {
                    return !countryCodeUpper.equals(addressCountryCode.toUpperCase());
                }
            }

            return true;
        };
    }
}
