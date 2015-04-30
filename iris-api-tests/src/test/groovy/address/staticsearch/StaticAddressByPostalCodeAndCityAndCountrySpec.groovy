package address.staticsearch

import spock.lang.Specification
import util.ClientFactory

/**
 * @author Arnold Franke - franke@synyx.de
 */
class StaticAddressByPostalCodeAndCityAndCountrySpec extends Specification {

    def "request for Static Addresses matching the given details"() {
        
        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "address details (postalCode, city, country)"
        def urlParams = [
                "postalCode": "76131",
                "city": "Karlsruhe",
                "country": "DE"
        ]

        when: "static addresses are requested"
        def response = client.get(path: "/api/staticaddresses", query: urlParams)

        then: "response status code should be 200 (OK)"
        response.status == 200
        
        and: "response consists of one address"
        def staticAddresses = response.responseData
        staticAddresses.size() == 1
        
        and: "a static address contains certain attributes"
        def staticaddress = staticAddresses[0]
        staticaddress.keySet().size() == 10
        staticaddress.keySet().containsAll("countryCode", "niceName", "displayName", "osmId", "placeId", "shortName", "address", "type", "longitude", "latitude")

        and: "a static address contains a address map with certain attributes"
        def addressmap = staticaddress.address
        addressmap.size() == 6
        addressmap.keySet().containsAll("suburb", "static_id", "postcode", "country_code", "city", "hashkey")
                                                       
        and: "the type of a static address is ADDRESS"
        staticaddress.type == "ADDRESS"
        
    }
}
