package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.address.Address;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Supporting class for AddressService.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
class AddressServiceHelper {

    List<Address> mergeSearchResultsWithoutDuplications(List<Address> one, List<Address> two) {

        Set<Address> resultSet = new HashSet<>();
        resultSet.addAll(one);
        resultSet.addAll(two);

        return new ArrayList<>(resultSet);
    }
}
