package net.contargo.iris.connection.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.persistence.MainRunConnectionRepository;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigInteger;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link MainRunConnectionServiceImpl}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class MainRunConnectionServiceImplUnitTest {

    private static final Long CONNECTION_ID = 42L;

    private MainRunConnectionServiceImpl sut;

    @Mock
    private MainRunConnectionRepository mainRunConnectionRepositoryMock;

    private MainRunConnection con1;
    private MainRunConnection con2;
    private Terminal terminal;
    private Seaport seaport;

    @Before
    public void setUp() {

        sut = new MainRunConnectionServiceImpl(mainRunConnectionRepositoryMock);

        con1 = new MainRunConnection();
        con2 = new MainRunConnection();
        terminal = new Terminal();
        seaport = new Seaport();
    }


    @Test
    public void getAll() {

        when(mainRunConnectionRepositoryMock.findAll()).thenReturn(asList(con1, con2));

        List<MainRunConnection> allMainrunConnections = sut.getAll();

        assertThat(allMainrunConnections, hasSize(2));
        assertThat(allMainrunConnections, contains(con1, con2));
    }


    @Test
    public void getAllActive() {

        when(mainRunConnectionRepositoryMock.findByEnabled(true)).thenReturn(asList(con1, con2));

        List<MainRunConnection> activeMainrunConnections = sut.getAllActive();

        assertThat(activeMainrunConnections, hasSize(2));
        assertThat(activeMainrunConnections, contains(con1, con2));
    }


    @Test
    public void getById() {

        when(mainRunConnectionRepositoryMock.findOne(CONNECTION_ID)).thenReturn(con1);

        MainRunConnection mainrunConnection = sut.getById(CONNECTION_ID);
        assertThat(mainrunConnection, is(con1));
    }


    @Test
    public void save() {

        MainRunConnection con = new MainRunConnection();
        con.setId(CONNECTION_ID);

        when(mainRunConnectionRepositoryMock.save(con1)).thenReturn(con);

        MainRunConnection savedMainrunConnection = sut.save(con1);

        assertThat(savedMainrunConnection, is(con));
        assertThat(savedMainrunConnection.getId(), is(CONNECTION_ID));
    }


    @Test
    public void findRoutingConnectionBetweenTerminalAndSeaportByType() {

        when(mainRunConnectionRepositoryMock.findByTerminalAndSeaportAndRouteTypeAndEnabled(terminal, seaport,
                RouteType.BARGE, true)).thenReturn(con1);

        MainRunConnection mainrunConnection = sut.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal,
                seaport, RouteType.BARGE);

        assertThat(mainrunConnection, is(con1));
    }


    @Test
    public void getConnectionsForTerminal() {

        BigInteger terminalUID = new BigInteger("200");

        MainRunConnection c1 = new MainRunConnection();
        MainRunConnection c2 = new MainRunConnection();

        when(mainRunConnectionRepositoryMock.findConnectionsByTerminalUniqueId(terminalUID)).thenReturn(asList(c1, c2));

        assertThat(sut.getConnectionsForTerminal(terminalUID), contains(c1, c2));
    }
}
