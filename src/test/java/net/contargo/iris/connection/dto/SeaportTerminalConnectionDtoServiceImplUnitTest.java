package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.service.SeaportTerminalConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.when;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link net.contargo.iris.connection.dto.SeaportTerminalConnectionDtoService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class SeaportTerminalConnectionDtoServiceImplUnitTest {

    private SeaportTerminalConnectionDtoService sut;

    @Mock
    private SeaportTerminalConnectionService seaportTerminalConnectionServiceMock;

    private Seaport port1;
    private Seaport port2;
    private SeaportDto port2Dto;
    private Terminal terminal1;
    private TerminalDto terminal1Dto;
    private Terminal terminal2;
    private TerminalDto terminal2Dto;

    @Before
    public void setup() {

        sut = new SeaportTerminalConnectionDtoServiceImpl(seaportTerminalConnectionServiceMock);

        createSeaports();
        createTerminals();
    }


    private void createSeaports() {

        port1 = new Seaport();
        port1.setEnabled(true);
        port1.setId(42L);
        port1.setName("Seaport 1");
        port1.setLatitude(BigDecimal.ONE);
        port1.setLongitude(BigDecimal.ONE);

        port2 = new Seaport();
        port2.setEnabled(true);
        port2.setId(23L);
        port2.setName("Seaport 2");
        port2.setLatitude(BigDecimal.TEN);
        port2.setLongitude(BigDecimal.TEN);
        port2Dto = new SeaportDto(port2);
    }


    private void createTerminals() {

        terminal1 = new Terminal();
        terminal1.setEnabled(true);
        terminal1.setId(42L);
        terminal1.setName("Terminal 1");
        terminal1.setLatitude(BigDecimal.ONE);
        terminal1.setLongitude(BigDecimal.ONE);
        terminal1Dto = new TerminalDto(terminal1);

        terminal2 = new Terminal();
        terminal2.setEnabled(true);
        terminal2.setId(23L);
        terminal2.setName("Terminal 2");
        terminal2.setLatitude(BigDecimal.TEN);
        terminal2.setLongitude(BigDecimal.TEN);
        terminal2Dto = new TerminalDto(terminal2);
    }


    @Test
    public void findSeaPortsConnectedByRouteType() {

        when(seaportTerminalConnectionServiceMock.findSeaPortsConnectedByRouteType(RouteType.BARGE)).thenReturn(asList(
                port1, port2));

        List<SeaportDto> seaportDtos = sut.findSeaportsConnectedByRouteType(RouteType.BARGE);

        assertThat(seaportDtos, hasSize(2));
        assertReflectionEquals(seaportDtos.get(0), new SeaportDto(port1));
        assertReflectionEquals(seaportDtos.get(1), new SeaportDto(port2));
    }


    @Test
    public void getTerminalsConnectedToSeaPortByRouteType() {

        when(seaportTerminalConnectionServiceMock.getTerminalsConnectedToSeaPortByRouteType(port2Dto.toEntity(),
                RouteType.RAIL)).thenReturn(asList(terminal1, terminal2));

        List<TerminalDto> terminalDtos = sut.findTerminalsConnectedToSeaPortByRouteType(port2Dto, RouteType.RAIL);

        assertThat(terminalDtos, hasSize(2));
        assertReflectionEquals(terminalDtos.get(0), terminal1Dto);
        assertReflectionEquals(terminalDtos.get(1), terminal2Dto);
    }
}
