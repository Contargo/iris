package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.staticsearch.StaticAddress;

import java.util.List;


/**
 * Provides service methods for {@link StaticAddress} entities.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public interface StaticAddressService {

    /**
     * Finds all {@link net.contargo.iris.address.Address}es by the given parameters postalCode, city and country.
     *
     * @param  postalCode  String
     * @param  city  String
     * @param  country  String
     *
     * @return  {@link AddressList}
     */
    AddressList findAddresses(String postalCode, String city, String country);


    /**
     * Finds all {@link StaticAddress} by the given parameters postalCode, city and country. The procedure of this
     * method is following (step to next procedure if result list is empty): 1. Execute an AND search, i.e. find
     * addresses by postal code AND city 2. Execute an OR search, i.e. find addresses by postal code OR city 3. Execute
     * a split search, i.e. if the city string is "Neustadt an der Weinstrasse" and nothing is found for that then
     * execute search for "Neustadt"
     *
     * @param  postalCode  String
     * @param  city  String
     * @param  country  String
     *
     * @return  List of {@link StaticAddress}es.
     */
    List<StaticAddress> getAddressesByDetailsWithFallbacks(String postalCode, String city, String country);


    List<StaticAddress> getAddressesByDetails(String postalCode, String city, String country);


    /**
     * Finds an Address at the exact given location or null.
     *
     * @param  loc
     *
     * @return
     */
    StaticAddress getForLocation(GeoLocation loc);


    /**
     * @param  staticAddressId
     *
     * @return
     */
    StaticAddress findbyId(Long staticAddressId);


    /**
     * saves the staticAddress in the Database.
     *
     * @param  staticAddress  the staticAddress to save in the database
     *
     * @return  staticAddress which was saved in the database
     *
     * @throws  StaticAddressDuplicationException  if duplicate was found.
     */
    StaticAddress saveStaticAddress(StaticAddress staticAddress);


    /**
     * @param  staticAddressId
     *
     * @return  The _one_ Address for the given static address ID. For consistent processing on client side it is
     *          wrapped in a list of AddressLists.
     */
    List<AddressList> getAddressListListForStaticAddressId(Long staticAddressId);


    /**
     * @param  location
     *
     * @return  The _one_ Address for the given geoLocation. For consistent processing on client side it is wrapped in a
     *          list of AddressLists.
     */
    List<AddressList> getAddressListListForGeolocation(GeoLocation location);


    void normalizeFields(StaticAddress staticAddress);


    /**
     * looks for database entries with missing hashkeys. generates and saves the hashkeys.
     */
    void fillMissingHashKeys();


    /**
     * Retrieves a list of static addresses that are located in a bounding box with radius {@code km} around
     * {@code geoLocation}.
     *
     * @param  geoLocation  the geolocation at the bounding box's center
     * @param  km  the bounding box's radius
     *
     * @return  a list of static addresses
     */
    List<StaticAddress> getAddressesInBoundingBox(GeoLocation geoLocation, Double km);
}
