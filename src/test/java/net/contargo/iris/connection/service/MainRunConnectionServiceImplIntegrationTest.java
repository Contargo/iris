package net.contargo.iris.connection.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
@Rollback
@Transactional
public class MainRunConnectionServiceImplIntegrationTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MainRunConnectionService sut;

    private Seaport seaport;
    private Terminal terminal;

    @Before
    public void setUp() {

        em.createQuery("DELETE FROM MainRunConnection c").executeUpdate();
        em.createQuery("DELETE FROM RouteDataRevision r").executeUpdate();
        em.createQuery("DELETE FROM Terminal t").executeUpdate();
        em.createQuery("DELETE FROM Seaport s").executeUpdate();

        seaport = new Seaport(new GeoLocation(ONE, TEN));
        seaport.setName("Groß-Schonach");
        seaport.setUniqueId(BigInteger.ONE);
        em.persist(seaport);

        terminal = new Terminal(new GeoLocation(TEN, ONE));
        terminal.setName("Hornberg");
        terminal.setUniqueId(BigInteger.TEN);
        em.persist(terminal);
    }


    @Test
    public void save() {

        MainRunConnection connection = new MainRunConnection(seaport);
        connection.setTerminal(terminal);
        connection.setRouteType(RouteType.BARGE);
        connection.setBargeDieselDistance(ZERO);
        connection.setRailDieselDistance(ZERO);
        connection.setRailElectricDistance(ZERO);
        connection.setRoadDistance(ZERO);

        sut.save(connection);
    }
}
