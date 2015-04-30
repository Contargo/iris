package connection

import spock.lang.Specification
import util.ClientFactory

class ConnectionSpec extends Specification {

    def "request for connections"() {

        given: "a REST client"
        def client = new ClientFactory().newAdminClient()

        when: "connections for a specified terminal are requested"
        def response = client.get(path: '/api/connections', query: [terminalUid: '1301000000000001'])

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "response body is as expected"
        response.data[0].keySet() == ['routeType', 'terminalUid', 'seaportUid'] as Set
        response.data[0].terminalUid == '1301000000000001'
        response.data[0].seaportUid == '1301000000000002'
        response.data[0].routeType == 'BARGE'
    }
}