package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.truck.service.OSRMNonRoutableRouteException;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import static net.contargo.iris.route.RouteType.RAIL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Collections.singletonList;


/**
 * Unit test of {@link net.contargo.iris.route.service.EnricherServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class EnricherServiceImplUnitTest {

    private EnricherServiceImpl sut;

    @Mock
    private RouteTotalEnricher routeTotalEnricherMock;
    @Mock
    private RoutePartEnricher routePartEnricherMock;

    private Route route;

    @Before
    public void setUp() throws Exception {

        RouteData routeData = new RouteData();
        routeData.setParts(singletonList(new RoutePart(new GeoLocation(TEN, TEN), new GeoLocation(ZERO, ZERO), RAIL)));

        route = new Route();
        route.setData(routeData);

        sut = new EnricherServiceImpl(singletonList(routePartEnricherMock), singletonList(routeTotalEnricherMock));
    }


    @Test
    public void enrich() throws CriticalEnricherException {

        sut.enrich(route);

        verify(routeTotalEnricherMock).enrich(any(Route.class), any(EnricherContext.class));
        verify(routePartEnricherMock).enrich(any(RoutePart.class), any(EnricherContext.class));
    }


    @Test(expected = OSRMNonRoutableRouteException.class)
    public void enrichOSRMFailure() throws CriticalEnricherException {

        doThrow(OSRMNonRoutableRouteException.class).when(routePartEnricherMock)
            .enrich(any(RoutePart.class), any(EnricherContext.class));

        sut.enrich(route);

        verify(routeTotalEnricherMock).enrich(any(Route.class), any(EnricherContext.class));
        verify(routePartEnricherMock).enrich(any(RoutePart.class), any(EnricherContext.class));
    }


    @Test
    public void enrichCriticalEnricherException() throws CriticalEnricherException {

        doThrow(CriticalEnricherException.class).when(routePartEnricherMock)
            .enrich(any(RoutePart.class), any(EnricherContext.class));

        sut.enrich(route);

        assertThat(route.getErrors().get("route"), is("Routing failed"));
    }
}
