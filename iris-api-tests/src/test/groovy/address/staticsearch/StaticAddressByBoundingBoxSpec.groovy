package address.staticsearch

import spock.lang.Specification
import util.ClientFactory

/**
 * @author Sandra Thieme - thieme@synyx.de
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

        and: "response consists of a list of static address uids"
        def uidList = response.data.uids
        uidList.size() == 1

    }
}