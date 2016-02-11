package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import java.util.List;

import static java.util.Collections.emptyList;


/**
 * Abstract base class that allows to chain multiple implementations of {@code StaticAddressMappingProcessor} following
 * the template method design pattern.
 *
 * <p>Usage: Overriding the map-method is sufficient.</p>
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
abstract class StaticAddressMappingProcessor {

    private final StaticAddressMappingProcessor next;

    StaticAddressMappingProcessor(StaticAddressMappingProcessor next) {

        this.next = next;
    }

    abstract List<StaticAddress> map(Address address);


    List<StaticAddress> process(Address address) {

        List<StaticAddress> addresses = map(address);

        if (!addresses.isEmpty()) {
            return addresses;
        }

        return next != null ? next.process(address) : emptyList();
    }
}
