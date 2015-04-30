package net.contargo.iris.address.nominatim.service;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;


class AddressValidator {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final int MIN_STREET_LENGTH = 3;

    String validateStreet(String street) {

        if (street != null && street.length() < MIN_STREET_LENGTH) {
            LOG.info("street='" + street + "' seems to be inaccurate, so it will be ignored during geocoding process.");

            return "";
        }

        return street;
    }
}
