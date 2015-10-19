package routerevision

import groovyx.net.http.HttpResponseException
import spock.lang.Specification
import util.ClientFactory

class RouteRevisionSpec extends Specification {

    def "request for route revision for terminal one"() {

        def latKA = 49.008085
        def lonKA = 8.403756

        def uid = 1301000000000001

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "route revision is requested"
        def params = [latitude:latKA, longitude:lonKA, terminalUid:uid]
        def response = client.get(path: "/api/routerevisions", query: params)
        def revision = response.data

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "revision should consist of certain attributes"
        revision.keySet().size() == 11
        revision.keySet().containsAll("tollDistanceOneWayInKilometer", "airlineDistanceInKilometer", "terminalUid", "latitude",
                "radiusInMeter", "comment", "id", "truckDistanceOneWayInKilometer", "longitude", "validTo", "validFrom")

    }

    def "request for route revision for terminal one fails"() {

        def latKA = 49.008085
        def lonKA = 8.403756

        def uid = 47182372189378941

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "route revision is requested"
        def params = [latitude:latKA, longitude:lonKA, terminalUid:uid]
        client.get(path: "/api/routerevisions", query: params)

        then: "the request fails"
        def e = thrown(HttpResponseException)
        def response = e.getResponse();
        def errorResponse = response.data

        then: "response status code should be 200 (OK)"
        response.status == 404

        and: "error object consists of code and message"
        errorResponse.keySet().size() == 3
        errorResponse.keySet().containsAll("code", "message", "validationError")

    }
}
