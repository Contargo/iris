package address

import spock.lang.Specification
import util.ClientFactory

class AddressWherePlaceIsInSpec extends Specification {

    def "request for addresses containing place"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "addresses with place id 50350574 are requested"
        def response = client.get(path: "/api/places/50350574/addresses")
        def addresses = response.data.addresses

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "there should be fifteen addresses in the response"
        addresses.size() == 15

        and: "an address should have certain attributes"
        def address = addresses.get(0)
        def addressAttributes = address.keySet()
        addressAttributes.size() == 10
        addressAttributes.containsAll("niceName", "address", "placeId", "countryCode", "osmId", "longitude", "latitude",
                "type", "shortName", "displayName");

        and: "that address has the requested placeid"
        address.placeId == 50350574

    }
}