package net.contargo.iris.address.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;

import java.util.List;


/**
 * Responsible for filtering address lists.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public interface AddressListFilter {

    /**
     * Filters the given list so that it does not contain a Address with the given countryCode.
     *
     * @param  addressLists  the Addresses to filter.
     * @param  countryCode  the value to filter out.
     *
     * @return  the filtered list.
     */
    List<AddressList> filterOutByCountryCode(List<AddressList> addressLists, String countryCode);


    /**
     * Chceks if the given {@link Address} is of the given country.
     *
     * @param  address  an {@link Address}
     * @param  countryCode  the countryCode to test the Address against.
     *
     * @return  true if the address matches the given countryCode. Otherwise false.
     */
    boolean isAddressOfCountry(Address address, String countryCode);
}
