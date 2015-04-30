package distance

import spock.lang.Specification
import util.ClientFactory

class CloudDistanceSpec extends Specification {

    def "request for cloud distance between karlsruhe and terminal one"() {

        def idKarlsruhe = 1301000000000001
        def idFrankfurt = 1301000000000001

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "distance is requested"
        def params = [
                terminal: idFrankfurt,
                address: idKarlsruhe
        ]
        def response = client.get(path: "/api/distancecloudaddress", query: params)

        then: "response status code should be 200 (OK)"
        response.status == 200

        def responseData = response.data

        and: "responseData should consist of address and links"
        responseData.keySet().size() == 2
        responseData.keySet().containsAll("address", "links")

        and: "address should consist of certain attributes"
        def address = responseData.address
        address.keySet().size() == 10
        address.keySet().containsAll("country", "hashKey", "distance", "city", "postalcode", "airLineDistanceMeter",
                "errorMessage", "suburb", "tollDistance", "uniqueId")
    }
}
