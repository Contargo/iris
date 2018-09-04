package net.contargo.iris.connection;

import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.route.RouteType.BARGE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.TEN;


/**
 * Unit test for {@link MainRunConnection}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class MainRunConnectionUnitTest {

    private MainRunConnection sut;
    private Seaport seaport;
    private Terminal terminal;

    @Before
    public void setUp() {

        sut = new MainRunConnection();
        sut.setRouteType(BARGE);

        seaport = new Seaport();
        terminal = new Terminal();
    }


    @Test
    public void getTotalDistance() {

        sut.setRailDieselDistance(TEN);
        sut.setRailElectricDistance(TEN);
        sut.setBargeDieselDistance(TEN);

        BigDecimal totalDistance = sut.getTotalDistance();

        assertThat(totalDistance, is(new BigDecimal(30)));
    }


    @Test
    public void getBargeDieselDistance() {

        sut.setBargeDieselDistance(TEN);
        assertThat(sut.getBargeDieselDistance(), is(TEN));
    }


    @Test
    public void getRailDieselDistance() {

        sut.setRailDieselDistance(TEN);
        assertThat(sut.getRailDieselDistance(), is(TEN));
    }


    @Test
    public void getRailElectricDistance() {

        sut.setRailElectricDistance(TEN);
        assertThat(sut.getRailElectricDistance(), is(TEN));
    }


    @Test
    public void getEveryThingEnabledFalse() {

        sut.setEnabled(false);

        assertThat(sut.getEverythingEnabled(), is(false));
    }


    @Test
    public void getEveryThingEnabledDisabledSeaport() {

        sut.setEnabled(true);

        seaport.setEnabled(false);
        sut.setSeaport(seaport);

        assertThat(sut.getEverythingEnabled(), is(false));
    }


    @Test
    public void getEveryThingEnabledDisabledTerminal() {

        sut.setEnabled(true);

        seaport.setEnabled(true);
        sut.setSeaport(seaport);

        terminal.setEnabled(false);
        sut.setTerminal(terminal);

        assertThat(sut.getEverythingEnabled(), is(false));
    }


    @Test
    public void getEveryThingEnabledTrue() {

        sut.setEnabled(true);

        seaport.setEnabled(true);
        sut.setSeaport(seaport);

        terminal.setEnabled(true);
        sut.setTerminal(terminal);

        assertThat(sut.getEverythingEnabled(), is(true));
    }
}
