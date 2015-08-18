package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.truck.service.OSRMNonRoutableRouteException;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


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
    @Mock
    private Route routeMock;
    @Mock
    private RouteData routeDataMock;

    @Captor
    private ArgumentCaptor<Map<String, String>> mapCaptor;

    private RoutePart routePart;

    @Before
    public void setUp() throws Exception {

        routePart = new RoutePart(new GeoLocation(BigDecimal.TEN, BigDecimal.TEN),
                new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO), RouteType.RAIL);

        when(routeDataMock.getParts()).thenReturn(asList(routePart));
        when(routeMock.getData()).thenReturn(routeDataMock);

        sut = new EnricherServiceImpl(asList(routePartEnricherMock), asList(routeTotalEnricherMock));
    }


    @Test
    public void enrich() throws CriticalEnricherException {

        sut.enrich(routeMock);

        verify(routeTotalEnricherMock).enrich(any(Route.class), any(EnricherContext.class));
        verify(routePartEnricherMock).enrich(any(RoutePart.class), any(EnricherContext.class));
    }


    @Test(expected = OSRMNonRoutableRouteException.class)
    public void enrichOSRMFailure() throws CriticalEnricherException {

        doThrow(OSRMNonRoutableRouteException.class).when(routePartEnricherMock)
            .enrich(any(RoutePart.class), any(EnricherContext.class));

        sut.enrich(routeMock);

        verify(routeTotalEnricherMock).enrich(any(Route.class), any(EnricherContext.class));
        verify(routePartEnricherMock).enrich(any(RoutePart.class), any(EnricherContext.class));
    }


    @Test
    public void enrichCriticalEnricherException() throws CriticalEnricherException {

        doThrow(CriticalEnricherException.class).when(routePartEnricherMock)
            .enrich(any(RoutePart.class), any(EnricherContext.class));

        sut.enrich(routeMock);

        verify(routeMock).setData(Matchers.argThat(
                org.hamcrest.Matchers.<RouteData>hasProperty("parts", is(asList(routePart)))));
        verify(routeMock).setErrors(mapCaptor.capture());
        assertThat(mapCaptor.getValue().get("route"), is("Routing failed"));
    }
}
