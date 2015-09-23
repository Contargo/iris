package net.contargo.iris.routedatarevision.persistence;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import org.joda.time.DateTime;

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

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.equalTo;

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
        terminal.setUniqueId(BigInteger.TEN);
        routeDataRevision = createRouteDataRevision(terminal, ONE, ONE, ONE, valueOf(49.1011), valueOf(8.9101), TEN,
                "comment0", DateTime.now().toDate(), DateTime.now().plusDays(1).toDate());
    }


    @Test
    public void findNearest() {

        em.persist(terminal);

        em.persist(routeDataRevision);
        em.persist(createRouteDataRevision(terminal, ZERO, ZERO, ZERO, valueOf(49.1001), valueOf(8.9102), TEN,
                "comment1", DateTime.now().toDate(), DateTime.now().plusDays(1).toDate()));
        em.persist(createRouteDataRevision(terminal, TEN, TEN, TEN, valueOf(49.1021), valueOf(8.9103), TEN, "comment2",
                DateTime.now().toDate(), DateTime.now().plusDays(1).toDate()));

        em.flush();

        RouteDataRevision nearestRouteDataRevision = sut.findNearest(terminal, valueOf(49.1001), valueOf(8.9102));

        assertThat(nearestRouteDataRevision.getAirlineDistanceInMeter(), is(ZERO));
        assertThat(nearestRouteDataRevision.getTollDistanceOneWayInMeter(), is(ZERO));
        assertThat(nearestRouteDataRevision.getTruckDistanceOneWayInMeter(), is(ZERO));
        assertThat(nearestRouteDataRevision.getLatitude(), is(valueOf(49.1001)));
        assertThat(nearestRouteDataRevision.getLongitude(), is(valueOf(8.9102)));
        assertThat(nearestRouteDataRevision.getComment(), equalTo("comment1"));
    }


    @Test
    public void findByTerminalAndLatitudeAndLongitude() {

        em.persist(terminal);
        em.persist(routeDataRevision);

        em.flush();

        Optional<RouteDataRevision> routeDataRevisionOptional = sut.findByTerminalAndLatitudeAndLongitude(
                BigInteger.TEN, valueOf(49.1011), valueOf(8.9101));
        assertThat(routeDataRevisionOptional.isPresent(), is(true));
    }


    @Test
    public void findByTerminalAndLatitudeAndLongitudeNotExisting() {

        em.persist(terminal);
        em.persist(routeDataRevision);

        em.flush();

        Optional<RouteDataRevision> routeDataRevisionOptional = sut.findByTerminalAndLatitudeAndLongitude(
                BigInteger.TEN, valueOf(49.1001), valueOf(8.9102));
        assertThat(routeDataRevisionOptional.isPresent(), is(false));
    }


    private Terminal createTerminal(String name, BigInteger id, BigDecimal latitude, BigDecimal longitude) {

        Terminal terminal = new Terminal(new GeoLocation(latitude, longitude));
        terminal.setName(name);
        terminal.setUniqueId(id);

        return terminal;
    }


    private RouteDataRevision createRouteDataRevision(Terminal t, BigDecimal tdow, BigDecimal truckdow, BigDecimal ad,
        BigDecimal lat, BigDecimal lng, BigDecimal r, String comment, Date validFrom, Date validTo) {

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setTollDistanceOneWayInMeter(tdow);
        routeDataRevision.setTruckDistanceOneWayInMeter(truckdow);
        routeDataRevision.setAirlineDistanceInMeter(ad);
        routeDataRevision.setLatitude(lat);
        routeDataRevision.setLongitude(lng);
        routeDataRevision.setRadiusInMeter(r);
        routeDataRevision.setTerminal(t);
        routeDataRevision.setComment(comment);
        routeDataRevision.setValidFrom(validFrom);
        routeDataRevision.setValidTo(validTo);

        return routeDataRevision;
    }
}
