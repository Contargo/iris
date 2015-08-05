package connection

import spock.lang.Specification
import util.ClientFactory

class EnrichedRouteSpec extends Specification {

    def "request for enriched route"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "a route specification"
        def routeParams = [
                "data.parts[0].origin.longitude"     : 8.544177,
                "data.parts[0].origin.latitude"      : 50.08162,
                "data.parts[0].destination.longitude": 8.4232,
                "data.parts[0].destination.latitude" : 49.01179,
                "data.parts[0].routeType"            : "TRUCK",
                "data.parts[0].containerType"        : "FORTY",
                "data.parts[0].containerState"       : "EMPTY",
                "data.parts[1].origin.longitude"     : 8.4232,
                "data.parts[1].origin.latitude"      : 49.01179,
                "data.parts[1].destination.longitude": 8.544177,
                "data.parts[1].destination.latitude" : 50.08162,
                "data.parts[1].routeType"            : "TRUCK",
                "data.parts[1].containerType"        : "FORTY",
                "data.parts[1].containerState"       : "FULL",
                "data.parts[2].origin.longitude"     : 8.544177,
                "data.parts[2].origin.latitude"      : 50.08162,
                "data.parts[2].destination.longitude": 4.3,
                "data.parts[2].destination.latitude" : 51.36833,
                "data.parts[2].routeType"            : "BARGE",
                "data.parts[2].containerType"        : "FORTY",
                "data.parts[2].containerState"       : "FULL",
        ]

        when: "details are requested"
        def response = client.get(path: "/api/routedetails", query: routeParams)

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "the route has certain attributes"
        def route = response.data.response.route
        route.keySet().size() == 9
        route.keySet().containsAll("product", "errors", "responsibleTerminal", "direction", "name", "roundTrip", "data", "shortName", "url")

        and: "the route's data has certain attribures"
        route.data.keySet().size() == 8
        route.data.keySet().containsAll("co2", "parts", "totalRealTollDistance", "totalDuration", "totalDistance",
                "totalTollDistance", "co2DirectTruck", "totalOnewayTruckDistance")

        and: "each route part has certain attributes"
        route.data.parts.each {
            assert it.keySet().size() == 9
            assert it.keySet().containsAll("containerType", "routeType", "direction", "name", "containerState", "origin",
                    "data", "destination", "subRouteParts")
            assert it.data.keySet().size() == 9
            assert it.data.keySet().containsAll("electricDistance", "duration", "distance", "railDieselDistance",
                    "bargeDieselDistance", "co2", "dieselDistance", "tollDistance", "airlineDistance")
        }
    }
}