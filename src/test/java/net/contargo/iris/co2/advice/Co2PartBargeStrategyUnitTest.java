package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.terminal.Region.OBERRHEIN;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;


/**
 * Unit test of {@link net.contargo.iris.co2.advice.Co2PartBargeStrategy}.
 *
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Co2PartBargeStrategyUnitTest {

    private Co2PartBargeStrategy sut = new Co2PartBargeStrategy();

    @Test
    public void getEmissionUpstream() {

        Terminal terminal = new Terminal();
        terminal.setRegion(OBERRHEIN);

        RoutePart routePart = new RoutePart();
        routePart.setRouteType(BARGE);
        routePart.setContainerState(EMPTY);
        routePart.setOrigin(new Seaport());
        routePart.setDestination(terminal);

        routePart.getData().setBargeDieselDistance(new BigDecimal("714"));

        assertThat(sut.getEmissionForRoutePart(routePart), comparesEqualTo(new BigDecimal("285.6")));
    }


    @Test
    public void getEmissionDownstream() {

        Terminal terminal = new Terminal();
        terminal.setRegion(OBERRHEIN);

        RoutePart routePart = new RoutePart();
        routePart.setRouteType(BARGE);
        routePart.setContainerState(EMPTY);
        routePart.setOrigin(terminal);
        routePart.setDestination(new Seaport());

        routePart.getData().setBargeDieselDistance(new BigDecimal("714"));

        assertThat(sut.getEmissionForRoutePart(routePart), comparesEqualTo(new BigDecimal("149.94")));
    }
}
