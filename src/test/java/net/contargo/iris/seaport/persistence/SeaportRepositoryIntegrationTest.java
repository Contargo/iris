package net.contargo.iris.seaport.persistence;

import net.contargo.iris.seaport.Seaport;

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
public class SeaportRepositoryIntegrationTest {

    @Autowired
    SeaportRepository sut;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testFindByEnabled() throws Exception {

        Seaport seaport1 = createSeaport();
        Seaport seaport2 = createSeaport();
        seaport1.setUniqueId(BigInteger.ONE);
        seaport2.setUniqueId(BigInteger.TEN);
        em.persist(seaport1);
        em.persist(seaport2);
        em.flush();

        assertThat(sut.findByEnabled(true).contains(seaport1), is(true));
        assertThat(sut.findByEnabled(true).contains(seaport2), is(true));
    }


    private Seaport createSeaport() {

        Seaport seaport = new Seaport();
        seaport.setName("Carlo Pedersoli" + Math.random());
        seaport.setEnabled(true);
        seaport.setLatitude(new BigDecimal(Math.random()));
        seaport.setLongitude(new BigDecimal(Math.random()));
        seaport.setUniqueId(BigInteger.ONE);

        return seaport;
    }
}
