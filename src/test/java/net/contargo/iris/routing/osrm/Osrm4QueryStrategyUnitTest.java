package net.contargo.iris.routing.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routing.RoutingException;
import net.contargo.iris.routing.RoutingQueryResult;
import net.contargo.iris.routing.RoutingQueryStrategy;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.http.HttpStatus.OK;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;


/**
 * Unit test of {@link Osrm4QueryStrategy}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class Osrm4QueryStrategyUnitTest {

    private static final String BASE_URL = "baseUrl";
    private static final double TOTAL_DISTANCE = 28295.0;
    private static final double TOTAL_TIME = 2264.0;
    private static final GeoLocation START_LOCATION = new GeoLocation(ONE, ONE);
    private static final GeoLocation DESTINATION_LOCATION = new GeoLocation(TEN, TEN);

    private RoutingQueryStrategy sut;

    @Mock
    private RestTemplate osrmRestClientMock;

    @Before
    public void setUp() throws Exception {

        sut = new Osrm4QueryStrategy(osrmRestClientMock, BASE_URL);
    }


    @Test
    public void getOSRMXmlRoute() {

        OSRM4ResponseRouteSummary summary = new OSRM4ResponseRouteSummary();
        summary.setTotalDistance(TOTAL_DISTANCE);
        summary.setTotalTime(TOTAL_TIME);

        OSRM4Response osrm4Response = new OSRM4Response();
        osrm4Response.setStatus(200);
        osrm4Response.setRoute_summary(summary);

        ResponseEntity<OSRM4Response> responseEntity = new ResponseEntity<>(osrm4Response, OK);
        when(osrmRestClientMock.getForEntity(anyString(), eq(OSRM4Response.class))).thenReturn(responseEntity);

        RoutingQueryResult osrmResult = sut.route(START_LOCATION, DESTINATION_LOCATION);

        assertThat(osrmResult.getStatus(), is(200));
        assertThat(osrmResult.getTotalDistance(), is(TOTAL_DISTANCE));
        assertThat(osrmResult.getTotalTime(), is(TOTAL_TIME));
    }


    @Test
    public void getOSRMXmlRouteNoRoute() throws IOException {

        OSRM4Response osrm4Response = new OSRM4Response();
        osrm4Response.setStatus(207);

        ResponseEntity<OSRM4Response> responseEntity = new ResponseEntity<>(osrm4Response, OK);
        when(osrmRestClientMock.getForEntity(anyString(), eq(OSRM4Response.class))).thenReturn(responseEntity);

        RoutingQueryResult actualResult = sut.route(START_LOCATION, new GeoLocation(TEN, TEN));

        assertThat(actualResult.getStatus(), is(207));
        assertThat(actualResult.getTotalDistance(), is(0d));
        assertThat(actualResult.getTotalTime(), is(0d));
    }


    @Test(expected = RoutingException.class)
    public void getOSRMXmlRouteThrowsException() throws IOException {

        doThrow(RoutingException.class).when(osrmRestClientMock).getForEntity(anyString(), eq(OSRM4Response.class));

        sut.route(START_LOCATION, new GeoLocation(TEN, TEN));
    }
}
