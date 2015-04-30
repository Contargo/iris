package distance

import spock.lang.Specification
import util.ClientFactory

class AirlineDistanceSpec extends Specification {

    def "request for airline distance between karlsruhe and duisburg"() {

        def latKA = 49.008085
        def lonKA = 8.403756

        def latDU = 51.435146
        def lonDU = 6.762691

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "airline distance is requested"
        def params = [alat:latKA, alon:lonKA, blat:latDU, blon:lonDU]
        def response = client.get(path: "/api/airlineDistance", query: params)
        def airlineDistance = response.data.airlineDistance

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "airlineDistance should consist of unit and distance and links"
        airlineDistance.keySet().size() == 3
        airlineDistance.keySet().containsAll("unit", "distance", "links")

        and: "unit is meter"
        airlineDistance.unit == "meter"

    }
}
