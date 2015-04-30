package net.contargo.iris.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import java.lang.invoke.MethodHandles;


/**
 * Util class for dealing with Http stuff.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class HttpUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int STATUS_CODE_300 = 300;
    private static final int STATUS_CODE_199 = 199;
    private final HttpClient httpClient;

    public HttpUtil(HttpClient httpClient) {

        this.httpClient = httpClient;
    }

    /**
     * Gets the response as String for the given String representing an URL.
     *
     * @param  url  String
     *
     * @return  response as String
     */
    public String getResponseContent(String url) {

        HttpEntity entity = getHttpEntityForGETRequest(url);
        String response = null;

        if (entity != null) {
            try(InputStream is = entity.getContent()) {
                response = InputStreamUtil.convertInputStreamToString(is);
            } catch (IOException ex) {
                throw new HttpUtilException("Could not get content (InputStream) of HttpEntity", ex);
            }
        }

        return response;
    }


    /**
     * Sends GET request to the given url and returns the response's HttpEntity.
     *
     * @param  url  String
     *
     * @return  HttpEntity
     */
    private HttpEntity getHttpEntityForGETRequest(String url) {

        HttpEntity entity;

        try {
            LOG.debug("Sending GET request to {} ", url);

            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode > STATUS_CODE_199 && statusCode < STATUS_CODE_300) {
                LOG.debug("Received response with status code {} for URL {}", statusCode, url);
            } else {
                LOG.error("Problem requesting {} - Code {} - {}", url, statusCode, response.getStatusLine());
            }

            entity = response.getEntity();

            if (entity == null) {
                LOG.warn("No response content found for url {} ", url);
            }
        } catch (IOException ex) {
            throw new HttpUtilException("Error getting response for url " + url, ex);
        }

        return entity;
    }
}
