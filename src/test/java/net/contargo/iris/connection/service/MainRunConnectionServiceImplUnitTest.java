package net.contargo.iris.connection.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.persistence.MainRunConnectionRepository;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigInteger;

import java.util.List;

import static net.contargo.iris.route.RouteType.RAIL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


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
    @Mock
    private SeaportService seaportServiceMock;
    @Mock
    private TerminalService terminalServiceMock;

    private MainRunConnection con1, con2;
    private Terminal terminal;
    private Seaport seaport;

    @Before
    public void setUp() {

        sut = new MainRunConnectionServiceImpl(mainRunConnectionRepositoryMock, seaportServiceMock,
                terminalServiceMock);

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

        Seaport seaport = new Seaport();
        seaport.setUniqueId(BigInteger.ONE);
        seaport.setId(42L);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(BigInteger.TEN);
        terminal.setId(23L);

        con1.setSeaport(seaport);
        con1.setTerminal(terminal);

        when(mainRunConnectionRepositoryMock.save(con1)).thenReturn(con);
        when(seaportServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(seaport);
        when(terminalServiceMock.getByUniqueId(BigInteger.TEN)).thenReturn(terminal);

        MainRunConnection savedMainrunConnection = sut.save(con1);

        assertThat(savedMainrunConnection, is(con));
        assertThat(savedMainrunConnection.getId(), is(CONNECTION_ID));
    }


    @Test(expected = DuplicateMainRunConnectionException.class)
    public void saveNewConnectionThatExists() {

        MainRunConnection con = new MainRunConnection();
        con.setRouteType(RouteType.BARGE);

        Seaport seaport = new Seaport();
        seaport.setUniqueId(BigInteger.ONE);
        seaport.setId(42L);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(BigInteger.ONE);
        terminal.setId(23L);

        con.setSeaport(seaport);
        con.setTerminal(terminal);

        when(seaportServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(seaport);
        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);
        when(mainRunConnectionRepositoryMock.existsBySeaportAndTerminalAndRouteType(42L, 23L, RouteType.BARGE))
            .thenReturn(true);

        sut.save(con);
    }


    @Test(expected = DuplicateMainRunConnectionException.class)
    public void updateConnectionThatExists() {

        MainRunConnection con = new MainRunConnection();
        con.setId(65L);
        con.setRouteType(RouteType.BARGE);

        Seaport seaport = new Seaport();
        seaport.setUniqueId(BigInteger.ONE);
        seaport.setId(42L);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(BigInteger.ONE);
        terminal.setId(23L);

        con.setSeaport(seaport);
        con.setTerminal(terminal);

        when(seaportServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(seaport);
        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);
        when(mainRunConnectionRepositoryMock.existsBySeaportAndTerminalAndRouteTypeAndIdNot(42L, 23L, RouteType.BARGE,
                    65L)).thenReturn(true);

        sut.save(con);
    }


    @Test
    public void findRoutingConnectionBetweenTerminalAndSeaportByType() {

        when(mainRunConnectionRepositoryMock.findByTerminalAndSeaportAndRouteTypeAndEnabled(terminal, seaport,
                    RouteType.BARGE, true)).thenReturn(singletonList(con1));

        MainRunConnection mainrunConnection = sut.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal,
                seaport, RouteType.BARGE);

        assertThat(mainrunConnection, is(con1));
    }


    @Test
    public void findRoutingConnectionBetweenTerminalAndSeaportByTypeWithEmptyResults() {

        when(mainRunConnectionRepositoryMock.findByTerminalAndSeaportAndRouteTypeAndEnabled(terminal, seaport,
                    RouteType.BARGE, true)).thenReturn(emptyList());

        MainRunConnection mainrunConnection = sut.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal,
                seaport, RouteType.BARGE);

        assertThat(mainrunConnection, nullValue());
    }


    @Test
    public void getConnectionsForTerminal() {

        BigInteger terminalUID = new BigInteger("200");

        MainRunConnection c1 = new MainRunConnection();
        MainRunConnection c2 = new MainRunConnection();

        when(mainRunConnectionRepositoryMock.findConnectionsByTerminalUniqueId(terminalUID)).thenReturn(asList(c1,
                c2));

        assertThat(sut.getConnectionsForTerminal(terminalUID), contains(c1, c2));
    }


    @Test
    public void getConnectionByTerminalUidAndSeaportUidAndType() {

        MainRunConnection connection = new MainRunConnection();
        connection.setId(42L);

        when(mainRunConnectionRepositoryMock.findConnectionByTerminalUidAndSeaportUidAndType(
                    new BigInteger("123456789"), new BigInteger("111"), RAIL)).thenReturn(connection);

        MainRunConnection result = sut.getConnectionByTerminalUidAndSeaportUidAndType(new BigInteger("123456789"),
                new BigInteger("111"), RAIL);

        assertThat(result.getId(), is(42L));
    }
}
