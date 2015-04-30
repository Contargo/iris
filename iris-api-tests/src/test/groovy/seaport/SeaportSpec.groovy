package seaport

import groovyx.net.http.ContentType
import spock.lang.Specification
import util.ClientFactory

class SeaportSpec extends Specification {

    def 'create new seaport'() {

        given: "a REST client"
        def client = new ClientFactory().newAdminClient()

        when: 'new seaport is created'
        def seaportUid = System.nanoTime()
        def content = [
                name: "seaport-${seaportUid}".toString(),
                longitude: "8.${seaportUid}".toString(),
                latitude: "49.${seaportUid}".toString()
        ]

        def response = client.put(path: "/api/seaports/$seaportUid", body: content, contentType: ContentType.JSON)

        then: 'response status code should be 201 (CREATED)'
        response.status == 201
    }

    def 'create and update seaport'() {

        given: "a REST client"
        def client = new ClientFactory().newAdminClient()

        and: 'a new seaport'
        def seaportUid = System.nanoTime()
        def content = [
                name: "seaport-${seaportUid}".toString(),
                longitude: "8.${seaportUid}".toString(),
                latitude: "49.${seaportUid}".toString()
        ]

        client.put(path: "/api/seaports/$seaportUid", body: content, contentType: ContentType.JSON)

        when: 'seaport is updated'
        def response = client.put(path: "/api/seaports/$seaportUid", body: content, contentType: ContentType.JSON)

        then: 'response status code should be 204 (NO CONTENT)'
        response.status == 204
    }
}
