package net.contargo.iris.osrm.service;

import net.contargo.iris.GeoLocation;

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
 * Unit test of {@link OSRMQueryServiceImpl}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class OSRMQueryServiceImplUnitTest {

    private static final String BASE_URL = "baseUrl";
    private static final double TOTAL_DISTANCE = 28295.0;
    private static final double TOTAL_TIME = 2264.0;
    private static final GeoLocation START_LOCATION = new GeoLocation(ONE, ONE);
    private static final GeoLocation DESTINATION_LOCATION = new GeoLocation(TEN, TEN);

    private OSRMQueryService sut;

    @Mock
    private RestTemplate osrmRestClientMock;

    @Before
    public void setUp() throws Exception {

        sut = new OSRMQueryServiceImpl(osrmRestClientMock, BASE_URL);
    }


    @Test
    public void getOSRMXmlRoute() {

        OSRMJsonResponseRouteSummary summary = new OSRMJsonResponseRouteSummary();
        summary.setTotalDistance(TOTAL_DISTANCE);
        summary.setTotalTime(TOTAL_TIME);

        OSRMJsonResponse osrmJsonResponse = new OSRMJsonResponse();
        osrmJsonResponse.setStatus(200);
        osrmJsonResponse.setRoute_summary(summary);

        ResponseEntity<OSRMJsonResponse> responseEntity = new ResponseEntity<>(osrmJsonResponse, OK);
        when(osrmRestClientMock.getForEntity(anyString(), eq(OSRMJsonResponse.class))).thenReturn(responseEntity);

        OSRMQueryResult osrmResult = sut.getOSRMXmlRoute(START_LOCATION, DESTINATION_LOCATION);

        assertThat(osrmResult.getStatus(), is(200));
        assertThat(osrmResult.getTotalDistance(), is(TOTAL_DISTANCE));
        assertThat(osrmResult.getTotalTime(), is(TOTAL_TIME));
    }


    @Test
    public void getOSRMXmlRouteNoRoute() throws IOException {

        OSRMJsonResponse osrmJsonResponse = new OSRMJsonResponse();
        osrmJsonResponse.setStatus(207);

        ResponseEntity<OSRMJsonResponse> responseEntity = new ResponseEntity<>(osrmJsonResponse, OK);
        when(osrmRestClientMock.getForEntity(anyString(), eq(OSRMJsonResponse.class))).thenReturn(responseEntity);

        OSRMQueryResult actualResult = sut.getOSRMXmlRoute(START_LOCATION, new GeoLocation(TEN, TEN));

        assertThat(actualResult.getStatus(), is(207));
        assertThat(actualResult.getTotalDistance(), is(0d));
        assertThat(actualResult.getTotalTime(), is(0d));
    }


    @Test(expected = RoutingException.class)
    public void getOSRMXmlRouteThrowsException() throws IOException {

        doThrow(RoutingException.class).when(osrmRestClientMock).getForEntity(anyString(), eq(OSRMJsonResponse.class));

        sut.getOSRMXmlRoute(START_LOCATION, new GeoLocation(TEN, TEN));
    }
}
