package net.contargo.iris.terminal.service;

import net.contargo.iris.sequence.UniqueIdSequence;
import net.contargo.iris.sequence.service.SequenceService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.persistence.TerminalRepository;

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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
@Rollback
@Transactional
public class TerminalServiceImplIntegrationTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TerminalRepository terminalRepository;
    @Autowired
    private SequenceService uniqueIdSequenceService;

    private TerminalServiceImpl sut;

    private Terminal terminal;

    @Before
    public void setUp() {

        sut = new TerminalServiceImpl(terminalRepository, uniqueIdSequenceService);
        terminal = new Terminal();
        terminal.setName("TerminalName");
        terminal.setLatitude(BigDecimal.ONE);
        terminal.setLongitude(BigDecimal.ONE);
    }


    @Test
    public void testSaveWithoutUniqueId() {

        UniqueIdSequence nextSequenceObject = getNextSequenceObjectFromDb();

        BigInteger nextSequence = nextSequenceObject.getNextId();
        Terminal savedTerminal = sut.save(terminal);
        assertThat(savedTerminal.getUniqueId(), is(nextSequence));

        assertThat(getNextSequenceObjectFromDb().getNextId(), is(nextSequence.add(BigInteger.ONE)));
    }


    private UniqueIdSequence getNextSequenceObjectFromDb() {

        String queryString = "SELECT o FROM UniqueIdSequence o WHERE o.entityName = 'Terminal'";

        return em.createQuery(queryString, UniqueIdSequence.class).getSingleResult();
    }
}
