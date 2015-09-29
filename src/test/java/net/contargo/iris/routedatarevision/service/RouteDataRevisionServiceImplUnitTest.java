package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.joda.time.DateTime;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.time.LocalDate;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * Unit test of {@link RouteDataRevisionServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteDataRevisionServiceImplUnitTest {

    private RouteDataRevisionService sut;

    @Mock
    private RouteDataRevisionRepository routeDataRevisionRepositoryMock;
    @Mock
    private TerminalService terminalServiceMock;

    private Terminal terminal;

    @Before
    public void before() {

        sut = new RouteDataRevisionServiceImpl(routeDataRevisionRepositoryMock, terminalServiceMock);
        terminal = new Terminal();
    }


    @Test
    public void getRouteDataRevisionByTerminalAndGeolocation() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);
        RouteDataRevision routeDataRevisionDB = new RouteDataRevision();

        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), Mockito.any(Date.class))).thenReturn(routeDataRevisionDB);

        RouteDataRevision routeDataRevision = sut.getRouteDataRevision(terminal, address);

        assertThat(routeDataRevision, is(routeDataRevisionDB));
    }


    @Test
    public void getRouteDataRevisionByTerminalUidAndGeolocataion() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);
        RouteDataRevision routeDataRevisionDB = new RouteDataRevision();

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);
        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), Mockito.any(Date.class))).thenReturn(routeDataRevisionDB);

        RouteDataRevision routeDataRevision = sut.getRouteDataRevision(BigInteger.ONE, address);

        assertThat(routeDataRevision, is(routeDataRevisionDB));
    }


    @Test
    public void getNoRouteDataRevisionByTerminalUidAndGeolocataion() {

        GeoLocation address = new GeoLocation(BigDecimal.ONE, BigDecimal.TEN);

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);
        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), Mockito.any(Date.class))).thenReturn(null);

        try {
            sut.getRouteDataRevision(BigInteger.ONE, new Address(BigDecimal.ONE, BigDecimal.TEN));
        } catch (RevisionDoesNotExistException e) {
            assertThat(e.getCode(), is("routerevision.notfound"));
        }
    }


    @Test
    public void getNoTerminal() {

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(null);

        try {
            sut.getRouteDataRevision(BigInteger.ONE, new Address(BigDecimal.ONE, BigDecimal.TEN));
        } catch (RevisionDoesNotExistException e) {
            assertThat(e.getCode(), is("terminal.notfound"));
        }
    }


    @Test
    public void getRouteDataRevision() {

        List<RouteDataRevision> routeDataRevisions = singletonList(new RouteDataRevision());
        when(routeDataRevisionRepositoryMock.findAll()).thenReturn(routeDataRevisions);

        List<RouteDataRevision> resultList = sut.getRouteDataRevisions();
        assertThat(resultList, is(routeDataRevisions));
    }


    @Test
    public void getRouteDataRevisionByTerminal() {

        List<RouteDataRevision> routeDataRevisions = singletonList(new RouteDataRevision());
        when(routeDataRevisionRepositoryMock.findByTerminalId(1L)).thenReturn(routeDataRevisions);

        List<RouteDataRevision> resultList = sut.getRouteDataRevisions(1L);
        assertThat(resultList, is(routeDataRevisions));
    }


    @Test
    public void getOne() {

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        when(routeDataRevisionRepositoryMock.findOne(5L)).thenReturn(routeDataRevision);

        RouteDataRevision result = sut.getRouteDataRevision(5L);
        assertThat(result, is(routeDataRevision));
    }


    @Test
    public void save() {

        RouteDataRevision routeDataRevision = new RouteDataRevision();

        when(routeDataRevisionRepositoryMock.save(routeDataRevision)).thenReturn(routeDataRevision);

        RouteDataRevision result = sut.save(routeDataRevision);

        assertThat(result, is(routeDataRevision));
        verify(routeDataRevisionRepositoryMock).save(routeDataRevision);
    }


    @Test
    public void existsEntryNewSurroundsExisting() {

        RouteDataRevision firstExistingRevision = new RouteDataRevision();
        firstExistingRevision.setValidFrom(new DateTime().minusDays(3).toDate());
        firstExistingRevision.setValidTo(new DateTime().minusDays(2).toDate());
        firstExistingRevision.setId(5L);

        RouteDataRevision secondRevision = new RouteDataRevision();
        secondRevision.setValidFrom(new DateTime().minusDays(1).toDate());
        secondRevision.setValidTo(new DateTime().plusDays(1).toDate());
        secondRevision.setId(7L);

        when(routeDataRevisionRepositoryMock.findByTerminalAndLatitudeAndLongitude(BigInteger.ONE, BigDecimal.TEN,
                    BigDecimal.ONE)).thenReturn(asList(firstExistingRevision, secondRevision));

        assertThat(sut.overlapsWithExisting(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE,
                new ValidityRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)), null), is(true));

        assertThat(sut.overlapsWithExisting(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE,
                new ValidityRange(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3)), null), is(false));

        assertThat(sut.overlapsWithExisting(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE,
                new ValidityRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)), 7L), is(false));
    }
}
