package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.gis.service.GisService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit test of {@link net.contargo.iris.route.service.AirLineDistancePartEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */

@RunWith(MockitoJUnitRunner.class)
public class AirLineDistancePartEnricherUnitTest {

    private static final BigDecimal METERS_IN_A_KILOMETER = new BigDecimal(1000);

    private AirLineDistancePartEnricher sut;

    @Mock
    private GisService gisServiceMock;
    @Mock
    private RoundingService roundingServiceMock;
    @Mock
    private RoutePart routePartMock;

    // This is no mock because it is final
    private EnricherContext enricherContext;

    private BigDecimal airDis1, airDis2, airDis3;
    private GeoLocation geoLocation;
    private RoutePartData routePartData;

    @Before
    public void setup() {

        geoLocation = new GeoLocation();
        routePartData = new RoutePartData();

        airDis1 = new BigDecimal(42000.0001);
        airDis2 = new BigDecimal(42000.1);
        airDis3 = new BigDecimal(42001.00);

        when(roundingServiceMock.roundDistance(airDis1.divide(METERS_IN_A_KILOMETER))).thenReturn(new BigDecimal(42));
        when(roundingServiceMock.roundDistance(airDis2.divide(METERS_IN_A_KILOMETER))).thenReturn(new BigDecimal(42));
        when(roundingServiceMock.roundDistance(airDis3.divide(METERS_IN_A_KILOMETER))).thenReturn(new BigDecimal(43));
        when(gisServiceMock.calcAirLineDistInMeters(geoLocation, geoLocation)).thenReturn(airDis1, airDis2, airDis3);

        enricherContext = new EnricherContext.Builder().build();

        sut = new AirLineDistancePartEnricher(gisServiceMock, roundingServiceMock);
    }


    @Test
    public void testDelegation() {

        when(routePartMock.getOrigin()).thenReturn(geoLocation);
        when(routePartMock.getDestination()).thenReturn(geoLocation);
        when(routePartMock.getData()).thenReturn(routePartData);

        sut.enrich(routePartMock, enricherContext);
        assertThat(routePartData.getAirLineDistance(), equalTo(new BigDecimal(42)));
        verify(roundingServiceMock).roundDistance(airDis1.divide(METERS_IN_A_KILOMETER));

        sut.enrich(routePartMock, enricherContext);
        assertThat(routePartData.getAirLineDistance(), equalTo(new BigDecimal(42)));
        verify(roundingServiceMock).roundDistance(airDis2.divide(METERS_IN_A_KILOMETER));

        sut.enrich(routePartMock, enricherContext);
        assertThat(routePartData.getAirLineDistance(), equalTo(new BigDecimal(43)));
        verify(roundingServiceMock).roundDistance(airDis3.divide(METERS_IN_A_KILOMETER));
    }
}
