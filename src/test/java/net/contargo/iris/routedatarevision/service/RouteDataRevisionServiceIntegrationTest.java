package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.RouteRevisionRequest;
import net.contargo.iris.terminal.Terminal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.time.ZoneId;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@ContextConfiguration(locations = "classpath*:application-context.xml")
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
class RouteDataRevisionServiceIntegrationTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RouteDataRevisionService sut;

    private Terminal terminal;
    private RouteDataRevision expired;
    private RouteDataRevision notYetExpired;
    private RouteDataRevision neverExpired;

    @BeforeEach
    void setup() {

        terminal = new Terminal(new GeoLocation(new BigDecimal("49.0"), new BigDecimal("8.6")));
        terminal.setUniqueId(new BigInteger("6532"));
        terminal.setName("the terminal");

        Date firstDayIn2018 = Date.from(of(2018, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date yesterday = Date.from(now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date today = Date.from(now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        expired = new RouteDataRevision();
        expired.setTerminal(terminal);
        expired.setValidFrom(firstDayIn2018);
        expired.setValidTo(yesterday);
        expired.setLatitude(new BigDecimal("49.57"));
        expired.setLongitude(new BigDecimal("8.67"));
        expired.setRadiusInMeter(new BigDecimal("10.0"));
        expired.setTruckDistanceOneWayInKilometer(new BigDecimal("220.0"));
        expired.setTollDistanceOneWayInKilometer(new BigDecimal("175.0"));
        expired.setAirlineDistanceInKilometer(new BigDecimal("200.0"));
        expired.getTruckDistanceOnWayInKilometerCountry().put("DE", new BigDecimal("220.0"));

        notYetExpired = new RouteDataRevision();
        notYetExpired.setTerminal(terminal);
        notYetExpired.setValidFrom(firstDayIn2018);
        notYetExpired.setValidTo(today);
        notYetExpired.setLatitude(new BigDecimal("49.55"));
        notYetExpired.setLongitude(new BigDecimal("8.65"));
        notYetExpired.setRadiusInMeter(new BigDecimal("10.0"));
        notYetExpired.setTruckDistanceOneWayInKilometer(new BigDecimal("120.0"));
        notYetExpired.setTollDistanceOneWayInKilometer(new BigDecimal("75.0"));
        notYetExpired.setAirlineDistanceInKilometer(new BigDecimal("100.0"));
        notYetExpired.getTruckDistanceOnWayInKilometerCountry().put("DE", new BigDecimal("120.0"));

        neverExpired = new RouteDataRevision();
        neverExpired.setTerminal(terminal);
        neverExpired.setValidFrom(firstDayIn2018);
        neverExpired.setValidTo(null);
        neverExpired.setLatitude(new BigDecimal("49.53"));
        neverExpired.setLongitude(new BigDecimal("8.63"));
        neverExpired.setRadiusInMeter(new BigDecimal("10.0"));
        neverExpired.setTruckDistanceOneWayInKilometer(new BigDecimal("20.0"));
        neverExpired.setTollDistanceOneWayInKilometer(new BigDecimal("7.5"));
        neverExpired.setAirlineDistanceInKilometer(new BigDecimal("10.0"));
        neverExpired.getTruckDistanceOnWayInKilometerCountry().put("DE", new BigDecimal("20.0"));

        em.persist(terminal);
        em.persist(notYetExpired);
        em.persist(neverExpired);
        em.persist(expired);
    }


    @Test
    void searchWithExpirationFilter() {

        RouteRevisionRequest request = new RouteRevisionRequest();
        request.setTerminalId(terminal.getId());

        // search for all
        assertThat(sut.search(request), hasSize(3));

        // search for expired
        request.setFilterName(RouteRevisionRequest.ExpirationFilter.EXPIRED.getFilterName());
        assertThat(sut.search(request), containsInAnyOrder(expired));

        // search for not expired
        request.setFilterName(RouteRevisionRequest.ExpirationFilter.NOT_EXPIRED.getFilterName());
        assertThat(sut.search(request), containsInAnyOrder(notYetExpired, neverExpired));
    }
}
