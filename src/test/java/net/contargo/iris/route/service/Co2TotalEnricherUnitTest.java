package net.contargo.iris.route.service;

import net.contargo.iris.co2.service.Co2Service;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


/**
 * Unit test of {@link Co2TotalEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class Co2TotalEnricherUnitTest {

    private Co2TotalEnricher sut;

    @Mock
    private Co2Service co2ServiceMock;

    private Route route;
    private BigDecimal co2Emission, co2EmissionDirectTruck;

    @Before
    public void setup() {

        co2Emission = BigDecimal.TEN;
        co2EmissionDirectTruck = BigDecimal.ONE;

        route = new Route();

        sut = new Co2TotalEnricher(co2ServiceMock);

        RoutePart routePart = new RoutePart();
        routePart.setRouteType(RouteType.RAIL);
        routePart.getData().setCo2(BigDecimal.TEN);
        route.getData().setParts(singletonList(routePart));

        when(co2ServiceMock.getEmissionDirectTruck(route)).thenReturn(co2EmissionDirectTruck);
        when(co2ServiceMock.getEmission(route)).thenReturn(co2Emission);
    }


    @Test
    public void enrich() throws CriticalEnricherException {

        sut.enrich(route, null);

        assertThat(route.getData().getCo2(), is(co2Emission));
        assertThat(route.getData().getCo2DirectTruck(), is(co2EmissionDirectTruck));

        verify(co2ServiceMock).getEmission(route);
        verify(co2ServiceMock).getEmissionDirectTruck(route);
        verifyNoMoreInteractions(co2ServiceMock);
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichDirectTruckWithCriticalException() throws CriticalEnricherException {

        when(co2ServiceMock.getEmissionDirectTruck(route)).thenThrow(new IllegalStateException());

        sut.enrich(route, null);
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichRouteWithCriticalException() throws CriticalEnricherException {

        when(co2ServiceMock.getEmission(route)).thenThrow(new IllegalStateException());

        sut.enrich(route, null);
    }
}
