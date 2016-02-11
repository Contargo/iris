package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import java.util.List;


/**
 * Service selecting which {@link StaticAddress} best matches a given {@link Address}.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
interface StaticAddressSelector {

    /**
     * @param  staticAddresses  A list of static addresses of which one will be selected. Can not be null or empty.
     * @param  address  The address which is responsible for the static address selection.
     *
     * @return  The selected {@link StaticAddress}
     */
    StaticAddress select(List<StaticAddress> staticAddresses, Address address);
}
