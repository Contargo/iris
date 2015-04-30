package address.staticsearch

import spock.lang.Specification
import util.ClientFactory

/**
 * @author Arnold Franke - franke@synyx.de
 */
class StaticAddressByGeolocationSpec extends Specification{

    def "request for Static Addresses matching the given coordinates"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "a geolocation (latitude, longitude)"
        def urlParams = [
                "lat": "49.0126538640",
                "lon": "8.3770751953"
        ]

        when: "static addresses are requested"
        def response = client.get(path: "/api/staticaddresses", query: urlParams)
        print(response.data)
        
        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "response consists of a list of one addresslist object"
        def addressListList = response.data.geoCodeResponse.addresses
        addressListList.size() == 1

        and: "the addresslistobject contains a parent address and a list of static addresses"
        def addressListObject = addressListList[0]
        def parentAddress = addressListObject.parentAddress
        def staticaddresses = addressListObject.addresses
        staticaddresses.size() == 1

        and: "the parent address object has certain attributes"
        parentAddress.size() == 10
        parentAddress.keySet().containsAll("countryCode", "niceName", "displayName", "osmId", "placeId", "shortName", "address", "type", "longitude", "latitude")
        parentAddress.type == "ADDRESS"

        and: "a static address contains certain attributes"
        def staticaddress = staticaddresses[0]
        staticaddress.size() == 10
        staticaddress.keySet().containsAll("countryCode", "niceName", "displayName", "osmId", "placeId", "shortName", "address", "type", "longitude", "latitude")

        and: "the type of a static address is ADDRESS"
        staticaddress.type == "ADDRESS"

        and: "a static address contains a address map with certain attributes"
        def addressmap = staticaddress.address
        addressmap.size() == 6
        addressmap.keySet().containsAll("suburb", "static_id", "postcode", "country_code", "city", "hashkey")
    }
}