package terminal

import groovyx.net.http.ContentType
import spock.lang.Specification
import util.ClientFactory

class TerminalSpec extends Specification {

    def 'create new terminal'() {

        given: "a REST client"
        def client = new ClientFactory().newAdminClient()

        when: 'new terminal is created'
        def terminalUid = System.nanoTime()
        def content = [
                name: "terminal-${terminalUid}".toString(),
                longitude: "8.${terminalUid}".toString(),
                latitude: "49.${terminalUid}".toString(),
                region: "NOT_SET"
        ]

        def response = client.put(path: "/api/terminals/$terminalUid", body: content, contentType: ContentType.JSON)

        then: 'response status code should be 201 (CREATED)'
        response.status == 201
    }

    def 'create and update terminal'() {

        given: "a REST client"
        def client = new ClientFactory().newAdminClient()

        and: 'a new terminal'
        def terminalUid = System.nanoTime()
        def content = [
                name: "terminal-${terminalUid}".toString(),
                longitude: "8.${terminalUid}".toString(),
                latitude: "49.${terminalUid}".toString(),
                region: "NOT_SET"
        ]

        client.put(path: "/api/terminals/$terminalUid", body: content, contentType: ContentType.JSON)

        when: 'terminal is updated'
        def response = client.put(path: "/api/terminals/$terminalUid", body: content, contentType: ContentType.JSON)

        then: 'response status code should be 204 (NO CONTENT)'
        assert response.status == 204
    }
}
