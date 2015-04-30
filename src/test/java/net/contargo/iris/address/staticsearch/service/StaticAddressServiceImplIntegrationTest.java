package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.persistence.StaticAddressRepository;
import net.contargo.iris.normalizer.NormalizerServiceImpl;
import net.contargo.iris.sequence.UniqueIdSequence;
import net.contargo.iris.sequence.service.UniqueIdSequenceServiceImpl;

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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class StaticAddressServiceImplIntegrationTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private StaticAddressRepository staticAddressRepository;
    @Autowired
    private UniqueIdSequenceServiceImpl uniqueIdSequenceService;
    @Autowired
    private NormalizerServiceImpl normalizerService;

    private StaticAddressServiceImpl sut;

    private StaticAddress staticAddress;

    @Before
    public void setUp() {

        sut = new StaticAddressServiceImpl(staticAddressRepository, uniqueIdSequenceService, normalizerService);
        staticAddress = new StaticAddress();
        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(BigDecimal.ONE);
        staticAddress.setCity("City");
        staticAddress.setCityNormalized("CITY");
        staticAddress.setPostalcode("12345");
        staticAddress.setCountry("DE");

        em.createQuery("DELETE FROM StaticAddress s").executeUpdate();
    }


    @Test
    public void testSaveWithoutUniqueId() {

        UniqueIdSequence nextSequenceObject = getNextSequenceObjectFromDb();

        BigInteger nextSequence = nextSequenceObject.getNextId();
        StaticAddress savedStaticAddress = sut.saveStaticAddress(staticAddress);
        assertThat(savedStaticAddress.getUniqueId(), is(nextSequence));

        assertThat(getNextSequenceObjectFromDb().getNextId(), is(nextSequence.add(BigInteger.ONE)));
    }


    private UniqueIdSequence getNextSequenceObjectFromDb() {

        String queryString = "SELECT o FROM UniqueIdSequence o WHERE o.entityName = 'StaticAddress'";

        return em.createQuery(queryString, UniqueIdSequence.class).getSingleResult();
    }
}
