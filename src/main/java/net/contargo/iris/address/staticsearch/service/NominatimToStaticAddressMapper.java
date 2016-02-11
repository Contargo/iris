package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;


/**
 * Service that is able to map an address to a static address.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface NominatimToStaticAddressMapper {

    /**
     * @param  address  The {@link Address} to map
     *
     * @return  the mapped {@link StaticAddress}
     *
     * @throws  NominatimToStaticAddressMapperException  when mapping was not successful.
     */
    StaticAddress map(Address address);
}
