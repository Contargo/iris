package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.staticsearch.StaticAddress;

import java.math.BigInteger;

import java.util.List;


/**
 * Provides service methods for {@link StaticAddress} entities.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
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
     * Finds all {@link StaticAddress}s by the given parameters postalCode, city and country. The procedure of this
     * method is following (step to next procedure if result list is empty): 1. Execute an AND search, i.e. find
     * addresses by postal code AND city 2. Execute an OR search, i.e. find addresses by postal code OR city 3. Execute
     * a split search, i.e. if the city string is "Neustadt an der Weinstrasse" and nothing is found for that then
     * execute search for "Neustadt"
     *
     * @param  postalCode  as search parameter for {@link StaticAddress}
     * @param  city  as search parameter for {@link StaticAddress}
     * @param  country  as search parameter for {@link StaticAddress}
     *
     * @return  List of {@link StaticAddress}es.
     */
    List<StaticAddress> getAddressesByDetailsWithFallbacks(String postalCode, String city, String country);


    /**
     * Finds all {@link StaticAddress}s with the given parameter of postalCode, city and country.
     *
     * @param  postalCode  as search parameter for {@link StaticAddress}
     * @param  city  as search parameter for {@link StaticAddress}
     * @param  country  as search parameter for {@link StaticAddress}
     *
     * @return  List of {@link StaticAddress}s filtered by the given search parameters.
     */
    List<StaticAddress> getAddressesByDetails(String postalCode, String city, String country);


    /**
     * Finds an {@link StaticAddress} at the exact given location or null.
     *
     * @param  loc  {@link GeoLocation} as search parameter for {@link StaticAddress}
     *
     * @return  {@link StaticAddress} of the given {@link GeoLocation}
     */
    StaticAddress getForLocation(GeoLocation loc);


    /**
     * Finds an {@link StaticAddress} by the given id.
     *
     * @param  staticAddressId  as search parameter for {@link StaticAddress}
     *
     * @return  {@link StaticAddress} of the given id
     */
    StaticAddress findById(Long staticAddressId);


    /**
     * Finds exactly one or no {@link StaticAddress} by the given hashKey.
     *
     * @param  hashKey  as search parameter for {@link StaticAddress}
     *
     * @return  {@link StaticAddress} with the provided hashKey
     */
    StaticAddress findByHashKey(String hashKey);


    /**
     * Finds an {@link StaticAddress} by the given unique id.
     *
     * @param  staticAddressUId  as search parameter for {@link StaticAddress}
     *
     * @return  {@link StaticAddress} of the given unique id
     */
    StaticAddress findByUId(BigInteger staticAddressUId);


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


    /**
     * Normalizes {@link StaticAddress} city and suburb attributes.
     *
     * @param  staticAddress  to normalize it attributes
     */
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


    List<StaticAddress> findByPostalcode(String postalcode);


    List<StaticAddress> findByPostalcodeAndCountry(String postalcode, String country);
}
