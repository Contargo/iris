package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigInteger;

import java.util.List;

import static net.contargo.iris.route.RouteType.BARGE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;


/**
 * Unit test of {@link MainRunConnectionDtoServiceImpl}.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class MainRunConnectionDtoServiceImplUnitTest {

    private MainRunConnectionDtoServiceImpl sut;

    @Mock
    private MainRunConnectionService mainRunConnectionServiceMock;

    private BigInteger terminalUID;
    private Terminal terminal;
    private BigInteger seaportOneUID;
    private Seaport seaportOne;
    private BigInteger seaportTwoUID;
    private Seaport seaportTwo;

    @Before
    public void before() {

        sut = new MainRunConnectionDtoServiceImpl(mainRunConnectionServiceMock);

        terminalUID = new BigInteger("2");
        terminal = new Terminal();
        terminal.setUniqueId(terminalUID);

        seaportOneUID = new BigInteger("3");
        seaportOne = new Seaport();
        seaportOne.setUniqueId(seaportOneUID);

        seaportTwoUID = new BigInteger("4");
        seaportTwo = new Seaport();
        seaportTwo.setUniqueId(seaportTwoUID);
    }


    @Test
    public void getConnectionsForTerminal() {

        MainRunConnection c1 = newConnection(seaportOne, terminal, BARGE);
        MainRunConnection c2 = newConnection(seaportTwo, terminal, BARGE);

        when(mainRunConnectionServiceMock.getConnectionsForTerminal(terminalUID)).thenReturn(asList(c1, c2));

        SimpleMainRunConnectionDto dto1 = new SimpleMainRunConnectionDto(seaportOneUID.toString(),
                terminalUID.toString(), BARGE);
        SimpleMainRunConnectionDto dto2 = new SimpleMainRunConnectionDto(seaportTwoUID.toString(),
                terminalUID.toString(), BARGE);

        assertConnections(sut.getConnectionsForTerminal(terminalUID), asList(dto1, dto2));
    }


    @Test
    public void get() {

        when(mainRunConnectionServiceMock.getById(42L)).thenReturn(newConnection(seaportOne, terminal, BARGE));

        MainRunConnectionDto dto = sut.get(42L);

        assertThat(dto.getRouteType(), is(BARGE));
    }


    @Test
    public void save() {

        MainRunConnectionDto dto = new MainRunConnectionDto(42L, ONE.toString(), TEN.toString(), ONE, TEN, ZERO, ONE,
                BARGE, true);

        MainRunConnection connection = newConnection(seaportOne, terminal, BARGE);
        connection.setId(42L);

        when(mainRunConnectionServiceMock.save(any(MainRunConnection.class))).thenReturn(connection);

        MainRunConnectionDto savedDto = sut.save(dto);

        assertThat(savedDto.getId(), is(42L));

        ArgumentCaptor<MainRunConnection> captor = ArgumentCaptor.forClass(MainRunConnection.class);
        verify(mainRunConnectionServiceMock).save(captor.capture());

        MainRunConnection value = captor.getValue();
        assertThat(value.getId(), is(42L));
        assertThat(value.getRouteType(), is(BARGE));
    }


    private MainRunConnection newConnection(Seaport seaport, Terminal terminal, RouteType type) {

        MainRunConnection connection = new MainRunConnection();
        connection.setSeaport(seaport);
        connection.setTerminal(terminal);
        connection.setRouteType(type);

        return connection;
    }


    private void assertConnections(List<SimpleMainRunConnectionDto> actual,
        List<SimpleMainRunConnectionDto> expected) {

        assertThat(actual, hasSize(expected.size()));

        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i).getTerminalUid(), is(expected.get(i).getTerminalUid()));
            assertThat(actual.get(i).getSeaportUid(), is(expected.get(i).getSeaportUid()));
            assertThat(actual.get(i).getRouteType(), is(expected.get(i).getRouteType()));
        }
    }
}
