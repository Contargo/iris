package net.contargo.iris.address.w3w;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.contargo.iris.GeoLocation;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

import static org.springframework.http.ResponseEntity.ok;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class ThreeWordClientUnitTest {

    private static final String API_KEY = "apikey";
    private static final String REQUEST_URI =
        "https://api.what3words.com/v2/forward?addr={w3wAddress}&key={apiKey}&lang=de";

    private ThreeWordClient sut;

    @Mock
    private RestTemplate restTemplateMock;

    @Before
    public void setUp() {

        sut = new ThreeWordClient(restTemplateMock, API_KEY);
    }


    @Test
    public void resolve() throws IOException {

        when(restTemplateMock.getForEntity(REQUEST_URI, ForwardW3wResponse.class, "wohin.polizist.meinung", API_KEY))
            .thenReturn(ok(response()));

        GeoLocation geoLocation = sut.resolve("wohin.polizist.meinung");

        assertThat(geoLocation, is(new GeoLocation(new BigDecimal("52.520119"), new BigDecimal("13.369304"))));
    }


    @Test(expected = ThreeWordClientException.class)
    public void resolveError() throws IOException {

        when(restTemplateMock.getForEntity(REQUEST_URI, ForwardW3wResponse.class, "wohin.polizist.meinung", API_KEY))
            .thenReturn(ok(errorResponse()));

        sut.resolve("wohin.polizist.meinung");
    }


    private ForwardW3wResponse response() throws IOException {

        String json = "{"
            + "  \"thanks\": \"Thanks from all of us at index.home.raft for using a what3words API\","
            + "  \"crs\": {"
            + "    \"type\": \"link\","
            + "    \"properties\": {"
            + "      \"href\": \"http://spatialreference.org/ref/epsg/4326/ogcwkt/\","
            + "      \"type\": \"ogcwkt\""
            + "    }"
            + "  },"
            + "  \"words\": \"wohin.polizist.meinung\","
            + "  \"bounds\": {"
            + "    \"southwest\": {"
            + "      \"lng\": 13.369282,"
            + "      \"lat\": 52.520106"
            + "    },"
            + "    \"northeast\": {"
            + "      \"lng\": 13.369326,"
            + "      \"lat\": 52.520133"
            + "    }"
            + "  },"
            + "  \"geometry\": {"
            + "    \"lng\": 13.369304,"
            + "    \"lat\": 52.520119"
            + "  },"
            + "  \"language\": \"de\","
            + "  \"map\": \"http://w3w.co/wohin.polizist.meinung\","
            + "  \"status\": {"
            + "    \"reason\": \"OK\","
            + "    \"status\": 200"
            + "  }"
            + "}";

        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .readValue(json, ForwardW3wResponse.class);
    }


    private ForwardW3wResponse errorResponse() throws IOException {

        String json = "{"
            + "  \"thanks\": \"Thanks from all of us at index.home.raft for using a what3words API\","
            + "  \"status\": {"
            + "    \"reason\": \"OK\","
            + "    \"code\": 300,"
            + "    \"message\": \"Invalid or non-existent 3 word address\","
            + "    \"status\": 200"
            + "  }"
            + "}";

        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .readValue(json, ForwardW3wResponse.class);
    }
}
