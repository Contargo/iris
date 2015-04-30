package connection

import spock.lang.Specification
import util.ClientFactory

class ConnectionsForTerminalSpec extends Specification {

    def "request for Connections belonging to specific Terminal"() {
        given: "a REST client"
        def client = ClientFactory.newAdminClient()
        
        and: "a Terminal Unique ID"
        def urlParams = ["terminalUid": 1301000000000001]
    
        when: "connections are requested"
        def response = client.get(path: "/api/connections", query: urlParams)        
    
        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "response consists at least one connection"
        response.responseData.size() >= 1
        
        and: "a connection contains certain attributes"
        def connection = response.responseData[0]
        connection.keySet().size() == 3
        connection.keySet().containsAll("seaportUid", "terminalUid", "routeType")
        connection.terminalUid == "1301000000000001"
        
        and: "the attributes have certain types"
        connection.seaportUid.isNumber()
        connection.terminalUid.isNumber()
        !connection.routeType.isNumber()
        
    }
}
