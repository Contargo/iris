package net.contargo.iris.transport.inclinations.client.osrm;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.inclinations.dto.Point2D;

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

import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class OsrmRoutingClientTest {

    private OsrmRoutingClient sut;

    @Mock
    private RestTemplate restTemplateMock;

    @Before
    public void setUp() {

        sut = new OsrmRoutingClient(restTemplateMock, "https://mapshost");
    }


    @Test
    public void getPoints() {

        GeoLocation start = new GeoLocation(new BigDecimal("49.45646354"), new BigDecimal("8.45646354"));
        GeoLocation end = new GeoLocation(new BigDecimal("50.45646354"), new BigDecimal("9.45646354"));

        AnnotatedOsrmResponse.Route.Leg.Annotation annotation = new AnnotatedOsrmResponse.Route.Leg.Annotation(asList(
                    5467856L, 8987654L));
        AnnotatedOsrmResponse.Route.Leg leg = new AnnotatedOsrmResponse.Route.Leg(annotation);
        AnnotatedOsrmResponse.Route.Geometry geometry = new AnnotatedOsrmResponse.Route.Geometry(asList(
                    new BigDecimal[] { new BigDecimal("8.45646354"), new BigDecimal("49.45646354") },
                    new BigDecimal[] { new BigDecimal("9.45646354"), new BigDecimal("50.45646354") }));
        AnnotatedOsrmResponse.Route route = new AnnotatedOsrmResponse.Route(geometry, singletonList(leg));

        AnnotatedOsrmResponse response = new AnnotatedOsrmResponse(singletonList(route));

        when(restTemplateMock.getForObject(
                    eq(
                        "{mapsHost}/osrm/route/v1/driving/{lon1},{lat1};{lon2},{lat2}?overview=full&geometries=geojson&annotations=nodes"),
                    eq(AnnotatedOsrmResponse.class), eq("https://mapshost"), eq(new BigDecimal("8.4564635400")),
                    eq(new BigDecimal("49.4564635400")), eq(new BigDecimal("9.4564635400")),
                    eq(new BigDecimal("50.4564635400")))).thenReturn(response);

        List<Point2D> result = sut.getPoints(start, end);

        assertThat(result, hasSize(2));

        assertThat(result.get(0).getLatitude(), is(new BigDecimal("49.45646354")));
        assertThat(result.get(0).getLongitude(), is(new BigDecimal("8.45646354")));
        assertThat(result.get(0).getOsmId(), is(5467856L));

        assertThat(result.get(1).getLatitude(), is(new BigDecimal("50.45646354")));
        assertThat(result.get(1).getLongitude(), is(new BigDecimal("9.45646354")));
        assertThat(result.get(1).getOsmId(), is(8987654L));
    }
}
