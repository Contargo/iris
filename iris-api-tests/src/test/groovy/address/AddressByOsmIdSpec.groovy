package address

import spock.lang.Specification
import util.ClientFactory

class AddressByOsmIdSpec extends Specification {

    def "request for addresses with osm id "() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "address with osm id 90085697 is requested"
        def response = client.get(path: "/api/osmaddresses/90085697")
        def addresses = response.data.geoCodeResponse.addresses.addresses
        def parentAddress = response.data.geoCodeResponse.addresses.parentAddress

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "there should be a parent address"
        parentAddress.size() == 1

        and: "that parent address should have certain attributes"
        def parentAddressAttributes = parentAddress.get(0).keySet()
        parentAddressAttributes.size() == 10
        parentAddressAttributes.containsAll("niceName", "address", "placeId", "countryCode", "osmId", "longitude",
                "latitude", "type", "shortName", "displayName")

        and: "there should be exactly one address in the response"
        def innerAddresses = addresses.get(0)
        innerAddresses.size() == 1

        and: "that address should have certain attributes"
        def address = innerAddresses.get(0)
        def addressAttributes = address.keySet()
        addressAttributes.size() == 10
        addressAttributes.containsAll("address", "displayName", "countryCode", "osmId", "latitude", "placeId", 
                "niceName", "shortName", "type", "longitude");

        and: "that address has the requested osmid"
        address.osmId == 90085697

        and: "that address's actual address has certain attributes"
        address["address"].keySet().size() == 10
        address["address"].keySet().containsAll("country", "country_code", "road", "city", "state_district", 
                "postcode", "suburb", "house_number", "address29", "state")
    }
}