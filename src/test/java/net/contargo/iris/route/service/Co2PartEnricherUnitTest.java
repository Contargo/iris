package net.contargo.iris.route.service;

import net.contargo.iris.co2.advice.Co2PartStrategy;
import net.contargo.iris.co2.advice.Co2PartStrategyAdvisor;
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

import static org.mockito.Mockito.when;


/**
 * Unit test of {@link net.contargo.iris.route.service.Co2PartEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class Co2PartEnricherUnitTest {

    private Co2PartEnricher sut;

    @Mock
    private Co2PartStrategyAdvisor co2PartStrategyAdvisorMock;
    @Mock
    private Co2PartStrategy strategyMock;

    private RoutePart routePart;

    @Before
    public void setup() {

        routePart = new RoutePart();
        routePart.setRouteType(RouteType.RAIL);

        sut = new Co2PartEnricher(co2PartStrategyAdvisorMock);
    }


    @Test
    public void enrich() throws CriticalEnricherException {

        when(co2PartStrategyAdvisorMock.advice(RouteType.RAIL)).thenReturn(strategyMock);
        when(strategyMock.getEmissionForRoutePart(routePart)).thenReturn(BigDecimal.TEN);

        sut.enrich(routePart, null);
        assertThat(routePart.getData().getCo2(), is(BigDecimal.TEN));
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichWithCriticalError() throws CriticalEnricherException {

        when(co2PartStrategyAdvisorMock.advice(RouteType.RAIL)).thenThrow(new IllegalStateException());

        sut.enrich(routePart, null);
    }
}
