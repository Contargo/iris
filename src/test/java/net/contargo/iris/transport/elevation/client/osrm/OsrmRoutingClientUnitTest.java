package net.contargo.iris.transport.elevation.client.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.elevation.dto.Point2D;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class OsrmRoutingClientUnitTest {

    private static final BigDecimal START_LON = new BigDecimal("8.45646354");
    private static final BigDecimal START_LAT = new BigDecimal("49.45646354");
    private static final BigDecimal END_LON = new BigDecimal("9.45646354");
    private static final BigDecimal END_LAT = new BigDecimal("50.45646354");
    private OsrmRoutingClient sut;

    @Mock
    private RestTemplate restTemplateMock;

    @Before
    public void setUp() {

        sut = new OsrmRoutingClient(restTemplateMock, "https://mapshost");
    }


    @Test
    public void getPoints() {

        GeoLocation start = new GeoLocation(START_LAT, START_LON);
        GeoLocation end = new GeoLocation(END_LAT, END_LON);

        AnnotatedOsrmResponse.Route.Leg.Annotation annotation = new AnnotatedOsrmResponse.Route.Leg.Annotation(asList(
                    5467856L, 8987654L));
        AnnotatedOsrmResponse.Route.Leg leg = new AnnotatedOsrmResponse.Route.Leg(annotation);
        AnnotatedOsrmResponse.Route.Geometry geometry = new AnnotatedOsrmResponse.Route.Geometry(asList(
                    new BigDecimal[] { START_LON, START_LAT }, new BigDecimal[] { END_LON, END_LAT }));
        AnnotatedOsrmResponse.Route route = new AnnotatedOsrmResponse.Route(geometry, singletonList(leg));

        AnnotatedOsrmResponse response = new AnnotatedOsrmResponse(singletonList(route));

        when(restTemplateMock.getForObject(
                    eq(
                        "{osrmHost}/v1/driving/{lon1},{lat1};{lon2},{lat2}?overview=full&geometries=geojson&annotations=nodes"),
                    eq(AnnotatedOsrmResponse.class), eq("https://mapshost"), any(), any(), any(), any())).thenReturn(
            response);

        List<Point2D> result = sut.getPoints(start, end);

        assertThat(result, hasSize(2));

        assertThat(result.get(0).getLatitude(), is(START_LAT));
        assertThat(result.get(0).getLongitude(), is(START_LON));
        assertThat(result.get(0).getOsmId(), is(5467856L));

        assertThat(result.get(1).getLatitude(), is(END_LAT));
        assertThat(result.get(1).getLongitude(), is(END_LON));
        assertThat(result.get(1).getOsmId(), is(8987654L));
    }
}
