package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.staticsearch.StaticAddress;

import java.math.BigDecimal;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public interface ClosestStaticAddressService {

    /**
     * Returns the closest static address to the given details. A first lookup will try to match a static address with
     * postCode, city and country. In case there is no result, a second lookup will look for the closest static address
     * by air distance of latitude/longitude
     *
     * @return  a static address
     */
    StaticAddress get(String postalCode, String city, String country, BigDecimal latitude, BigDecimal longitude);
}
