package address.staticsearch

import spock.lang.Specification
import util.ClientFactory

/**
 * @author Sandra Thieme - thieme@synyx.de
 * @author David Schilling - schilling@synyx.de
 */
class StaticAddressByBoundingBoxSpec extends Specification{

    def "request for Static Addresses matching the given bounding box"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "a bounding box definition"
        def urlParams = [
                "lat": "49.0126538640",
                "lon": "8.3770751953",
                "distance": "10"
        ]

        when: "static addresses are requested"
        def response = client.get(path: "/api/staticaddresses", query: urlParams)

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "response consists of a list of static addresses"
        def addresses = response.data
        addresses.size() == 1
        addresses[0].size() == 7
        addresses[0].keySet().containsAll("uniqueId", "geoLocation", "country", "postalcode", "city", "suburb", "hashKey")
        addresses[0].geoLocation.keySet().containsAll("latitude", "longitude", "type")
        addresses[0].geoLocation.size() == 3
    }
}