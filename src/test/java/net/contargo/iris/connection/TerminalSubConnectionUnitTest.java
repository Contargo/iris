package net.contargo.iris.connection;

import net.contargo.iris.GeoLocation;
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
public class TerminalSubConnectionUnitTest {

    private TerminalSubConnection sut;
    private Terminal terminal1;
    private Terminal terminal2;

    @Before
    public void setUp() {

        terminal1 = new Terminal(new GeoLocation(ONE, ONE));
        terminal2 = new Terminal(new GeoLocation(TEN, TEN));

        sut = new TerminalSubConnection();
        sut.setTerminal(terminal1);
        sut.setTerminal2(terminal2);
    }


    @Test
    public void isEnabled() {

        terminal1.setEnabled(true);
        terminal2.setEnabled(true);

        assertThat(sut.isEnabled(), is(true));
    }


    @Test
    public void isNotEnabled() {

        terminal1.setEnabled(false);
        terminal2.setEnabled(true);

        assertThat(sut.isEnabled(), is(false));
    }


    @Test
    public void matchesOriginAndDestination() {

        assertThat(sut.matchesOriginAndDestination(terminal1, terminal2, false), is(true));
        assertThat(sut.matchesOriginAndDestination(terminal1, terminal2, true), is(false));
        assertThat(sut.matchesOriginAndDestination(terminal2, terminal1, false), is(false));
        assertThat(sut.matchesOriginAndDestination(terminal2, terminal1, true), is(true));
    }
}
