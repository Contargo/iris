package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


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
    private Terminal terminal;

    @Before
    public void before() {

        sut = new RouteDataRevisionServiceImpl(routeDataRevisionRepositoryMock);
        terminal = new Terminal();
    }


    @Test
    public void getRouteDataRevisionByTerminalAndAddress() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);
        RouteDataRevision routeDataRevisionDB = new RouteDataRevision();

        when(routeDataRevisionRepositoryMock.findNearest(terminal, address.getLatitude(), address.getLongitude()))
            .thenReturn(routeDataRevisionDB);

        RouteDataRevision routeDataRevision = sut.getRouteDataRevision(terminal, address);

        assertThat(routeDataRevision, is(routeDataRevisionDB));
    }


    @Test
    public void getRouteDataRevision() {

        List<RouteDataRevision> routeDataRevisions = asList(new RouteDataRevision());
        when(routeDataRevisionRepositoryMock.findAll()).thenReturn(routeDataRevisions);

        List<RouteDataRevision> resultList = sut.getRouteDataRevisions();
        assertThat(resultList, is(routeDataRevisions));
    }


    @Test
    public void getRouteDataRevisionByTerminal() {

        List<RouteDataRevision> routeDataRevisions = asList(new RouteDataRevision());
        when(routeDataRevisionRepositoryMock.findByTerminal(terminal)).thenReturn(routeDataRevisions);

        List<RouteDataRevision> resultList = sut.getRouteDataRevisions(terminal);
        assertThat(resultList, is(routeDataRevisions));
    }
}
