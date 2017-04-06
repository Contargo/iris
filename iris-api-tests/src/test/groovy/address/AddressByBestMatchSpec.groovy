package address

import groovyx.net.http.HttpResponseException
import spock.lang.Specification
import util.ClientFactory

/**
 * @author Ben Antony - antony@synyx.de
 */
class AddressByBestMatchSpec extends Specification {

    def "request for addresses by details like city etc. matching a static address"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "address details city, postal code"
        def urlParams = [
                "city": "karlsruhe",
                "postalcode": "76131",
                "countrycode": "DE"
        ]

        when: "addresses by city and postal code are requested"
        def response = client.get(path: "/api/addresses/bestmatch", query: urlParams);
        def bestMatchAddressKarlsruhe = response.data

        then: "response status code should be 200 (OK)"
        response.status == 200
        
        and: "resulting address has all fields"
        bestMatchAddressKarlsruhe.size() == 6
        bestMatchAddressKarlsruhe.keySet().containsAll("hashKey", "geoLocation","city", "countryCode", "postalCode", "suburb")
        bestMatchAddressKarlsruhe.geoLocation.size() == 3
        bestMatchAddressKarlsruhe.geoLocation.keySet().containsAll("latitude", "longitude", "type")
        
        and: "resulting address is Karlsruhe"
        bestMatchAddressKarlsruhe.hashKey == "D3YTD"
        bestMatchAddressKarlsruhe.city == "Karlsruhe"
        bestMatchAddressKarlsruhe.geoLocation.latitude == 49.0126538640
        bestMatchAddressKarlsruhe.geoLocation.longitude == 8.3770751953
        bestMatchAddressKarlsruhe.geoLocation.type == "GEOLOCATION"
        bestMatchAddressKarlsruhe.countryCode == "DE"
        bestMatchAddressKarlsruhe.postalCode == "76131"
        bestMatchAddressKarlsruhe.suburb == ""
        
    }

    def "request for addresses by details like city etc. matching a not static address"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "address details city, postal code"
        def urlParams = [
                "city": "Trier",
                "postalcode": "54290",
                "countrycode": "DE"
        ]

        when: "addresses by city and postal code are requested"
        def response = client.get(path: "/api/addresses/bestmatch", query: urlParams);
        def bestMatchAddressKarlsruhe = response.data

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "resulting address has all fields"
        bestMatchAddressKarlsruhe.size() == 6
        bestMatchAddressKarlsruhe.keySet().containsAll("hashKey", "geoLocation","city", "countryCode", "postalCode", "suburb")
        bestMatchAddressKarlsruhe.geoLocation.size() == 3
        bestMatchAddressKarlsruhe.geoLocation.keySet().containsAll("latitude", "longitude", "type")

        and: "resulting address is Trier (from nominatim)"
        bestMatchAddressKarlsruhe.hashKey == null
        bestMatchAddressKarlsruhe.city == "Trier"
        bestMatchAddressKarlsruhe.geoLocation.latitude == 49.7596208000
        bestMatchAddressKarlsruhe.geoLocation.longitude == 6.6441878000
        bestMatchAddressKarlsruhe.geoLocation.type == "GEOLOCATION"
        bestMatchAddressKarlsruhe.countryCode == "de"
        bestMatchAddressKarlsruhe.postalCode == "54290"
        bestMatchAddressKarlsruhe.suburb == null

    }

    def "request for addresses which does not exist "() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        and: "address details city, postal code"
        def urlParams = [
                "city": "adfac",
                "postalcode": "1",
                "countrycode": "DE"
        ]

        when: "addresses by city and postal code are requested"
        def response
        try {
            client.get(path: "/api/addresses/bestmatch", query: urlParams);
        } catch (HttpResponseException e) {
            response = e.getResponse()
        }
        
        then: "result status is 404 NOT FOUND"
        response.status == 404
        
    }
}
