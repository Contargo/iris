package connection

import spock.lang.*
import util.ClientFactory
import org.apache.http.client.HttpResponseException

/**
 * @author Oliver Messner - messner@synyx.de
 */
class SeaportsInConnectionsSpec extends Specification {

    def "request for connected seaport"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()
        
        when: "request is sent to server"
        def response = client.get(path: "/api/connections/seaports")

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "the seaport object has certain attributes"
        def seaports = response.data.seaports
        !seaports.isEmpty()
        seaports.each {
            assert it.keySet().size() == 6
            assert it.keySet().containsAll("latitude", "longitude", "name", "enabled", "type", "uniqueId")
        }
        
        and: "links are provided"
        def links = response.data.links
        !links.isEmpty()
    }

    def "request for undefined combo type"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()
        
        when: "an invalid combotype is requested"
        client.get(path: "/api/connections/seaports", query: [combo: 'INVALID COMBO TYPE'])

        then: "response status type is bad request"
        def e = thrown(HttpResponseException)
        e.message == 'Bad Request'
        e.statusCode == 400
    }
}