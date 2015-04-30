package address

import spock.lang.Specification
import util.ClientFactory

class AddressByGeolocationSpec extends Specification {

    def "request for geolocation"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "geolocation with latitude 49.123 and longituide 8.12 is requested"
        def response = client.get(path: "/api/reversegeocode/49.123:8.12/")
        def address = response.data.reverseGeocodeResponse.address

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "the address should have certain attributes"
        def addressAttributes = address.keySet()
        addressAttributes.size() == 10
        addressAttributes.containsAll("niceName", "address", "placeId", "countryCode", "osmId", "longitude", "latitude",
                "type", "shortName", "displayName");

        and: "the address is of type ADDRESS"
        address.type == "ADDRESS"

        and: "the address has the requested longitude and latitude"
        address.latitude == 49.123
        address.longitude == 8.12

        and: "the address's actual address has certain attributes"
        address.address.keySet().size() == 7
        println address.address.keySet()
        address.address.keySet().containsAll("country", "country_code", "farmyard", "county", "postcode", 
                "state", "village")
    }
}
