package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.terminal.Region.OBERRHEIN;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;

import static org.mockito.Mockito.when;


/**
 * Unit test of {@link net.contargo.iris.co2.advice.Co2PartBargeStrategy}.
 *
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class Co2PartBargeStrategyUnitTest {

    @Mock
    private Terminal terminalMock;
    @Mock
    private Co2BargeRegionMap co2BargeRegionMapMock;

    private Co2PartBargeStrategy sut;

    @Before
    public void before() {

        sut = new Co2PartBargeStrategy(co2BargeRegionMapMock);
    }


    @Test
    public void getEmission() {

        RoutePart routePart = new RoutePart();
        routePart.setRouteType(BARGE);
        routePart.setContainerState(EMPTY);

        // seaport -> terminal (so direction is upstream)
        routePart.setOrigin(new Seaport());
        routePart.setDestination(terminalMock);

        routePart.getData().setBargeDieselDistance(new BigDecimal("2"));

        when(terminalMock.getRegion()).thenReturn(OBERRHEIN);
        when(co2BargeRegionMapMock.getCo2Factor(OBERRHEIN, routePart.getDirection(), EMPTY)).thenReturn(new BigDecimal(
                "0.4"));

        BigDecimal expected = BigDecimal.valueOf(2 * 0.4);
        assertThat(sut.getEmissionForRoutePart(routePart), comparesEqualTo(expected));
    }
}
