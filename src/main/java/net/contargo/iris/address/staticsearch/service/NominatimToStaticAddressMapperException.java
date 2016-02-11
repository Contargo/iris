package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class NominatimToStaticAddressMapperException extends RuntimeException {

    private final Address address;

    public NominatimToStaticAddressMapperException(Address address) {

        this.address = address;
    }

    public Address getAddress() {

        return address;
    }
}
