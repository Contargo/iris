package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
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

import static net.contargo.iris.route.RouteType.BARGE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class MainRunConnectionDtoServiceImplUnitTest {

    @Mock
    private MainRunConnectionService mainRunConnectionServiceMock;
    private MainRunConnectionDtoServiceImpl sut;

    @Before
    public void before() {

        sut = new MainRunConnectionDtoServiceImpl(mainRunConnectionServiceMock);
    }


    @Test
    public void getConnectionsForTerminal() {

        BigInteger terminalUID = new BigInteger("2");
        Terminal terminal = new Terminal();
        terminal.setUniqueId(terminalUID);

        BigInteger seaportOneUID = new BigInteger("3");
        Seaport seaportOne = new Seaport();
        seaportOne.setUniqueId(seaportOneUID);

        BigInteger seaportTwoUID = new BigInteger("4");
        Seaport seaportTwo = new Seaport();
        seaportTwo.setUniqueId(seaportTwoUID);

        MainRunConnection c1 = newConnection(seaportOne, terminal, BARGE);
        MainRunConnection c2 = newConnection(seaportTwo, terminal, BARGE);

        when(mainRunConnectionServiceMock.getConnectionsForTerminal(terminalUID)).thenReturn(asList(c1, c2));

        MainRunConnectionDto dto1 = new MainRunConnectionDto(seaportOneUID.toString(), terminalUID.toString(), BARGE);
        MainRunConnectionDto dto2 = new MainRunConnectionDto(seaportTwoUID.toString(), terminalUID.toString(), BARGE);

        List<MainRunConnectionDto> expectedConnections = asList(dto1, dto2);

        assertConnections(sut.getConnectionsForTerminal(terminalUID), expectedConnections);
    }


    private MainRunConnection newConnection(Seaport seaport, Terminal terminal, RouteType type) {

        MainRunConnection connection = new MainRunConnection();
        connection.setSeaport(seaport);
        connection.setTerminal(terminal);
        connection.setRouteType(type);

        return connection;
    }


    private void assertConnections(List<MainRunConnectionDto> actual, List<MainRunConnectionDto> expected) {

        assertThat(actual, hasSize(expected.size()));

        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i).getTerminalUid(), is(expected.get(i).getTerminalUid()));
            assertThat(actual.get(i).getSeaportUid(), is(expected.get(i).getSeaportUid()));
            assertThat(actual.get(i).getRouteType(), is(expected.get(i).getRouteType()));
        }
    }
}
