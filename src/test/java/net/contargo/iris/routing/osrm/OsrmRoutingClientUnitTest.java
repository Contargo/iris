package net.contargo.iris.routing.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.OsrmRoutingClient;
import net.contargo.iris.routing.RoutingQueryResult;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static net.contargo.iris.routing.osrm.OSRMProfile.DRIVING;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OsrmRoutingClientUnitTest {

    private RestTemplate restTemplateMock = mock(RestTemplate.class);

    private OsrmRoutingClient sut;
    private Osrm5Response osrmResponseMock = mock(Osrm5Response.class);

    @Before
    public void setUp() {

        sut = new OsrmRoutingClient(restTemplateMock, "http://maps1.contargo.net/osrm/route");

        when(osrmResponseMock.getDistance()).thenReturn(new BigDecimal("45.45"));
        when(osrmResponseMock.getToll()).thenReturn(new BigDecimal("34"));
        when(osrmResponseMock.getDuration()).thenReturn(new BigDecimal("20"));
    }


    @Test
    public void route() {

        ResponseEntity<Osrm5Response> response = new ResponseEntity<>(osrmResponseMock, HttpStatus.OK);

        when(restTemplateMock.exchange(
                    "http://maps1.contargo.net/osrm/route/v1/driving/10.0000000000,1.0000000000;1.0000000000,0E-10?"
                    + "overview=false&alternatives=false&steps=true", HttpMethod.GET, HttpEntity.EMPTY,
                    Osrm5Response.class)).thenReturn(response);

        RoutingQueryResult result = sut.route(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN),
                new GeoLocation(BigDecimal.ZERO, BigDecimal.ONE), DRIVING);

        assertThat(result.getTotalDistance(), is(45.45));
        assertThat(result.getToll(), is(new BigDecimal("34")));
        assertThat(result.getTotalTime(), is(20.0));
        assertThat(result.noRoute(), is(false));
    }


    @Test
    public void noRoute() {

        HttpClientErrorException clientException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, null,
                OsrmResponseProvider.osrm5ErrorResponse().getBytes(), null);
        when(restTemplateMock.exchange(
                    "http://maps1.contargo.net/osrm/route/v1/driving/10.0000000000,1.0000000000;1.0000000000,0E-10?"
                    + "overview=false&alternatives=false&steps=true", HttpMethod.GET, HttpEntity.EMPTY,
                    Osrm5Response.class)).thenThrow(clientException);

        RoutingQueryResult result = sut.route(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN),
                new GeoLocation(BigDecimal.ZERO, BigDecimal.ONE), DRIVING);

        assertThat(result.noRoute(), is(true));
    }
}
