package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;


/**
 * Unit test of {@link Co2PartRailStrategy}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartRailStrategyUnitTest {

    private Co2PartRailStrategy sut = new Co2PartRailStrategy();

    @Test
    public void getEmissionForRoutePartExport() {

        RoutePart routePart = new RoutePart();
        routePart.setOrigin(new Terminal());
        routePart.setDestination(new Seaport());
        routePart.getData().setRailDieselDistance(new BigDecimal("105"));
        routePart.getData().setElectricDistance(new BigDecimal("530"));

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);

        assertThat(co2, comparesEqualTo(new BigDecimal("191.66")));
    }


    @Test
    public void getEmissionForRoutePartImport() {

        RoutePart routePart = new RoutePart();
        routePart.setOrigin(new Seaport());
        routePart.setDestination(new Terminal());
        routePart.getData().setRailDieselDistance(new BigDecimal("105"));
        routePart.getData().setElectricDistance(new BigDecimal("530"));

        BigDecimal co2 = sut.getEmissionForRoutePart(routePart);

        assertThat(co2, comparesEqualTo(new BigDecimal("221.06")));
    }
}
