package net.contargo.iris.seaport.service;

import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.persistence.SeaportRepository;
import net.contargo.iris.sequence.UniqueIdSequence;
import net.contargo.iris.sequence.service.SequenceService;

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
public class SeaportServiceImplIntegrationTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SeaportRepository seaportRepository;
    @Autowired
    private SequenceService uniqueIdSequenceService;

    private SeaportServiceImpl sut;

    private Seaport seaport;

    @Before
    public void setUp() {

        sut = new SeaportServiceImpl(seaportRepository, uniqueIdSequenceService);
        seaport = new Seaport();
        seaport.setName("SeaportName");
        seaport.setLatitude(BigDecimal.ONE);
        seaport.setLongitude(BigDecimal.ONE);
    }


    @Test
    public void testSaveWithoutUniqueId() {

        UniqueIdSequence nextSequenceObject = getNextSequenceObjectFromDb();

        BigInteger nextSequence = nextSequenceObject.getNextId();
        Seaport savedSeaport = sut.save(seaport);
        assertThat(savedSeaport.getUniqueId(), is(nextSequence));

        assertThat(getNextSequenceObjectFromDb().getNextId(), is(nextSequence.add(BigInteger.ONE)));
    }


    private UniqueIdSequence getNextSequenceObjectFromDb() {

        String queryString = "SELECT o FROM UniqueIdSequence o WHERE o.entityName = 'Seaport'";

        return em.createQuery(queryString, UniqueIdSequence.class).getSingleResult();
    }
}
