package address

import spock.lang.Specification
import util.ClientFactory

/**
 * @author Arnold Franke - franke@synyx.de
 */
class AddressByAddressDetailsSpec extends Specification {

    def "request for addresses by details like street, city etc. "() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()
        
        and: "address details city, postal code and street"
        def urlParams = [
                "city": "karlsruhe",
                "postalcode": "76131",
                "street": "hirtenweg",
                "country": "DE"
        ]

        when: "addresses by city, postal code and street are requested"
        def response = client.get(path: "/api/geocodes", query: urlParams);
        def listOfAddressLists = response.data.geoCodeResponse.addresses
        def staticAddressesObject = listOfAddressLists[0]
        def nominatimAddressesObject = listOfAddressLists[1] 

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "there should be 4 objects in the response list"
        listOfAddressLists.size() == 4

        and: "the staticAddressesObject contains a parent address and a list of static addresses"
        def parentAddressStatic = staticAddressesObject.parentAddress
        def staticaddresses = staticAddressesObject.addresses
        staticaddresses.size() >= 1

        and: "the parent address object has certain attributes"
        parentAddressStatic.size() == 10
        parentAddressStatic.keySet().containsAll("countryCode", "niceName", "displayName", "osmId", "placeId", "shortName", "address", "type", "longitude", "latitude")
        parentAddressStatic.type == "ADDRESS"

        and: "a static address contains certain attributes"
        def staticaddress = staticaddresses[0]
        staticaddress.size() == 10
        staticaddress.keySet().containsAll("countryCode", "niceName", "displayName", "osmId", "placeId", "shortName", "address", "type", "longitude", "latitude")

        and: "the type of a static address is ADDRESS"
        staticaddress.type == "ADDRESS"

        and: "a static address contains a address map with certain attributes"
        def addressmapStatic = staticaddress.address
        addressmapStatic.size() == 6
        addressmapStatic.keySet().containsAll("suburb", "static_id", "postcode", "country_code", "city", "hashkey")

        and: "the nominatimAddressesObject contains a parent address and a list of static addresses"
        def parentAddressNominatim = nominatimAddressesObject.parentAddress
        def nominatimaddresses = nominatimAddressesObject.addresses
        nominatimaddresses.size() == 1

        and: "the parent address object has certain attributes"
        parentAddressNominatim.size() == 10
        parentAddressNominatim.keySet().containsAll("countryCode", "niceName", "displayName", "osmId", "placeId", "shortName", "address", "type", "longitude", "latitude")
        parentAddressNominatim.type == "ADDRESS"

        and: "a nominatim address contains certain attributes"
        def nominatimaddress = nominatimaddresses[0]
        nominatimaddress.size() == 10
        nominatimaddress.keySet().containsAll("countryCode", "niceName", "displayName", "osmId", "placeId", "shortName", "address", "type", "longitude", "latitude")

        and: "the type of a nominatim address is ADDRESS"
        nominatimaddress.type == "ADDRESS"

        and: "a nominatim address contains a address map with certain attributes"
        def addressmapNominatim = nominatimaddress.address
        addressmapNominatim.size() >= 8
        addressmapNominatim.keySet().containsAll("suburb", "postcode", "country_code", "city", "country", "road", "state_district", "state")
    
    }
}
