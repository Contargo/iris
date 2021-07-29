package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.distance.service.DistanceService;
import net.contargo.iris.duration.service.DurationService;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.routing.RoutingException;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.TruckRouteService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * Unit test of {@link net.contargo.iris.route.service.TruckRoutingPartEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TruckRoutingPartEnricherUnitTest {

    private TruckRoutingPartEnricher sut;

    @Mock
    private TruckRouteService truckRouteServiceMock;
    @Mock
    private DistanceService distanceServiceMock;
    @Mock
    private DurationService durationServiceMock;

    private RoutePart routePart;
    private TruckRoute truckRoute;
    private BigDecimal distance, toll, duration;

    @Before
    public void setup() {

        distance = TEN;
        toll = ONE;
        duration = TEN;

        Terminal destinationTerminal = new Terminal();
        Terminal originTerminal = new Terminal();

        truckRoute = new TruckRoute(ONE, TEN, null);

        routePart = new RoutePart();
        routePart.setOrigin(originTerminal);
        routePart.setDestination(destinationTerminal);

        when(distanceServiceMock.getDistance(truckRoute)).thenReturn(distance);
        when(distanceServiceMock.getTollDistance(truckRoute)).thenReturn(toll);
        when(durationServiceMock.getDuration(truckRoute)).thenReturn(duration);
        when(truckRouteServiceMock.route(originTerminal, destinationTerminal)).thenReturn(truckRoute);

        sut = new TruckRoutingPartEnricher(truckRouteServiceMock, distanceServiceMock, durationServiceMock);
    }


    @Test
    public void enrichTruck() throws CriticalEnricherException {

        routePart.setRouteType(RouteType.TRUCK);

        sut.enrich(routePart, null);

        assertThat(routePart.getData().getDistance(), is(distance));
        verify(distanceServiceMock).getDistance(truckRoute);

        assertThat(routePart.getData().getDieselDistance(), is(distance));
        assertThat(routePart.getData().getElectricDistance(), is(ZERO));
        assertThat(routePart.getData().getDtruckDistance(), is(ZERO));

        assertThat(routePart.getData().getDuration(), is(duration));
        verify(durationServiceMock).getDuration(truckRoute);

        assertThat(routePart.getData().getTollDistance(), is(toll));
        verify(distanceServiceMock).getTollDistance(truckRoute);
    }


    @Test
    public void enrichDTruck() throws CriticalEnricherException {

        routePart.setRouteType(RouteType.DTRUCK);

        sut.enrich(routePart, null);

        assertThat(routePart.getData().getDistance(), is(distance));
        verify(distanceServiceMock).getDistance(truckRoute);

        assertThat(routePart.getData().getDieselDistance(), is(distance));
        assertThat(routePart.getData().getElectricDistance(), is(ZERO));
        assertThat(routePart.getData().getDtruckDistance(), is(TEN));

        assertThat(routePart.getData().getDuration(), is(duration));
        verify(durationServiceMock).getDuration(truckRoute);

        assertThat(routePart.getData().getTollDistance(), is(toll));
        verify(distanceServiceMock).getTollDistance(truckRoute);
    }


    @Test
    public void enrichNoTruckRoute() throws CriticalEnricherException {

        routePart.setRouteType(RouteType.RAIL);

        sut.enrich(routePart, null);

        assertThat(routePart.getData().getDistance(), nullValue());
        verify(distanceServiceMock, never()).getDistance(truckRoute);

        assertThat(routePart.getData().getDieselDistance(), nullValue());
        assertThat(routePart.getData().getElectricDistance(), nullValue());
        assertThat(routePart.getData().getDtruckDistance(), nullValue());

        assertThat(routePart.getData().getDuration(), nullValue());
        verify(durationServiceMock, never()).getDuration(truckRoute);

        assertThat(routePart.getData().getTollDistance(), nullValue());
        verify(distanceServiceMock, never()).getTollDistance(truckRoute);
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichWithCriticalError() throws CriticalEnricherException {

        routePart.setRouteType(RouteType.TRUCK);

        when(truckRouteServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenThrow(
            new RoutingException("", new Exception()));

        sut.enrich(routePart, null);
    }
}
