
package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;

import java.util.List;
import java.util.Map;


/**
 * This service is used for address resolution purposes.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public interface AddressService {

    /**
     * Resolves an address (described by the given parameters) to a {@link java.util.List} of
     * {@link net.contargo.iris.address.Address} objects with the attributes name, latitude and longitude. Uses
     * multiple fallback strategies to find addresses if not all parameters are provided
     *
     * @param  addressDetails  @return
     */
    List<Address> getAddressesByDetails(Map<String, String> addressDetails);


    /**
     * Returns all Addresses where the given place is in.
     *
     * @param  placeId  the OSM Place ID
     *
     * @return  All addresses belonging to the OSM-Place defined by the OSM Place ID
     */
    List<Address> getAddressesWherePlaceIsIn(Long placeId);


    /**
     * @param  osmId
     *
     * @return  The address for a certain osmId
     */
    Address getAddressByOsmId(long osmId);


    /**
     * @param  location
     *
     * @return  The address for the given geolocation.
     */
    Address getAddressByGeolocation(GeoLocation location);


    /**
     * Returns a list of addresses matching the query.
     *
     * @param  query  the address query
     *
     * @return  a list of matching addresses
     */
    List<Address> getAddressesByQuery(String query);
}
