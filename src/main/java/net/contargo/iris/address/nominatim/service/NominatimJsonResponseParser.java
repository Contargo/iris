package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.address.Address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

import java.util.List;

import static java.util.Arrays.asList;


/**
 * The {@link NominatimJsonResponseParser} sends requests to Nominatim and converts the responses to {@link Address}
 * objects.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
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
    List<Address> getAddressesForUrl(String url) {

        List<Address> addresses;

        try {
            addresses = asList(nominatimRestClient.getForEntity(url, Address[].class).getBody());
            LOG.debug("{} search result(s) found for URL {}", addresses.size(), url);
        } catch (RestClientException e) {
            addresses = null;
        }

        return addresses;
    }


    Address getAddressForUrl(String reverseGeoCodingLookupURL) {

        Address address;

        try {
            address = nominatimRestClient.getForEntity(reverseGeoCodingLookupURL, Address.class).getBody();
            LOG.debug("Got result for reverse Geo coding lookup: {}", address);
        } catch (RestClientException e) {
            address = null;
        }

        return address;
    }
}
