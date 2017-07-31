package net.contargo.iris.co2.advice;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.BARGE_RAIL;
import static net.contargo.iris.route.RouteType.DTRUCK;
import static net.contargo.iris.route.RouteType.RAIL;
import static net.contargo.iris.route.RouteType.TRUCK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * Unit test of {@link Co2PartStrategyAdvisor}.
 *
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class Co2PartStrategyAdvisorUnitTest {

    private Co2PartStrategyAdvisor sut;

    @Mock
    private Co2PartStrategy expectedStrategyMock;

    @Before
    public void before() {

        sut = new Co2PartStrategyAdvisor();
    }


    @Test
    public void adviceForRouteTypeBarge() {

        sut.setBargeStrategy(expectedStrategyMock);

        Co2PartStrategy strategy = sut.advice(BARGE);
        assertThat(strategy, is(expectedStrategyMock));
    }


    @Test
    public void adviceForRouteTypeRail() {

        sut.setRailStrategy(expectedStrategyMock);

        Co2PartStrategy strategy = sut.advice(RAIL);
        assertThat(strategy, is(expectedStrategyMock));
    }


    @Test
    public void adviceForRouteTypeTruck() {

        sut.setTruckStrategy(expectedStrategyMock);

        Co2PartStrategy strategy = sut.advice(TRUCK);
        assertThat(strategy, is(expectedStrategyMock));
    }


    @Test
    public void adviceForRouteTypeBargeRail() {

        sut.setBargeRailStrategy(expectedStrategyMock);

        Co2PartStrategy strategy = sut.advice(BARGE_RAIL);
        assertThat(strategy, is(expectedStrategyMock));
    }


    @Test
    public void adviceForRouteTypeDtruck() {

        sut.setDtruckStrategy(expectedStrategyMock);

        Co2PartStrategy strategy = sut.advice(DTRUCK);
        assertThat(strategy, is(expectedStrategyMock));
    }
}
