package net.contargo.iris.connection;

import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import java.math.BigDecimal;

import java.util.List;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.BARGE_RAIL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static java.math.BigDecimal.TEN;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link MainRunConnection}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class MainRunConnectionUnitTest {

    private MainRunConnection sut;
    private Seaport seaport;
    private Terminal terminal;
    private List<AbstractSubConnection> subConnections;

    @Before
    public void setUp() {

        sut = new MainRunConnection();
        sut.setRouteType(BARGE);

        seaport = new Seaport();
        terminal = new Terminal();

        AbstractSubConnection sub1 = new TerminalSubConnection();
        sub1.setRailDieselDistance(TEN);
        sub1.setRailElectricDistance(TEN);
        sub1.setBargeDieselDistance(TEN);

        AbstractSubConnection sub2 = new TerminalSubConnection();
        sub2.setRailDieselDistance(TEN);
        sub2.setRailElectricDistance(TEN);
        sub2.setBargeDieselDistance(TEN);

        subConnections = asList(sub1, sub2);
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
    public void getTotalDistanceWithSubConnections() {

        sut.setRouteType(BARGE_RAIL);
        sut.setSubConnections(subConnections);

        BigDecimal totalDistance = sut.getTotalDistance();

        assertThat(totalDistance, is(new BigDecimal(60)));
    }


    @Test
    public void getBargeDieselDistance() {

        sut.setBargeDieselDistance(TEN);
        assertThat(sut.getBargeDieselDistance(), is(TEN));
    }


    @Test
    public void getBargeDieselDistanceWithSubConnections() {

        sut.setRouteType(BARGE_RAIL);
        sut.setSubConnections(subConnections);
        assertThat(sut.getBargeDieselDistance(), is(new BigDecimal(20)));
    }


    @Test
    public void getRailDieselDistance() {

        sut.setRailDieselDistance(TEN);
        assertThat(sut.getRailDieselDistance(), is(TEN));
    }


    @Test
    public void getRailDieselDistanceWithSubConnections() {

        sut.setRouteType(BARGE_RAIL);
        sut.setSubConnections(subConnections);
        assertThat(sut.getRailDieselDistance(), is(new BigDecimal(20)));
    }


    @Test
    public void getRailElectricDistance() {

        sut.setRailElectricDistance(TEN);
        assertThat(sut.getRailElectricDistance(), is(TEN));
    }


    @Test
    public void getRailElectricDistanceWithSubConnections() {

        sut.setRouteType(BARGE_RAIL);
        sut.setSubConnections(subConnections);
        assertThat(sut.getRailElectricDistance(), is(new BigDecimal(20)));
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
    public void getEveryThingEnabledDisabledSubconnection() {

        AbstractSubConnection subConnection = Mockito.mock(AbstractSubConnection.class);
        when(subConnection.isEnabled()).thenReturn(false);
        sut.getSubConnections().add(subConnection);

        sut.setEnabled(true);

        seaport.setEnabled(true);
        sut.setSeaport(seaport);

        terminal.setEnabled(true);
        sut.setTerminal(terminal);

        assertThat(sut.getEverythingEnabled(), is(false));
    }


    @Test
    public void getEveryThingEnabledTrue() {

        AbstractSubConnection subConnection = Mockito.mock(AbstractSubConnection.class);
        when(subConnection.isEnabled()).thenReturn(true);
        sut.getSubConnections().add(subConnection);

        sut.setEnabled(true);

        seaport.setEnabled(true);
        sut.setSeaport(seaport);

        terminal.setEnabled(true);
        sut.setTerminal(terminal);

        assertThat(sut.getEverythingEnabled(), is(true));
    }
}
