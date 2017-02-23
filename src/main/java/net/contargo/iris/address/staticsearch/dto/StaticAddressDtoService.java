package net.contargo.iris.address.staticsearch.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressListDto;

import java.util.List;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public interface StaticAddressDtoService {

    /**
     * Finds all {@link AddressDto} of static addresses by the given parameters postalCode, city and country.
     *
     * @param  postalCode  String
     * @param  city  String
     * @param  country  String
     *
     * @return  List of {@link AddressDto}s.
     */
    List<AddressDto> getAddressesByDetails(String postalCode, String city, String country);


    /**
     * Returns one {@link net.contargo.iris.address.staticsearch.StaticAddress} wrapped in a list of lists for
     * compatibility with the client and consistency to the other interfaces, that deliver actual lists of address
     * lists. The static address is the one matching to the given Geolocation.
     *
     * @param  location  as search parameter
     *
     * @return  static address wrapped in a list of lists.
     */
    List<AddressListDto> getStaticAddressByGeolocation(GeoLocation location);


    /**
     * Retrieves a list of static addresses that are located in a bounding box with radius {@code km} around
     * {@code geoLocation}.
     *
     * @param  location  the geolocation at the bounding box's center
     * @param  distance  the bounding box's radius
     *
     * @return  a list of static addresses
     */
    List<StaticAddressDto> getStaticAddressByBoundingBox(GeoLocation location, Double distance);
}
