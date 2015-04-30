package authorization

import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseException
import spock.lang.Specification
import util.ClientFactory

class AuthorizationSpec extends Specification {

    def "request for api from non-authorized user"() {

        given: "a REST client without login credentials"
        def client = ClientFactory.newUnauthorizedClient()

        when: "api is requested"
        client.get(path: "/api/")

        then: "the request fails"
        def e = thrown(HttpResponseException)
        def response = e.getResponse();

        then: "response status code should be 401 (Unauthorized)"
        response.status == 401

        and: "response should have correct authentication header"
        response.getFirstHeader("WWW-Authenticate").getValue() == "Basic realm=\"IRIS API\""
    }

    def "request for seaport synchronisation for non allowed user"() {

        given: "a REST client with user login credentials"
        def client = ClientFactory.newUserClient()

        when: "seaport creation is requested"
        def seaportUid = System.nanoTime()
        def content = [
                name: "seaport-${seaportUid}".toString(),
                longitude: "8.${seaportUid}".toString(),
                latitude: "49.${seaportUid}".toString()
        ]
        client.put(path: "/api/seaports/$seaportUid", body: content, contentType: ContentType.JSON)

        then: "the request fails"
        def e = thrown(HttpResponseException)

        then: "response status code should be 403 (Forbidden)"
        e.getResponse().status == 403
    }

    def "request for terminal synchronisation for non allowed user"() {

        given: "a REST client with user login credentials"
        def client = ClientFactory.newUserClient()

        when: "terminal creation is requested"
        def terminalUid = System.nanoTime()
        def content = [
                name: "terminal-${terminalUid}".toString(),
                longitude: "8.${terminalUid}".toString(),
                latitude: "49.${terminalUid}".toString(),
                region: "NOT_SET"
        ]
        client.put(path: "/api/terminals/$terminalUid", body: content, contentType: ContentType.JSON)

        then: "the request fails"
        def e = thrown(HttpResponseException)

        then: "response status code should be 403 (Forbidden)"
        e.getResponse().status == 403
    }
}
