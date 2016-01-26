package net.contargo.iris.connection.service;

import net.contargo.iris.connection.persistence.MainRunConnectionRepository;
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
import static net.contargo.iris.route.RouteType.TRUCK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class SeaportTerminalConnectionServiceImplUnitTest {

    private SeaportTerminalConnectionServiceImpl sut;

    @Mock
    private MainRunConnectionRepository mainRunConnectionRepositoryMock;
    @Mock
    private SeaportService seaportServiceMock;
    @Mock
    private TerminalService terminalServiceMock;

    private Terminal terminal;
    private Seaport seaport;

    @Before
    public void setUp() {

        sut = new SeaportTerminalConnectionServiceImpl(mainRunConnectionRepositoryMock, seaportServiceMock,
                terminalServiceMock);

        terminal = new Terminal();
        seaport = new Seaport();
        seaport.setUniqueId(BigInteger.ONE);
    }


    @Test
    public void findSeaPortsConnectedByTruckRouteType() {

        when(seaportServiceMock.getAllActive()).thenReturn(singletonList(seaport));

        List<Seaport> seaports = sut.findSeaPortsConnectedByRouteType(TRUCK);

        assertThat(seaports, hasSize(1));
        assertThat(seaports, contains(seaport));

        verifyZeroInteractions(mainRunConnectionRepositoryMock);
    }


    @Test
    public void findSeaPortsConnectedByRailRouteType() {

        when(mainRunConnectionRepositoryMock.findSeaportsConnectedByRouteType(RAIL)).thenReturn(singletonList(seaport));

        List<Seaport> seaports = sut.findSeaPortsConnectedByRouteType(RAIL);

        assertThat(seaports, hasSize(1));
        assertThat(seaports, contains(seaport));

        verifyZeroInteractions(seaportServiceMock);
    }


    @Test
    public void getTerminalsConnectedToSeaPortByTruckRouteType() {

        when(terminalServiceMock.getAllActive()).thenReturn(singletonList(terminal));

        List<Terminal> terminals = sut.getTerminalsConnectedToSeaPortByRouteType(seaport, TRUCK);

        assertThat(terminals, hasSize(1));
        assertThat(terminals, contains(terminal));

        verifyZeroInteractions(mainRunConnectionRepositoryMock);
    }


    @Test
    public void getTerminalsConnectedToSeaPortByRailRouteType() {

        when(mainRunConnectionRepositoryMock.getTerminalsConnectedToSeaPortByRouteType(seaport.getUniqueId(), RAIL))
            .thenReturn(singletonList(terminal));

        List<Terminal> terminals = sut.getTerminalsConnectedToSeaPortByRouteType(seaport, RAIL);

        assertThat(terminals, hasSize(1));
        assertThat(terminals, contains(terminal));

        verifyZeroInteractions(terminalServiceMock);
    }
}
