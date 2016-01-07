package net.contargo.iris.sequence.persistence;

import net.contargo.iris.sequence.UniqueIdSequence;

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

import static net.contargo.iris.sequence.UniqueIdSequence.SYSTEM_UNIQUEID_PREFIX;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.greaterThan;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
@Rollback
@Transactional
public class UniqueIdUniqueIdSequenceRepositoryIntegrationTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private UniqueIdSequenceRepository sut;

    UniqueIdSequence uniqueIdSequence;

    @Before
    public void setUp() {

        uniqueIdSequence = new UniqueIdSequence("Terminal", BigInteger.TEN);
    }


    @Test
    public void testFindByTableName() {

        UniqueIdSequence result = sut.findByEntityName("Terminal");
        assertThat(result.getNextId(), greaterThan(new BigInteger(SYSTEM_UNIQUEID_PREFIX + "000000000000")));
    }
}
