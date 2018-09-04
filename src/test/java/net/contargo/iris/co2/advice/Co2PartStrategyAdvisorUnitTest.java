package net.contargo.iris.co2.advice;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import static net.contargo.iris.route.RouteType.BARGE;
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
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class Co2PartStrategyAdvisorUnitTest {

    private Co2PartStrategyAdvisor sut;

    private Co2PartStrategy bargeStrategy = new Co2PartBargeStrategy();
    private Co2PartStrategy railStrategy = new Co2PartRailStrategy();
    private Co2PartStrategy truckStrategy = new Co2PartTruckStrategy();
    private Co2PartStrategy dtruckStrategy = new Co2PartDtruckStrategy();

    @Before
    public void before() {

        sut = new Co2PartStrategyAdvisor(bargeStrategy, railStrategy, truckStrategy, dtruckStrategy);
    }


    @Test
    public void adviceForRouteTypeBarge() {

        Co2PartStrategy strategy = sut.advice(BARGE);
        assertThat(strategy, is(bargeStrategy));
    }


    @Test
    public void adviceForRouteTypeRail() {

        Co2PartStrategy strategy = sut.advice(RAIL);
        assertThat(strategy, is(railStrategy));
    }


    @Test
    public void adviceForRouteTypeTruck() {

        Co2PartStrategy strategy = sut.advice(TRUCK);
        assertThat(strategy, is(truckStrategy));
    }


    @Test
    public void adviceForRouteTypeDtruck() {

        Co2PartStrategy strategy = sut.advice(DTRUCK);
        assertThat(strategy, is(dtruckStrategy));
    }
}
