package countries

import spock.lang.Specification
import util.ClientFactory

class CountriesSpec extends Specification {

    def "request for countries"() {

        given: "a REST client"
        def client = ClientFactory.newAdminClient()

        when: "countries are requested"
        def response = client.get(path: "/api/countries/")
        def countries = response.data.countriesResponse.countries

        then: "response status code should be 200 (OK)"
        response.status == 200

        and: "countries amount should be 12"
        countries.size() == 12

        and: "Belgium should be in the list"
        countries.contains([name: 'Belgium', value: 'BE']);   
        
        and: "Germany should be in the list"
        countries.contains([name: 'Germany', value: 'DE']);
    }

}
