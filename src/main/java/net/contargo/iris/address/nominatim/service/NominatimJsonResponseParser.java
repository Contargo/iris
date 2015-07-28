package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.address.Address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.TEXT_HTML;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * The {@link NominatimJsonResponseParser} sends requests to Nominatim and converts the responses to {@link Address}
 * objects.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
class NominatimJsonResponseParser {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestTemplate nominatimRestClient;

    public NominatimJsonResponseParser(RestTemplate nominatimRestClient) {

        this.nominatimRestClient = nominatimRestClient;
    }

    /**
     * Returns {@link java.util.List} of {@link Address}es for the given URL. Returns an empty {@link java.util.List} if
     * there are no search results.
     *
     * @param  url  String
     */
    List<Address> getAddresses(String url) {

        List<Address> addresses;

        try {
            addresses = asList(nominatimRestClient.exchange(url, GET, getHttpEntity(), Address[].class).getBody());
            LOG.debug("{} search result(s) found for URL {}", addresses.size(), url);
        } catch (RestClientException e) {
            addresses = null;
        }

        return addresses;
    }


    /**
     * Returns {@link java.util.List} of {@link Address}es for the given URL. Returns an empty {@link java.util.List} if
     * there are no search results.
     *
     * @param  url  String
     */
    List<Address> getAddressesFromOSMId(String url) {

        List<Address> addresses;

        try {
            addresses = singletonList(nominatimRestClient.exchange(url, GET, getHttpEntity(), Address.class).getBody());
            LOG.debug("{} search result(s) found for URL {}", addresses.size(), url);
        } catch (RestClientException e) {
            addresses = null;
        }

        return addresses;
    }


    Address getAddress(String url) {

        Address address;

        try {
            address = nominatimRestClient.exchange(url, GET, getHttpEntity(), Address.class).getBody();
            LOG.debug("Got result for reverse Geo coding lookup: {}", address);
        } catch (RestClientException e) {
            address = null;
        }

        return address;
    }


    /**
     * The Nominatim Server does only accept ACCEPT=TEXT_HTML Headers but will response JSON because of url parameters
     * format=json.
     *
     * @return  HttpEntity with TEXT_HTML Accept in Header
     */
    private HttpEntity<Address> getHttpEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(TEXT_HTML));

        return new HttpEntity<>(headers);
    }
}
