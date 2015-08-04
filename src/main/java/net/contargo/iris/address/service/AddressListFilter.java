package net.contargo.iris.address.service;

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
}
