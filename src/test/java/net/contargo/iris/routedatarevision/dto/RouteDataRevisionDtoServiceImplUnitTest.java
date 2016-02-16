package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.RouteRevisionRequest;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;
import net.contargo.iris.util.DateUtil;

import org.joda.time.DateTime;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import static java.util.Collections.singletonList;


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
        routeDataRevision.setTruckDistanceOneWayInKilometer(BigDecimal.ONE);
        routeDataRevision.setTollDistanceOneWayInKilometer(BigDecimal.ONE);
        routeDataRevision.setAirlineDistanceInKilometer(BigDecimal.ONE);
        routeDataRevision.setLatitude(BigDecimal.ONE);
        routeDataRevision.setLatitude(BigDecimal.ONE);
        routeDataRevision.setRadiusInMeter(BigDecimal.ONE);
        routeDataRevision.setValidFrom(new DateTime().toDate());
        routeDataRevision.setValidTo(new DateTime().plusDays(1).toDate());

        routeDataRevisions = singletonList(routeDataRevision);

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

        Date validFrom = new Date();
        ValidityRange validityRange = new ValidityRange(DateUtil.asLocalDate(validFrom),
                DateUtil.asLocalDate(validFrom));
        when(routeDataRevisionServiceMock.overlapsWithExisting(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE,
                    validityRange, null)).thenReturn(true);

        assertThat(sut.existsEntry("1", BigDecimal.TEN, BigDecimal.ONE, validityRange, null), is(true));
        verify(routeDataRevisionServiceMock).overlapsWithExisting(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE,
            validityRange, null);
    }


    @Test
    public void get() {

        GeoLocation geoLocation = new GeoLocation(BigDecimal.TEN, BigDecimal.ONE);

        Terminal terminal = new Terminal();
        terminal.setName("Foo");

        RouteDataRevision revision = new RouteDataRevision();
        revision.setAirlineDistanceInKilometer(BigDecimal.ONE);
        revision.setTerminal(terminal);

        Date date = new Date();

        when(routeDataRevisionServiceMock.getRouteDataRevision(BigInteger.ONE, geoLocation, date)).thenReturn(revision);

        RouteDataRevisionDto result = sut.findNearest("1", geoLocation, date);

        assertThat(result.getAirlineDistanceInKilometer(), is(BigDecimal.ONE));
        assertThat(result.getTerminal().getName(), is("Foo"));
    }


    @Test
    public void search() {

        RouteRevisionRequest routeRevisionRequest = new RouteRevisionRequest();

        when(routeDataRevisionServiceMock.search(routeRevisionRequest)).thenReturn(routeDataRevisions);

        List<RouteDataRevisionDto> searchResult = sut.search(routeRevisionRequest);

        assertThat(searchResult, hasSize(1));
        assertReflectionEquals(routeDataRevisionDto, searchResult.get(0));
    }
}
