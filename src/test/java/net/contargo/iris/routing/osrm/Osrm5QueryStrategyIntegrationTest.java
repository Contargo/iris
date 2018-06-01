package net.contargo.iris.routing.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.RoutingQueryResult;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.test.web.client.MockRestServiceServer;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static net.contargo.iris.routing.osrm.OSRMProfile.RAIL;
import static net.contargo.iris.routing.osrm.OsrmResponseProvider.osrm5Response;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


public class Osrm5QueryStrategyIntegrationTest {

    private static final String ENDPOINT = "http://maps/osrm/route";
    private RestTemplate restTemplate = new RestTemplate();

    private Osrm5QueryStrategy sut;

    @Before
    public void setUp() {

        sut = new Osrm5QueryStrategy(restTemplate, ENDPOINT);
    }


    @Test
    public void routeSuccess() {

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        String uri =
            "http://maps/osrm/route/v1/driving/8.6916010000,53.0929840000;8.7335420000,53.0828570000?overview=false&alternatives=false&steps=true";
        mockServer.expect(requestTo(uri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(osrm5Response(), APPLICATION_JSON));

        GeoLocation start = new GeoLocation(new BigDecimal("53.092984"), new BigDecimal("8.691601"));
        GeoLocation end = new GeoLocation(new BigDecimal("53.082857"), new BigDecimal("8.733542"));

        RoutingQueryResult result = sut.route(start, end);

        assertThat(result.noRoute(), is(false));
        assertThat(result.getTotalTime(), is(503.6));
        assertThat(result.getTotalDistance(), is(8249.1));
        assertThat(result.getToll(), is(new BigDecimal("5.2908")));
    }


    @Test
    public void routeSuccessWithModeOfTransport() {

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        String uri =
            "http://maps/osrm/route/v1/rail/8.6916010000,53.0929840000;8.7335420000,53.0828570000?overview=false&alternatives=false&steps=true";
        mockServer.expect(requestTo(uri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(osrm5Response(), APPLICATION_JSON));

        GeoLocation start = new GeoLocation(new BigDecimal("53.092984"), new BigDecimal("8.691601"));
        GeoLocation end = new GeoLocation(new BigDecimal("53.082857"), new BigDecimal("8.733542"));

        RoutingQueryResult result = sut.route(start, end, RAIL);

        assertThat(result.noRoute(), is(false));
        assertThat(result.getTotalTime(), is(503.6));
        assertThat(result.getTotalDistance(), is(8249.1));
        assertThat(result.getToll(), is(new BigDecimal("5.2908")));
    }


    @Test
    public void routeError() {

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        String uri =
            "http://maps/osrm/route/v1/driving/8.6916010000,53.0929840000;8.7335420000,53.0828570000?overview=false&alternatives=false&steps=true";
        mockServer.expect(requestTo(uri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST).body(OsrmResponseProvider.osrm5ErrorResponse()));

        GeoLocation start = new GeoLocation(new BigDecimal("53.092984"), new BigDecimal("8.691601"));
        GeoLocation end = new GeoLocation(new BigDecimal("53.082857"), new BigDecimal("8.733542"));

        RoutingQueryResult result = sut.route(start, end);

        assertThat(result.noRoute(), is(true));
    }
}
