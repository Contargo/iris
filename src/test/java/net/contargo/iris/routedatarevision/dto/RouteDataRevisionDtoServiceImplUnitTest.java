package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteDataRevisionDtoServiceImplUnitTest {

    private RouteDataRevisionDtoServiceImpl sut;

    @Mock
    private RouteDataRevisionService routeDataRevisionServiceMock;
    private List<RouteDataRevision> routeDataRevisions;
    private List<RouteDataRevisionDto> routeDataRevisionDtos;
    private Terminal terminal;
    private TerminalDto terminalDto;
    private RouteDataRevision routeDataRevision;
    private RouteDataRevisionDto routeDataRevisionDto;

    @Before
    public void setUp() {

        sut = new RouteDataRevisionDtoServiceImpl(routeDataRevisionServiceMock);

        routeDataRevision = new RouteDataRevision(5L, null, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

        routeDataRevisions = asList(routeDataRevision);

        routeDataRevisionDto = new RouteDataRevisionDto(routeDataRevision);

        routeDataRevisionDtos = asList(routeDataRevisionDto);

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

        when(routeDataRevisionServiceMock.getRouteDataRevisions(1L)).thenReturn(routeDataRevisions);

        List<RouteDataRevisionDto> resultList = sut.getRouteDataRevisions(1L);
        assertThat(resultList.size(), is(1));
        assertThat(resultList.get(0).getId(), is(5L));
    }


    @Test
    public void getRouteDataRevisionById() {

        when(routeDataRevisionServiceMock.getRouteDataRevision(5L)).thenReturn(routeDataRevision);

        RouteDataRevisionDto result = sut.getRouteDataRevision(5L);
        assertThat(result.getId(), is(5L));
    }


    @Test
    public void save() {

        when(routeDataRevisionServiceMock.save(
                Matchers.argThat(org.hamcrest.Matchers.<RouteDataRevision>hasProperty("id", is(5L))))).thenReturn(
            routeDataRevision);

        RouteDataRevisionDto result = sut.save(routeDataRevisionDto);
        assertThat(result.getId(), is(5L));
    }


    @Test
    public void existsEntry() {

        when(routeDataRevisionServiceMock.existsEntry(terminal, BigDecimal.TEN, BigDecimal.ONE)).thenReturn(true);

        assertThat(sut.existsEntry(terminal, BigDecimal.TEN, BigDecimal.ONE), is(true));
        verify(routeDataRevisionServiceMock).existsEntry(terminal, BigDecimal.TEN, BigDecimal.ONE);
    }
}
