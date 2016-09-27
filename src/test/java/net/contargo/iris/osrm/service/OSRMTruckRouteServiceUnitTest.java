package net.contargo.iris.osrm.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.OSRMNonRoutableRouteException;
import net.contargo.iris.truck.service.OSRMTruckRouteService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * Unit test of {@link net.contargo.iris.truck.service.OSRMTruckRouteService}.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class OSRMTruckRouteServiceUnitTest {

    private static final double DEFAULT_TOTAL_DISTANCE = 1.1;
    private static final double DEFAULT_TOTAL_TIME = 2.2;

    private OSRMTruckRouteService sut;

    @Mock
    private OSRMQueryService queryServiceMock;

    private GeoLocation start;
    private GeoLocation destination;

    @Before
    public void setup() {

        sut = new OSRMTruckRouteService(queryServiceMock);

        destination = new GeoLocation(49.1d, 8.1d);
        start = new GeoLocation(49d, 8d);
    }


    @Test(expected = OSRMNonRoutableRouteException.class)
    public void routeIsNotRoutable() {

        when(queryServiceMock.getOSRMXmlRoute(start, destination)).thenReturn(new OSRMQueryResult(207, 1.1, 2.2, TEN));

        sut.route(start, destination);
    }


    @Test
    public void delegatesCallToQueryService() {

        makeMockReturn(ZERO, DEFAULT_TOTAL_DISTANCE, DEFAULT_TOTAL_TIME);

        TruckRoute route = sut.route(start, destination);
        assertThat(route, notNullValue());
        verify(queryServiceMock).getOSRMXmlRoute(start, destination);
    }


    @Test
    public void parsesSectionsAndReturnsDistanceInMeters() {

        OSRMJsonResponseRouteSummary summary = new OSRMJsonResponseRouteSummary();
        summary.setTotalDistance(12000);
        makeMockReturn(ZERO, 12000, DEFAULT_TOTAL_TIME);

        TruckRoute route = sut.route(start, destination);
        assertThat(route, notNullValue());
        assertThat(route.getDistance(), equalTo(new BigDecimal("12.00000")));
    }


    @Test
    public void parsesSectionsAndReturnsTimeInMinutes() {

        OSRMJsonResponseRouteSummary summary = new OSRMJsonResponseRouteSummary();
        summary.setTotalTime(120);
        makeMockReturn(ZERO, DEFAULT_TOTAL_DISTANCE, 120);

        TruckRoute route = sut.route(start, destination);
        assertThat(route, notNullValue());
        assertThat(route.getDuration(), equalTo(new BigDecimal("2.00000")));
    }


    private void makeMockReturn(BigDecimal toll, double totalDistance, double totalTime) {

        OSRMQueryResult response = new OSRMQueryResult(0, totalDistance, totalTime, toll);

        when(queryServiceMock.getOSRMXmlRoute(start, destination)).thenReturn(response);
    }
}
