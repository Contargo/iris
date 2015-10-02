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


    @Override
    public boolean isAddressOfCountry(Address address, String countryCode) {

        String addressCountryCode = address.getCountryCode();

        return addressCountryCode != null && countryCode.equalsIgnoreCase(addressCountryCode);
    }


    private Predicate<? super AddressList> byFirstCountryCode(String countryCode) {

        return
            addressList -> {
            if (addressList.getAddresses().size() > 0) {
                Address address = addressList.getAddresses().get(0);

                return !isAddressOfCountry(address, countryCode);
            }

            return true;
        };
    }
}
