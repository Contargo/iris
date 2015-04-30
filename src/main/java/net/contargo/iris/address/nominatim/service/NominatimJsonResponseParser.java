package net.contargo.iris.address.nominatim.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.contargo.iris.address.Address;
import net.contargo.iris.util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.lang.invoke.MethodHandles;

import java.util.List;


/**
 * The {@link NominatimJsonResponseParser} sends requests to Nominatim and converts the responses to {@link Address}
 * objects.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
class NominatimJsonResponseParser {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HttpUtil httpUtil;
    private final ObjectMapper objectMapper;

    public NominatimJsonResponseParser(HttpUtil httpUtil, ObjectMapper objectMapper) {

        this.httpUtil = httpUtil;
        this.objectMapper = objectMapper;
    }

    /**
     * Returns {@link java.util.List} of {@link Address}es for the given URL. Returns an empty {@link java.util.List} if
     * there are no search results.
     *
     * @param  url  String
     */
    List<Address> getAddressesForUrl(String url) {

        return convertContentToAddresses(url, httpUtil.getResponseContent(url));
    }


    /**
     * Extracts the addresses from the URL.
     *
     * @param  url  to extract the address
     *
     * @return  the extracted addresses
     */
    List<Address> getAddressesForUrlForOsmId(String url) {

        return convertContentToAddresses(url, "[" + httpUtil.getResponseContent(url) + "]");
    }


    Address getAddressForUrl(String reverseGeoCodingLookupURL) {

        String content = httpUtil.getResponseContent(reverseGeoCodingLookupURL);
        LOG.debug("Got result for reverse Geo coding lookup: {}", content);

        Address address;

        try {
            address = objectMapper.readValue(content, Address.class);
        } catch (IOException | NullPointerException e) {
            address = null;
        }

        return address;
    }


    private List<Address> convertContentToAddresses(String url, String content) {

        List<Address> addresses = null;

        if (content != null) {
            try {
                addresses = objectMapper.readValue(content, new TypeReference<List<Address>>() {
                        });
                LOG.debug("{} search result(s) found for URL {}", addresses.size(), url);
            } catch (IOException e) {
                addresses = null;
            }
        }

        return addresses;
    }
}
