package net.contargo.iris.connection.persistence;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static net.contargo.iris.route.RouteType.BARGE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class MainRunConnectionRepositoryIntegrationTest {

    private static final GeoLocation GEO_TERMINAL_1 = new GeoLocation(new BigDecimal("49.2"), new BigDecimal("8.6"));
    private static final GeoLocation GEO_TERMINAL_2 = new GeoLocation(new BigDecimal("50.2"), new BigDecimal("9.6"));
    private static final GeoLocation GEO_SEAPORT = new GeoLocation(new BigDecimal("48.23"), new BigDecimal("11.75"));

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MainRunConnectionRepository sut;

    @Test
    public void findConnectionsByTerminalUniqueId() {

        BigInteger unconnectedTerminalUID = new BigInteger("2");
        Terminal unconnectedTerminal = new Terminal(GEO_TERMINAL_1);
        unconnectedTerminal.setName("unconnected terminal");
        unconnectedTerminal.setUniqueId(unconnectedTerminalUID);

        BigInteger connectedTerminalUID = new BigInteger("3");
        Terminal connectedTerminal = new Terminal(GEO_TERMINAL_2);
        connectedTerminal.setName("connected terminal");
        connectedTerminal.setUniqueId(connectedTerminalUID);

        BigInteger connectedSeaportUID = new BigInteger("4");
        Seaport connectedSeaport = new Seaport(GEO_SEAPORT);
        connectedSeaport.setName("connected seaport");
        connectedSeaport.setUniqueId(connectedSeaportUID);

        MainRunConnection connection = newConnection(connectedSeaport, connectedTerminal);

        // transition to managed state
        em.persist(unconnectedTerminal);
        em.persist(connectedTerminal);
        em.persist(connectedSeaport);
        em.persist(connection);

        em.flush(); // syncs in-memory model with database

        List<MainRunConnection> connections = sut.findConnectionsByTerminalUniqueId(connectedTerminalUID);
        List<MainRunConnection> expectedConnections = em.createQuery(
                    "SELECT c FROM MainRunConnection c WHERE c.terminal.uniqueId = " + connectedTerminalUID,
                    MainRunConnection.class)
            .getResultList();

        assertThat(connections, is(expectedConnections));
    }


    private MainRunConnection newConnection(Seaport seaport, Terminal terminal) {

        MainRunConnection connection = new MainRunConnection();
        connection.setSeaport(seaport);
        connection.setTerminal(terminal);
        connection.setRouteType(BARGE);
        connection.setRailElectricDistance(new BigDecimal("300"));
        connection.setRailDieselDistance(new BigDecimal("200"));
        connection.setBargeDieselDistance(new BigDecimal("400"));

        return connection;
    }
}
