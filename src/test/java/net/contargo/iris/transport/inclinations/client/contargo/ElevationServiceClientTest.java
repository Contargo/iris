package net.contargo.iris.transport.inclinations.client.contargo;

import net.contargo.iris.transport.inclinations.dto.Point2D;
import net.contargo.iris.transport.inclinations.dto.Point3D;

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

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class ElevationServiceClientTest {

    private ElevationServiceClient sut;

    @Mock
    private RestTemplate restTemplateMock;

    @Before
    public void setUp() {

        sut = new ElevationServiceClient(restTemplateMock, "https://mapshost");
    }


    @Test
    public void getElevations() {

        ElevationServicePoint3D[] response = new ElevationServicePoint3D[] {
            new ElevationServicePoint3D(new BigDecimal("49.45646354"), new BigDecimal("8.45646354"), 3546321L, 123),
            new ElevationServicePoint3D(new BigDecimal("50.45646354"), new BigDecimal("9.45646354"), 7983168L, 122)
        };
        when(restTemplateMock.postForObject(eq("{mapsHost}/contargo"), anyList(), eq(ElevationServicePoint3D[].class),
                    eq("https://mapshost"))).thenReturn(response);

        List<Point3D> result = sut.getElevations(asList(
                    new Point2D(new BigDecimal("49.45646354"), new BigDecimal("8.45646354"), 3546321L),
                    new Point2D(new BigDecimal("50.45646354"), new BigDecimal("9.45646354"), 7983168L)));

        assertThat(result, hasSize(2));

        assertThat(result.get(0).getLatitude(), is(new BigDecimal("49.45646354")));
        assertThat(result.get(0).getLongitude(), is(new BigDecimal("8.45646354")));
        assertThat(result.get(0).getElevation(), is(123));

        assertThat(result.get(1).getLatitude(), is(new BigDecimal("50.45646354")));
        assertThat(result.get(1).getLongitude(), is(new BigDecimal("9.45646354")));
        assertThat(result.get(1).getElevation(), is(122));
    }
}
