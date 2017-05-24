package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.persistence.StaticAddressRepository;
import net.contargo.iris.normalizer.NormalizerServiceImpl;
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

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static java.math.BigDecimal.ONE;


/**
 * Unit test of {@link StaticAddressService Impl}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
@Rollback
@Transactional
public class StaticAddressServiceImplIntegrationTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private StaticAddressRepository staticAddressRepository;
    @Autowired
    private SequenceService uniqueIdSequenceService;
    @Autowired
    private NormalizerServiceImpl normalizerService;

    private StaticAddressServiceImpl sut;

    private StaticAddress staticAddress;

    @Before
    public void setUp() {

        sut = new StaticAddressServiceImpl(staticAddressRepository, uniqueIdSequenceService, normalizerService);

        staticAddress = new StaticAddress();
        staticAddress.setLatitude(ONE);
        staticAddress.setLongitude(ONE);
        staticAddress.setCity("Entenhausen");
        staticAddress.setCityNormalized("ENTENHAUSEN");
        staticAddress.setPostalcode("999999");
        staticAddress.setCountry("DE");
    }


    @Test
    public void saveWithoutUniqueId() {

        UniqueIdSequence nextSequenceObject = getNextSequenceObjectFromDb();
        BigInteger nextSequence = nextSequenceObject.getNextId();

        StaticAddress savedStaticAddress = sut.saveStaticAddress(staticAddress);
        assertThat(savedStaticAddress.getUniqueId(), is(nextSequence));
        assertThat(getNextSequenceObjectFromDb().getNextId(), is(nextSequence.add(BigInteger.ONE)));
    }


    private UniqueIdSequence getNextSequenceObjectFromDb() {

        return em.createQuery("SELECT o FROM UniqueIdSequence o WHERE o.entityName = 'StaticAddress'",
                UniqueIdSequence.class)
            .getSingleResult();
    }
}
