package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


@RunWith(MockitoJUnitRunner.class)
public class RouteDataRevisionDtoServiceImplTest {

    private RouteDataRevisionDtoServiceImpl sut;

    @Mock
    private RouteDataRevisionService routeDataRevisionServiceMock;
    private List<RouteDataRevision> routeDataRevisions;
    private List<RouteDataRevisionDto> routeDataRevisionDtoss;
    private Terminal terminal;
    private TerminalDto terminalDto;

    @Before
    public void setUp() {

        sut = new RouteDataRevisionDtoServiceImpl(routeDataRevisionServiceMock);

        RouteDataRevision routeDataRevision = new RouteDataRevision(5L, null, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

        routeDataRevisions = asList(routeDataRevision);

        RouteDataRevisionDto routeDataRevisionDto = new RouteDataRevisionDto(routeDataRevision);

        routeDataRevisionDtoss = asList(routeDataRevisionDto);

        terminal = new Terminal(new GeoLocation(BigDecimal.TEN, BigDecimal.TEN));
        terminalDto = new TerminalDto(terminal);
    }


    @Test
    public void getRouteDataRevisions() {

        when(routeDataRevisionServiceMock.getRouteDataRevisions()).thenReturn(routeDataRevisions);

        List<RouteDataRevisionDto> resultList = sut.getRouteDataRevisions();
        assertThat(resultList.size(), is(1));
        assertThat(resultList.get(0).getId(), is(5L));
    }


    @Test
    public void getRouteDataRevisionsByTerminal() {

        when(routeDataRevisionServiceMock.getRouteDataRevisions(terminal)).thenReturn(routeDataRevisions);

        List<RouteDataRevisionDto> resultList = sut.getRouteDataRevisions(terminalDto);
        assertThat(resultList.size(), is(1));
        assertThat(resultList.get(0).getId(), is(5L));
    }
}
