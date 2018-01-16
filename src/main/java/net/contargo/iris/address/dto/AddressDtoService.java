package net.contargo.iris.address.dto;

import net.contargo.iris.GeoLocation;

import java.util.List;
import java.util.Map;


/**
 * Delegates to AddressService and converts Address Beans into Address DTOs.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
public interface AddressDtoService {

    /**
     * Returns a {@link AddressDto} with information behind the given osmId.
     *
     * @param  osmId  search parameter
     *
     * @return  The address for a certain osmId
     */
    AddressDto getAddressByOsmId(long osmId);


    /**
     * Wraps the given AddressDto as first element of an {@link AddressListDto} This is needed to provide a consistent
     * format of returned Addresses for API-Clients.
     *
     * @param  addressDto
     *
     * @return
     */
    List<AddressListDto> wrapInListOfAddressLists(AddressDto addressDto);


    /**
     * Returns the corresponding {@link AddressDto} object from a given {@link GeoLocation}.
     *
     * @param  location  keeps the basis information for the {@link AddressDto} search.
     *
     * @return  The address for the given {@link GeoLocation}.
     */
    AddressDto getAddressForGeoLocation(GeoLocation location);


    /**
     * Resolves an address (described by the given parameters) to a {@link java.util.List} of
     * {@link net.contargo.iris.address.Address} objects with the attributes name, latitude and longitude. Uses
     * multiple fallback strategies to find addresses if not all parameters are provided
     *
     * @param  addressDetails  The parameters describing the addresses we are looking for
     *
     * @return  A List of Address Lists
     */
    List<AddressListDto> getAddressesByDetails(Map<String, String> addressDetails);


    /**
     * Resolves an address (described by the given parameters) to a {@link java.util.List} of
     * {@link net.contargo.iris.address.Address} objects with the attributes name, latitude and longitude. Uses
     * multiple fallback strategies to find addresses if not all parameters are provided
     *
     * @param  addressDetails  The parameters describing the addresses we are looking for
     *
     * @return  A List of Addresses
     */
    List<AddressDto> getAddressesByDetailsPlain(Map<String, String> addressDetails);


    /**
     * Returns all Addresses where the given place is in.
     *
     * @param  placeId  the OSM Place ID
     *
     * @return  All addresses belonging to the OSM-Place defined by the OSM Place ID
     */
    List<AddressDto> getAddressesWherePlaceIsIn(Long placeId);


    /**
     * Returns one {@link AddressDto} with the given hashKey.
     *
     * @param  hashKey  as search parameter
     *
     * @return  the {@link AddressDto} with the given hashKey
     */
    AddressDto getAddressesByHashKey(String hashKey);


    /**
     * Returns a list of addresses matching the query.
     *
     * @param  query  the address query
     *
     * @return  a list of matching addresses
     */
    List<AddressDto> getAddressesByQuery(String query);
}
