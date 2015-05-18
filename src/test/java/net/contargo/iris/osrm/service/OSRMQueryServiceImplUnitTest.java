package net.contargo.iris.osrm.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.util.HttpUtil;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.when;

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
    private static final String OSRM_RSP_WRONG_PARAM = "{\"alternative_names\": [[\"Kriegsstraße/;primary/;no/;DE\"]]}";

    private OSRMQueryService sut;

    @Mock
    private HttpUtil httpUtilMock;

    @Before
    public void setUp() throws Exception {

        String response = new ObjectMapper().readTree(new File("src/test/resources/osrm/response.json")).toString();
        when(httpUtilMock.getResponseContent(anyString())).thenReturn(response);

        OSRMJsonResponse OSRMJsonResponse = new OSRMJsonResponse();
        OSRMJsonResponseRouteSummary summary = new OSRMJsonResponseRouteSummary();
        summary.setTotalDistance(TOTAL_DISTANCE);
        summary.setTotalTime(TOTAL_TIME);
        OSRMJsonResponse.setRoute_summary(summary);

        sut = new OSRMQueryServiceImpl(httpUtilMock, BASE_URL, new ObjectMapper());
    }


    @Test
    public void getOSRMXmlRoute() {

        GeoLocation start = new GeoLocation(ONE, ONE);
        GeoLocation destination = new GeoLocation(TEN, TEN);

        OSRMQueryResult actualResult = sut.getOSRMXmlRoute(start, destination);

        assertThat(actualResult.getStatus(), is(0));
        assertThat(actualResult.getTotalDistance(), is(TOTAL_DISTANCE));
        assertThat(actualResult.getTotalTime(), is(TOTAL_TIME));
        assertThat(actualResult.getRouteInstructions().length, is(23));
        assertThat(actualResult.getRouteInstructions()[0][1], is("Hafenstraße/;tertiary/;no/;DE"));
        assertThat(actualResult.getRouteInstructions()[5][4], is("19"));
    }


    @Test
    public void getOSRMXmlRouteNoRoute() throws IOException {

        String r = new ObjectMapper().readTree(new File("src/test/resources/osrm/osrmResponseNoRoute.json")).toString();
        when(httpUtilMock.getResponseContent(anyString())).thenReturn(r);

        GeoLocation start = new GeoLocation(ONE, ONE);
        GeoLocation destination = new GeoLocation(TEN, TEN);

        OSRMQueryResult actualResult = sut.getOSRMXmlRoute(start, destination);

        assertThat(actualResult.getStatus(), is(207));
        assertThat(actualResult.getTotalDistance(), is(0d));
        assertThat(actualResult.getTotalTime(), is(0d));
        assertThat(actualResult.getRouteInstructions().length, is(0));
    }


    @Test(expected = RoutingException.class)
    public void getOSRMXmlRouteThrowsException() throws IOException {

        GeoLocation start = new GeoLocation(ONE, ONE);
        GeoLocation destination = new GeoLocation(TEN, TEN);

        when(httpUtilMock.getResponseContent(anyString())).thenReturn(OSRM_RSP_WRONG_PARAM);

        sut.getOSRMXmlRoute(start, destination);
    }
}
