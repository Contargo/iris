package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

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
    @Mock
    private TerminalService terminalServiceMock;
    private List<RouteDataRevision> routeDataRevisions;
    private Terminal terminal;
    private RouteDataRevision routeDataRevision;
    private RouteDataRevisionDto routeDataRevisionDto;

    @Before
    public void setUp() {

        sut = new RouteDataRevisionDtoServiceImpl(terminalServiceMock, routeDataRevisionServiceMock);

        terminal = new Terminal(new GeoLocation(BigDecimal.TEN, BigDecimal.TEN));
        terminal.setUniqueId(BigInteger.ONE);

        routeDataRevision = new RouteDataRevision();
        routeDataRevision.setId(5L);
        routeDataRevision.setTerminal(terminal);
        routeDataRevision.setTruckDistanceOneWayInMeter(BigDecimal.ONE);
        routeDataRevision.setTollDistanceOneWayInMeter(BigDecimal.ONE);
        routeDataRevision.setAirlineDistanceInMeter(BigDecimal.ONE);
        routeDataRevision.setLatitude(BigDecimal.ONE);
        routeDataRevision.setLatitude(BigDecimal.ONE);
        routeDataRevision.setRadiusInMeter(BigDecimal.ONE);

        routeDataRevisions = asList(routeDataRevision);

        routeDataRevisionDto = new RouteDataRevisionDto(routeDataRevision);
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
        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);

        RouteDataRevisionDto result = sut.save(routeDataRevisionDto);
        assertThat(result.getId(), is(5L));
    }


    @Test
    public void existsEntry() {

        when(routeDataRevisionServiceMock.existsEntry(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE)).thenReturn(true);

        assertThat(sut.existsEntry("1", BigDecimal.TEN, BigDecimal.ONE), is(true));
        verify(routeDataRevisionServiceMock).existsEntry(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE);
    }
}
