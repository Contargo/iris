package net.contargo.iris.address.w3w;

import net.contargo.iris.GeoLocation;

import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class ThreeWordClient {

    private static final String FORWARD_URL =
        "https://api.what3words.com/v2/forward?addr={w3wAddress}&key={apiKey}&lang=de";

    private final RestTemplate restTemplate;
    private final String apiKey;

    public ThreeWordClient(RestTemplate restTemplate, String apiKey) {

        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public GeoLocation resolve(String threeWords) {

        ResponseEntity<ForwardW3wResponse> response = restTemplate.getForEntity(FORWARD_URL, ForwardW3wResponse.class,
                threeWords, apiKey);

        checkErrorStatus(response.getBody(), threeWords);

        return response.getBody().toGeolocation();
    }


    private static void checkErrorStatus(ForwardW3wResponse response, String threeWords) {

        if (response.error()) {
            Integer code = response.errorCode();
            String message = response.errorMessage();

            throw new ThreeWordClientException(code, message, threeWords);
        }
    }
}
