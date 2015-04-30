package connection

import spock.lang.Specification
import util.ClientFactory

class SeaportRouteSpec extends Specification {

    def "request for seaport routes"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "routes for a specified seaport are requested"
        def requestUrl = String.format('/api/connections/%s/%s:%s/%s', 1301000000000002, 49.01179, 8.4232, false)
        def params = [containerType: "TWENTY_LIGHT", isImport: false, combo: "WATERWAY"]
        def response = client.get(path: requestUrl, query: params)

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "there should be at least one route"
        def routes = response.data.response.routes
        !routes.isEmpty()

        routes.each {
            and: "each route has certain fields"
            def routeAttributes = it.keySet()
            assert routeAttributes.size() == 9
            assert routeAttributes.containsAll("roundTrip", "product", "shortName", "name", "data", "errors", "direction", "url", "responsibleTerminal")

            and: "each route's data field has certain fields"
            def data = it.data
            def dataAttributes = data.keySet()
            assert dataAttributes.size() == 8
            assert dataAttributes.containsAll("co2", "co2DirectTruck", "totalDistance", "totalOnewayTruckDistance",
                    "totalRealTollDistance", "totalTollDistance", "totalDuration", "parts")

            and: "the route parts have certain fields"
            def partAttributes = data.parts.first().keySet()
            assert partAttributes.size() == 8
            assert partAttributes.containsAll("name", "data", "origin", "containerState", "containerType", "direction",
                    "routeType", "destination")
        }
    }
}