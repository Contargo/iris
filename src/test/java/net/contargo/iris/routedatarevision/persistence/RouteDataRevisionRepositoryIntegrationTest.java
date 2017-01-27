package net.contargo.iris.routedatarevision.persistence;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;

import org.joda.time.DateTime;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static net.contargo.iris.routedatarevision.service.RouteRevisionSpecifications.hasCity;
import static net.contargo.iris.routedatarevision.service.RouteRevisionSpecifications.hasPostalCode;
import static net.contargo.iris.routedatarevision.service.RouteRevisionSpecifications.hasTerminal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.hamcrest.core.Is.is;

import static org.springframework.data.jpa.domain.Specifications.where;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

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
@Rollback
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

        RouteDataRevision nearestRouteDataRevision = sut.findNearest(terminal, valueOf(49.1001), valueOf(8.9102),
                new Date());

        assertThat(nearestRouteDataRevision.getAirlineDistanceInKilometer(), is(ZERO));
        assertThat(nearestRouteDataRevision.getTollDistanceOneWayInKilometer(), is(ZERO));
        assertThat(nearestRouteDataRevision.getTruckDistanceOneWayInKilometer(), is(ZERO));
        assertThat(nearestRouteDataRevision.getLatitude(), is(valueOf(49.1001)));
        assertThat(nearestRouteDataRevision.getLongitude(), is(valueOf(8.9102)));
        assertThat(nearestRouteDataRevision.getComment(), equalTo("comment1"));
    }


    @Test
    public void findNearestBeforeValidFrom() {

        em.persist(terminal);

        em.persist(routeDataRevision);
        em.persist(createRouteDataRevision(terminal, ZERO, ZERO, ZERO, valueOf(49.1001), valueOf(8.9102), TEN,
                "comment1", DateTime.now().plusDays(1).toDate(), DateTime.now().plusDays(3).toDate()));

        em.flush();

        RouteDataRevision nearestRouteDataRevision = sut.findNearest(terminal, valueOf(49.1001), valueOf(8.9102),
                new Date());

        assertThat(nearestRouteDataRevision, nullValue());
    }


    @Test
    public void findNearestAfterValidTo() {

        em.persist(terminal);

        em.persist(routeDataRevision);
        em.persist(createRouteDataRevision(terminal, ZERO, ZERO, ZERO, valueOf(49.1001), valueOf(8.9102), TEN,
                "comment1", DateTime.now().minusDays(2).toDate(), DateTime.now().minusDays(1).toDate()));

        em.flush();

        RouteDataRevision nearestRouteDataRevision = sut.findNearest(terminal, valueOf(49.1001), valueOf(8.9102),
                new Date());

        assertThat(nearestRouteDataRevision, nullValue());
    }


    @Test
    public void findByTerminalAndLatitudeAndLongitude() {

        em.persist(terminal);
        em.persist(routeDataRevision);

        em.flush();

        List<RouteDataRevision> routeDataRevisionOptional = sut.findByTerminalAndLatitudeAndLongitude(BigInteger.TEN,
                valueOf(49.1011), valueOf(8.9101));
        assertThat(routeDataRevisionOptional, hasSize(1));
    }


    @Test
    public void findByTerminalAndLatitudeAndLongitudeNotExisting() {

        em.persist(terminal);
        em.persist(routeDataRevision);

        em.flush();

        List<RouteDataRevision> routeDataRevisionOptional = sut.findByTerminalAndLatitudeAndLongitude(BigInteger.TEN,
                valueOf(49.1001), valueOf(8.9102));
        assertThat(routeDataRevisionOptional, hasSize(0));
    }


    @Test
    public void specifications() {

        Terminal gomaringen = new Terminal(new GeoLocation(ONE, ONE));
        gomaringen.setName("Gomaringen");
        gomaringen.setUniqueId(BigInteger.TEN);

        Terminal eisleben = new Terminal(new GeoLocation(ONE, TEN));
        eisleben.setName("Eisleben1");
        eisleben.setUniqueId(BigInteger.ZERO);

        RouteDataRevision revision1 = createRouteDataRevision(gomaringen, ZERO, ZERO, ZERO, ONE, ONE, TEN, "",
                new Date(), null);
        revision1.setCity("Stuttgart");
        revision1.setCityNormalized("STUTTGART");
        revision1.setPostalCode("70173");
        revision1.setCountry("de");

        RouteDataRevision revision2 = createRouteDataRevision(eisleben, ZERO, ZERO, ZERO, ONE, ONE, TEN, "", new Date(),
                null);
        revision2.setCity("Halle");
        revision2.setCityNormalized("HALLEEE");
        revision2.setPostalCode("70173");
        revision2.setCountry("de");

        em.persist(gomaringen);
        em.persist(eisleben);
        em.persist(revision1);
        em.persist(revision2);
        em.flush();

        Long gomaringenId = gomaringen.getId();
        List<RouteDataRevision> results = sut.findAll(where(hasTerminal(gomaringenId)).and(hasPostalCode("70173")));

        assertThat(results, hasSize(1));
        assertReflectionEquals(revision1, results.get(0));

        results = sut.findAll(where(hasPostalCode("70173")));
        assertThat(results, hasSize(2));
        assertReflectionEquals(revision1, results.get(0));
        assertReflectionEquals(revision2, results.get(1));

        results = sut.findAll(where(hasCity("ALLEEE")));
        assertThat(results, hasSize(1));
        assertReflectionEquals(revision2, results.get(0));
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
        routeDataRevision.setTollDistanceOneWayInKilometer(tdow);
        routeDataRevision.setTruckDistanceOneWayInKilometer(truckdow);
        routeDataRevision.setAirlineDistanceInKilometer(ad);
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
