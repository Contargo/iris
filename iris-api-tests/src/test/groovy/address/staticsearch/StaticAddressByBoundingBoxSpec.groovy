package address.staticsearch

import spock.lang.Specification
import util.ClientFactory

/**
 * @author Sandra Thieme - thieme@synyx.de
 * @author David Schilling - schilling@synyx.de
 * @author Oliver Messner - messner@synyx.de
 */
class StaticAddressByBoundingBoxSpec extends Specification {

    def "request for static addresses within a bounding box"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "a bounding box definition"
        def urlParams = ["lat": "49.0126538640", "lon": "8.3770751953", "distance": "10"]

        when: "request static addresses within bounding box"
        def response = client.get(path: "/api/staticaddresses", query: urlParams)

        then: "response status code should be 200 (OK)"
        with(response) {
            status == 200

            data.first().keySet() == ['uniqueId', 'geoLocation', 'country', 'postalcode',
                                      'city', 'suburb', 'hashKey'] as Set

            data.first().geoLocation.keySet() == ['latitude', 'longitude', 'type'] as Set
        }
    }
}