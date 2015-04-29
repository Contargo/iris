package net.contargo.iris.routedatarevision.persistence;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;


/**
 * Integration test of {@link net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository}.
 *
 * <p>findNearest(): test if the nearest {@link net.contargo.iris.routedatarevision.RouteDataRevision} to the given
 * GeoLocation will be returned</p>
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class RouteDataRevisionRepositoryIntegrationTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RouteDataRevisionRepository sut;

    private Terminal terminal;
    private RouteDataRevision routeDataRevision;

    @Before
    public void setUp() {

        terminal = createTerminal("terminal", BigInteger.ONE, TEN, TEN);
        routeDataRevision = createRouteDataRevision(terminal, ZERO, ZERO, ZERO, valueOf(49.1001), valueOf(8.9101), TEN);
    }


    @Test
    public void findNearest() {

        em.persist(terminal);

        em.persist(routeDataRevision);
        em.persist(createRouteDataRevision(terminal, ONE, ONE, ONE, valueOf(49.1011), valueOf(8.9102), TEN));
        em.persist(createRouteDataRevision(terminal, TEN, TEN, TEN, valueOf(49.1021), valueOf(8.9103), TEN));

        em.flush();

        RouteDataRevision nearestRouteDataRevision = sut.findNearest(terminal, valueOf(49.10), valueOf(8.91));

        assertThat(nearestRouteDataRevision.getAirlineDistance(), is(ZERO));
        assertThat(nearestRouteDataRevision.getTollDistanceOneWay(), is(ZERO));
        assertThat(nearestRouteDataRevision.getTruckDistanceOneWay(), is(ZERO));
        assertThat(nearestRouteDataRevision.getLatitude(), is(valueOf(49.1001)));
        assertThat(nearestRouteDataRevision.getLongitude(), is(valueOf(8.9101)));
    }


    @Test
    public void findByTerminalAndLatitudeAndLongitude() {

        em.persist(terminal);
        em.persist(routeDataRevision);

        em.flush();

        Optional<RouteDataRevision> routeDataRevisionOptional = sut.findByTerminalAndLatitudeAndLongitude(terminal,
                valueOf(49.1001), valueOf(8.9101));
        assertThat(routeDataRevisionOptional.isPresent(), is(true));
    }


    @Test
    public void findByTerminalAndLatitudeAndLongitudeNotExisting() {

        em.persist(terminal);
        em.persist(routeDataRevision);

        em.flush();

        Optional<RouteDataRevision> routeDataRevisionOptional = sut.findByTerminalAndLatitudeAndLongitude(terminal,
                valueOf(49.1001), valueOf(8.9102));
        assertThat(routeDataRevisionOptional.isPresent(), is(false));
    }


    private Terminal createTerminal(String name, BigInteger id, BigDecimal latitude, BigDecimal longitude) {

        Terminal terminal = new Terminal(new GeoLocation(latitude, longitude));
        terminal.setName(name);
        terminal.setUniqueId(id);

        return terminal;
    }


    private RouteDataRevision createRouteDataRevision(Terminal t, BigDecimal tdow, BigDecimal truckdow, BigDecimal ad,
        BigDecimal lat, BigDecimal lng, BigDecimal r) {

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setTollDistanceOneWay(tdow);
        routeDataRevision.setTruckDistanceOneWay(truckdow);
        routeDataRevision.setAirlineDistance(ad);
        routeDataRevision.setLatitude(lat);
        routeDataRevision.setLongitude(lng);
        routeDataRevision.setRadius(r);
        routeDataRevision.setTerminal(t);

        return routeDataRevision;
    }
}
