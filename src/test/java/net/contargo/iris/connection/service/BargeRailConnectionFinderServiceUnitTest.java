package net.contargo.iris.connection.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class BargeRailConnectionFinderServiceUnitTest {

    private BargeRailConnectionFinderService sut;

    private MainRunConnection connection;
    private Seaport seaport1;
    private Terminal terminal1, terminal2, terminal3;
    private SeaportSubConnection seaportSubConnection;
    private TerminalSubConnection terminalSubConnection;
    private SubRoutePart subRoutePart1, subRoutePart2;

    @Before
    public void setUp() {

        seaport1 = new Seaport(new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO));
        terminal1 = new Terminal(new GeoLocation(BigDecimal.TEN, BigDecimal.TEN));
        terminal2 = new Terminal(new GeoLocation(BigDecimal.TEN, BigDecimal.ONE));
        terminal3 = new Terminal(new GeoLocation(BigDecimal.TEN, BigDecimal.ZERO));

        connection = new MainRunConnection();
        connection.setSeaport(seaport1);
        connection.setTerminal(terminal2);

        seaportSubConnection = new SeaportSubConnection();
        seaportSubConnection.setSeaport(seaport1);
        seaportSubConnection.setTerminal(terminal1);

        terminalSubConnection = new TerminalSubConnection();
        terminalSubConnection.setTerminal(terminal1);
        terminalSubConnection.setTerminal2(terminal2);

        subRoutePart1 = new SubRoutePart();
        subRoutePart2 = new SubRoutePart();

        sut = new BargeRailConnectionFinderService();
    }


    @Test
    public void findMatchingBargeRailConnection() {

        connection.setSubConnections(asList(seaportSubConnection, terminalSubConnection));

        subRoutePart1.setOrigin(seaport1);
        subRoutePart1.setDestination(terminal1);

        subRoutePart2.setOrigin(terminal1);
        subRoutePart2.setDestination(terminal2);

        MainRunConnection matchingConnection = sut.findMatchingBargeRailConnection(singletonList(connection),
                asList(subRoutePart1, subRoutePart2));

        assertThat(matchingConnection, is(connection));
    }


    @Test
    public void findMatchingBargeRailConnectionReverse() {

        connection.setSubConnections(asList(seaportSubConnection, terminalSubConnection));

        subRoutePart1.setOrigin(terminal2);
        subRoutePart1.setDestination(terminal1);

        subRoutePart2.setOrigin(terminal1);
        subRoutePart2.setDestination(seaport1);

        MainRunConnection matchingConnection = sut.findMatchingBargeRailConnection(singletonList(connection),
                asList(subRoutePart1, subRoutePart2));

        assertThat(matchingConnection, is(connection));
    }


    @Test
    public void findMatchingBargeRailConnectionWrongSubRouteParts() {

        connection.setSubConnections(asList(seaportSubConnection, terminalSubConnection));

        subRoutePart1.setOrigin(terminal2);
        subRoutePart1.setDestination(terminal3);

        subRoutePart2.setOrigin(terminal3);
        subRoutePart2.setDestination(seaport1);

        MainRunConnection matchingConnection = sut.findMatchingBargeRailConnection(singletonList(connection),
                asList(subRoutePart1, subRoutePart2));

        assertThat(matchingConnection, nullValue());
    }


    @Test
    public void findMatchingBargeRailConnectionDifferentSize() {

        subRoutePart1.setOrigin(seaport1);
        subRoutePart1.setDestination(terminal1);

        MainRunConnection matchingConnection = sut.findMatchingBargeRailConnection(singletonList(connection),
                singletonList(subRoutePart1));

        assertThat(matchingConnection, nullValue());
    }
}
