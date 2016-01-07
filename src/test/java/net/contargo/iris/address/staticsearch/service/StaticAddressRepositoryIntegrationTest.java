package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.persistence.StaticAddressRepository;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

import java.math.BigInteger;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * Test for {@link StaticAddressRepository}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
@Rollback
@Transactional
public class StaticAddressRepositoryIntegrationTest {

    private static final String ENTENHAUSEN = "Entenhausen";
    private static final String KUMMERSDORF = "Kummersdorf";
    private static final String HERZOGSHAIN = "Herzogshain";
    private static final String POSTAL_CODE_999999 = "999999";
    private static final String POSTAL_CODE_999998 = "999998";
    private static final String DE = "DE";
    private static final String FR = "FR";
    private static final BigInteger UNIQUE_ID_ONE = new BigInteger("1301000001000001");
    private static final BigInteger UNIQUE_ID_TWO = new BigInteger("1301000001000002");
    private static final BigInteger UNIQUE_ID_THREE = new BigInteger("1301000001000003");

    private StaticAddress entenhausenKummersdorf;
    private StaticAddress entenhausenHerzogshain;
    private StaticAddress entenhausenWithoutSuburb;

    @Autowired
    private StaticAddressRepository sut;

    @PersistenceContext
    EntityManager em;

    @Before
    public void setUp() {

        entenhausenKummersdorf = new StaticAddress();
        entenhausenKummersdorf.setCity(ENTENHAUSEN);
        entenhausenKummersdorf.setCityNormalized(ENTENHAUSEN.toUpperCase());
        entenhausenKummersdorf.setSuburb(KUMMERSDORF);
        entenhausenKummersdorf.setSuburbNormalized(KUMMERSDORF.toUpperCase());
        entenhausenKummersdorf.setPostalcode(POSTAL_CODE_999999);
        entenhausenKummersdorf.setLatitude(ONE);
        entenhausenKummersdorf.setLongitude(ONE);
        entenhausenKummersdorf.setUniqueId(UNIQUE_ID_ONE);
        entenhausenKummersdorf.setCountry(DE);

        entenhausenHerzogshain = new StaticAddress();
        entenhausenHerzogshain.setCity(ENTENHAUSEN);
        entenhausenHerzogshain.setCityNormalized(ENTENHAUSEN.toUpperCase());
        entenhausenHerzogshain.setSuburb(HERZOGSHAIN);
        entenhausenHerzogshain.setSuburbNormalized(HERZOGSHAIN.toUpperCase());
        entenhausenHerzogshain.setPostalcode(POSTAL_CODE_999999);
        entenhausenHerzogshain.setLatitude(TEN);
        entenhausenHerzogshain.setLongitude(TEN);
        entenhausenHerzogshain.setUniqueId(UNIQUE_ID_TWO);
        entenhausenHerzogshain.setCountry(DE);

        entenhausenWithoutSuburb = new StaticAddress();
        entenhausenWithoutSuburb.setCity(ENTENHAUSEN);
        entenhausenWithoutSuburb.setCityNormalized(ENTENHAUSEN.toUpperCase());
        entenhausenWithoutSuburb.setSuburb("");
        entenhausenWithoutSuburb.setSuburbNormalized("".toUpperCase());
        entenhausenWithoutSuburb.setPostalcode(POSTAL_CODE_999998);
        entenhausenWithoutSuburb.setLatitude(ONE);
        entenhausenWithoutSuburb.setLongitude(TEN);
        entenhausenWithoutSuburb.setUniqueId(UNIQUE_ID_THREE);
        entenhausenWithoutSuburb.setCountry(FR);
    }


    @Test
    public void findByPostalCodeAndCityWorks() {

        em.persist(entenhausenKummersdorf);
        em.persist(entenhausenHerzogshain);
        em.flush();

        List<StaticAddress> staticAddresses = sut.findByPostalCodeAndCity(POSTAL_CODE_999999, ENTENHAUSEN);

        assertThat(staticAddresses, hasSize(2));
        assertThat(staticAddresses.get(0).getSuburb(), is(HERZOGSHAIN));
        assertThat(staticAddresses.get(1).getSuburb(), is(KUMMERSDORF));
    }


    @Test
    public void findByPostalCodeOrCityWorks() {

        em.persist(entenhausenKummersdorf);
        em.persist(entenhausenWithoutSuburb);
        em.flush();

        List<StaticAddress> staticAddresses = sut.findByPostalCodeOrCity(POSTAL_CODE_999999, ENTENHAUSEN);

        assertThat(staticAddresses, hasSize(2));
        assertThat(staticAddresses.get(0).getSuburb(), is(""));
        assertThat(staticAddresses.get(1).getSuburb(), is(KUMMERSDORF));
    }


    @Test
    public void findByCountryAndPostalCodeAndCityWorks() {

        em.persist(entenhausenKummersdorf);
        em.persist(entenhausenWithoutSuburb);

        List<StaticAddress> staticAddresses = sut.findByCountryAndPostalCodeAndCity(POSTAL_CODE_999999, ENTENHAUSEN,
                DE);

        assertThat(staticAddresses, hasSize(1));
        assertThat(staticAddresses.get(0).getSuburb(), is(KUMMERSDORF));
    }


    @Test
    public void findByCountryAndPostalCodeAndCityWithEmptyCityWorks() {

        em.persist(entenhausenKummersdorf);
        em.persist(entenhausenWithoutSuburb);

        List<StaticAddress> staticAddresses = sut.findByCountryAndPostalCodeAndCity(POSTAL_CODE_999999, "", DE);

        assertThat(staticAddresses, hasSize(1));
        assertThat(staticAddresses.get(0).getSuburb(), is(KUMMERSDORF));
    }


    @Test
    public void findByCountryAndPostalCodeOrCityWorks() {

        em.persist(entenhausenKummersdorf);
        em.persist(entenhausenHerzogshain);
        em.persist(entenhausenWithoutSuburb);

        List<StaticAddress> staticAddresses = sut.findByCountryAndPostalCodeOrCity(POSTAL_CODE_999999, ENTENHAUSEN, DE);
        assertThat(staticAddresses, hasSize(2));
        assertThat(staticAddresses.get(0).getSuburb(), is(HERZOGSHAIN));
        assertThat(staticAddresses.get(1).getSuburb(), is(KUMMERSDORF));
    }


    @Test
    public void findByCountryAndPostalCodeOrCityWithoutSuburbWorks() {

        em.persist(entenhausenKummersdorf);
        em.persist(entenhausenHerzogshain);
        em.persist(entenhausenWithoutSuburb);

        List<StaticAddress> staticAddresses = sut.findByCountryAndPostalCodeOrCity("12345", ENTENHAUSEN, DE);
        assertThat(staticAddresses, hasSize(2));
        assertThat(staticAddresses.get(0).getSuburb(), is(HERZOGSHAIN));
        assertThat(staticAddresses.get(1).getSuburb(), is(KUMMERSDORF));
    }


    @Test
    public void findNoAddressByPostalCodeAndCountry() {

        List<StaticAddress> staticAddressList = sut.findByCountryAndPostalCodeOrCity("37678", "", "DE");
        assertThat(staticAddressList, emptyCollectionOf(StaticAddress.class));
    }


    @Test
    public void findByLatLonWorks() {

        em.persist(entenhausenKummersdorf);
        em.persist(entenhausenWithoutSuburb);

        StaticAddress staticAddress = sut.findByLatitudeAndLongitude(ONE, ONE);
        assertThat(staticAddress, is(entenhausenKummersdorf));
    }


    @Test
    public void findByCityAndSuburbAndPostalCode() {

        em.persist(entenhausenWithoutSuburb);
        em.persist(entenhausenKummersdorf);

        List<StaticAddress> staticAddressList = sut.findByCityAndSuburbAndPostalcode(ENTENHAUSEN, KUMMERSDORF,
                POSTAL_CODE_999999);
        assertThat(staticAddressList, hasSize(1));
        assertThat(staticAddressList.get(0), is(entenhausenKummersdorf));
    }


    @Test
    public void findByPostalCode() {

        em.persist(entenhausenHerzogshain);
        em.persist(entenhausenWithoutSuburb);

        List<StaticAddress> staticAddressList = sut.findByPostalcode(POSTAL_CODE_999999);

        assertThat(staticAddressList, hasSize(1));
        assertThat(staticAddressList.get(0).getPostalcode(), is(POSTAL_CODE_999999));
    }


    @Test
    public void findNoAddressByPostalCode() {

        List<StaticAddress> staticAddressList = sut.findByPostalcode("55443");

        assertThat(staticAddressList, emptyCollectionOf(StaticAddress.class));
    }


    @Test
    public void findMissingHashKeys() throws NoSuchFieldException, IllegalAccessException {

        String city = "City";
        String suburb = "suburb";
        String postalcode = "76131";

        StaticAddress staticAddressWithoutHashkey = new StaticAddress();
        staticAddressWithoutHashkey.setCity(city);
        staticAddressWithoutHashkey.setCityNormalized(city.toUpperCase());
        staticAddressWithoutHashkey.setSuburb(suburb);
        staticAddressWithoutHashkey.setSuburbNormalized(suburb.toUpperCase());
        staticAddressWithoutHashkey.setPostalcode(postalcode);
        staticAddressWithoutHashkey.setLongitude(ONE);
        staticAddressWithoutHashkey.setLatitude(ONE);
        staticAddressWithoutHashkey.setUniqueId(UNIQUE_ID_ONE);

        // using reflection to set hashkey to null
        Class<?> c = staticAddressWithoutHashkey.getClass();

        Field hashKeyField = c.getDeclaredField("hashKey");
        hashKeyField.setAccessible(true);
        hashKeyField.set(staticAddressWithoutHashkey, null);

        StaticAddress staticAddressWithHashKey = new StaticAddress();
        staticAddressWithHashKey.setCity(city);
        staticAddressWithHashKey.setCityNormalized(city.toUpperCase());
        staticAddressWithHashKey.setSuburb(suburb);
        staticAddressWithHashKey.setSuburbNormalized(suburb.toUpperCase());
        staticAddressWithHashKey.setPostalcode(postalcode);
        staticAddressWithHashKey.setLongitude(TEN);
        staticAddressWithHashKey.setLatitude(TEN);
        staticAddressWithHashKey.setUniqueId(UNIQUE_ID_TWO);

        em.persist(staticAddressWithoutHashkey);
        em.persist(staticAddressWithHashKey);

        Pageable pageable = new PageRequest(0, 50);

        Page<StaticAddress> actualResult = sut.findMissingHashKeys(pageable);

        assertThat(actualResult.getContent(), contains(staticAddressWithoutHashkey));
        assertThat(actualResult.getContent(), not(contains(staticAddressWithHashKey)));
    }


    @Test
    public void findByBoundingBox() {

        em.persist(entenhausenKummersdorf);
        em.persist(entenhausenHerzogshain);

        List<StaticAddress> staticAddresses = sut.findByBoundingBox(ZERO, TEN, ZERO, ONE);

        assertThat(staticAddresses, hasSize(1));
        assertThat(staticAddresses.get(0).getSuburb(), is(KUMMERSDORF));
    }
}
