package net.contargo.iris.connection;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportSubConnectionUnitTest {

    private SeaportSubConnection sut;
    private Seaport seaport;
    private Terminal terminal;

    @Before
    public void setUp() {

        seaport = new Seaport(new GeoLocation(ONE, ONE));
        terminal = new Terminal(new GeoLocation(TEN, TEN));

        sut = new SeaportSubConnection();
        sut.setSeaport(seaport);
        sut.setTerminal(terminal);
    }


    @Test
    public void isEnabled() {

        seaport.setEnabled(true);
        terminal.setEnabled(true);

        assertThat(sut.isEnabled(), is(true));
    }


    @Test
    public void isNotEnabled() {

        seaport.setEnabled(false);
        terminal.setEnabled(true);

        assertThat(sut.isEnabled(), is(false));
    }


    @Test
    public void matchesOriginAndDestination() {

        assertThat(sut.matchesOriginAndDestination(seaport, terminal, false), is(true));
        assertThat(sut.matchesOriginAndDestination(seaport, terminal, true), is(false));
        assertThat(sut.matchesOriginAndDestination(terminal, seaport, false), is(false));
        assertThat(sut.matchesOriginAndDestination(terminal, seaport, true), is(true));
    }
}
