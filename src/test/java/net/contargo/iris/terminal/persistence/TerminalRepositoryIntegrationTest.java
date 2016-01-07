package net.contargo.iris.terminal.persistence;

import net.contargo.iris.terminal.Terminal;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
@Rollback
@Transactional
public class TerminalRepositoryIntegrationTest {

    @Autowired
    TerminalRepository sut;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testFindByEnabled() {

        Terminal terminal1 = createTerminal();
        terminal1.setUniqueId(BigInteger.ONE);

        Terminal terminal2 = createTerminal();
        terminal2.setUniqueId(BigInteger.TEN);
        em.persist(terminal1);
        em.persist(terminal2);
        em.flush();

        assertThat(sut.findByEnabled(true).contains(terminal1), is(true));
        assertThat(sut.findByEnabled(true).contains(terminal2), is(true));
    }


    private Terminal createTerminal() {

        Terminal terminal1 = new Terminal();
        terminal1.setName("Carlo Pedersoli" + Math.random());
        terminal1.setEnabled(true);
        terminal1.setLatitude(new BigDecimal(Math.random()));
        terminal1.setLongitude(new BigDecimal(Math.random()));
        terminal1.setUniqueId(BigInteger.ONE);

        return terminal1;
    }
}
