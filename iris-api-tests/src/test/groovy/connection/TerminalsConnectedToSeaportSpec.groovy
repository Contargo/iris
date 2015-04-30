package connection

import spock.lang.Specification
import util.ClientFactory

class TerminalsConnectedToSeaportSpec extends Specification {

    def "request for terminals connected to a seaport with route type BARGE"() {
        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "a seaport unique ID and a route type"
        def urlParams = [
                "seaportUid": 1301000000000002,
                "routeType": "BARGE"
        ]

        when: "terminals for this combination are requested"
        def response = client.get(path: "/api/terminals", query: urlParams)

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "the response contains terminals"
        def terminals = response.data.response.terminals
        !terminals.isEmpty()

        and: "the terminals contains certain attributes"
        terminals.each {
            assert it.keySet().size() == 7
            assert it.keySet().containsAll("latitude", "longitude", "name", "enabled", "uniqueId", "type", "region")
        }
    }
}
