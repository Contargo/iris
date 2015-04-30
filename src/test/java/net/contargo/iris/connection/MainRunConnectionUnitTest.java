package net.contargo.iris.connection;

import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


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

        seaport = new Seaport();
        terminal = new Terminal();
    }


    @Test
    public void getTotalDistance() {

        sut.setDieselDistance(BigDecimal.TEN);
        sut.setElectricDistance(BigDecimal.TEN);

        BigDecimal totalDistance = sut.getTotalDistance();

        assertThat(totalDistance, is(new BigDecimal(20)));
    }


    @Test
    public void getEveryThingEnabledFalse() {

        sut.setEnabled(false);

        assertThat(sut.getEverythingEnabled(), is(false));
    }


    @Test
    public void getEveryThingEnabledNullSeaport() {

        sut.setEnabled(true);

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
    public void getEveryThingEnabledNullTerminal() {

        sut.setEnabled(true);

        seaport.setEnabled(true);
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
