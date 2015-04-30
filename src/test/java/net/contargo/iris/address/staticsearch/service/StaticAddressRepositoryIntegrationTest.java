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

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

import java.math.BigDecimal;
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
import static org.hamcrest.Matchers.notNullValue;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;


/**
 * Test for {@link StaticAddressRepository}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class StaticAddressRepositoryIntegrationTest {

    private static final String COUNTRY_DE = "DE";
    private static final String CITY_ENTENHAUSEN = "Entenhausen";
    private static final String POSTALCODE_12345 = "12345";
    private static final String SUBURB_INNENSTADT = "Innenstadt";
    private static final String SUBURB_DAXLANDEN = "Daxlanden";
    private static final String KARLSRUHE = "Karlsruhe";
    private static final String MANNHEIM = "Mannheim";
    private static final String POSTAL_CODE = "76137";
    private static final BigInteger UNIQUE_ID_ONE = new BigInteger("1301000000000001");
    private static final BigInteger UNIQUE_ID_TWO = new BigInteger("1301000000000002");
    private static final BigInteger UNIQUE_ID_THREE = new BigInteger("1301000000000003");
    private static final BigInteger UNIQUE_ID_FOUR = new BigInteger("1301000000000004");
    private static final BigInteger UNIQUE_ID_FIVE = new BigInteger("1301000000000005");
    private static final BigInteger UNIQUE_ID_TEN = new BigInteger("1301000000000010");

    @Autowired
    private StaticAddressRepository sut;

    @PersistenceContext
    EntityManager em;

    @Before
    public void setUp() {

        em.createQuery("DELETE FROM StaticAddress s").executeUpdate();
    }


    @Test
    public void findByPostalCodeAndCityWorks() {

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity(KARLSRUHE);
        staticAddress1.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress1.setSuburb(SUBURB_INNENSTADT);
        staticAddress1.setSuburbNormalized(SUBURB_INNENSTADT.toUpperCase());
        staticAddress1.setPostalcode(POSTAL_CODE);
        staticAddress1.setLatitude(BigDecimal.ONE);
        staticAddress1.setLongitude(BigDecimal.ONE);
        staticAddress1.setUniqueId(UNIQUE_ID_ONE);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setCity(KARLSRUHE);
        staticAddress2.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress2.setSuburb(SUBURB_DAXLANDEN);
        staticAddress2.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress2.setPostalcode(POSTAL_CODE);
        staticAddress2.setLatitude(BigDecimal.TEN);
        staticAddress2.setLongitude(BigDecimal.TEN);
        staticAddress2.setUniqueId(UNIQUE_ID_TWO);

        em.persist(staticAddress1);
        em.persist(staticAddress2);
        em.flush();

        List<StaticAddress> staticAddresses = sut.findByPostalCodeAndCity(POSTAL_CODE, KARLSRUHE);

        assertThat(staticAddresses, hasSize(2));
        assertThat(staticAddresses.get(0).getSuburb(), is(SUBURB_DAXLANDEN));
        assertThat(staticAddresses.get(1).getSuburb(), is(SUBURB_INNENSTADT));
    }


    @Test
    public void findByPostalCodeOrCityWorks() {

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity(KARLSRUHE);
        staticAddress1.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress1.setSuburb(SUBURB_INNENSTADT);
        staticAddress1.setSuburbNormalized(SUBURB_INNENSTADT.toUpperCase());
        staticAddress1.setPostalcode(POSTALCODE_12345);
        staticAddress1.setLatitude(BigDecimal.ONE);
        staticAddress1.setLongitude(BigDecimal.ONE);
        staticAddress1.setUniqueId(UNIQUE_ID_ONE);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setCity(MANNHEIM);
        staticAddress2.setCityNormalized(MANNHEIM.toUpperCase());
        staticAddress2.setSuburb(SUBURB_DAXLANDEN);
        staticAddress2.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress2.setPostalcode(POSTAL_CODE);
        staticAddress2.setLatitude(BigDecimal.TEN);
        staticAddress2.setLongitude(BigDecimal.TEN);
        staticAddress2.setUniqueId(UNIQUE_ID_TWO);

        em.persist(staticAddress1);
        em.persist(staticAddress2);
        em.flush();

        List<StaticAddress> staticAddresses = sut.findByPostalCodeOrCity(POSTAL_CODE, KARLSRUHE);

        assertThat(staticAddresses, hasSize(2));
        assertThat(staticAddresses.get(0).getSuburb(), is(SUBURB_INNENSTADT));
        assertThat(staticAddresses.get(1).getSuburb(), is(SUBURB_DAXLANDEN));
    }


    @Test
    public void findByCountryAndPostalCodeAndCityWorks() {

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity(KARLSRUHE);
        staticAddress1.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress1.setSuburb(SUBURB_INNENSTADT);
        staticAddress1.setSuburbNormalized(SUBURB_INNENSTADT.toUpperCase());
        staticAddress1.setPostalcode(POSTAL_CODE);
        staticAddress1.setLatitude(BigDecimal.ONE);
        staticAddress1.setLongitude(BigDecimal.ONE);
        staticAddress1.setUniqueId(UNIQUE_ID_ONE);
        staticAddress1.setCountry(COUNTRY_DE);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setCity(KARLSRUHE);
        staticAddress2.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress2.setSuburb(SUBURB_DAXLANDEN);
        staticAddress2.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress2.setPostalcode(POSTAL_CODE);
        staticAddress2.setLatitude(BigDecimal.TEN);
        staticAddress2.setLongitude(BigDecimal.TEN);
        staticAddress2.setUniqueId(UNIQUE_ID_TWO);

        em.persist(staticAddress1);
        em.persist(staticAddress2);

        List<StaticAddress> staticAddresses = sut.findByCountryAndPostalCodeAndCity(POSTAL_CODE, KARLSRUHE, COUNTRY_DE);

        assertThat(staticAddresses, hasSize(1));
        assertThat(staticAddresses.get(0).getSuburb(), is(SUBURB_INNENSTADT));
    }


    @Test
    public void findByCountryAndPostalCodeAndCityWithEmptyCityWorks() {

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity(KARLSRUHE);
        staticAddress1.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress1.setSuburb(SUBURB_INNENSTADT);
        staticAddress1.setSuburbNormalized(SUBURB_INNENSTADT.toUpperCase());
        staticAddress1.setPostalcode(POSTAL_CODE);
        staticAddress1.setLatitude(BigDecimal.ONE);
        staticAddress1.setLongitude(BigDecimal.ONE);
        staticAddress1.setUniqueId(UNIQUE_ID_ONE);
        staticAddress1.setCountry(COUNTRY_DE);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setCity(KARLSRUHE);
        staticAddress2.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress2.setSuburb(SUBURB_DAXLANDEN);
        staticAddress2.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress2.setPostalcode(POSTAL_CODE);
        staticAddress2.setLatitude(BigDecimal.TEN);
        staticAddress2.setLongitude(BigDecimal.TEN);
        staticAddress2.setUniqueId(UNIQUE_ID_TWO);

        em.persist(staticAddress1);
        em.persist(staticAddress2);

        List<StaticAddress> staticAddresses = sut.findByCountryAndPostalCodeAndCity(POSTAL_CODE, "", COUNTRY_DE);

        assertThat(staticAddresses, hasSize(1));
        assertThat(staticAddresses.get(0).getSuburb(), is(SUBURB_INNENSTADT));
    }


    @Test
    public void findByCountryAndPostalCodeOrCityWorks() {

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity(KARLSRUHE);
        staticAddress1.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress1.setSuburb(SUBURB_INNENSTADT);
        staticAddress1.setSuburbNormalized(SUBURB_INNENSTADT.toUpperCase());
        staticAddress1.setPostalcode(POSTAL_CODE);
        staticAddress1.setLatitude(BigDecimal.ONE);
        staticAddress1.setLongitude(BigDecimal.ONE);
        staticAddress1.setUniqueId(UNIQUE_ID_ONE);
        staticAddress1.setCountry(COUNTRY_DE);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setCity(KARLSRUHE);
        staticAddress2.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress2.setSuburb(SUBURB_DAXLANDEN);
        staticAddress2.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress2.setPostalcode(POSTAL_CODE);
        staticAddress2.setLatitude(BigDecimal.TEN);
        staticAddress2.setLongitude(BigDecimal.TEN);
        staticAddress2.setUniqueId(UNIQUE_ID_TWO);

        StaticAddress staticAddress3 = new StaticAddress();
        staticAddress3.setCity(MANNHEIM);
        staticAddress3.setCityNormalized(MANNHEIM.toUpperCase());
        staticAddress3.setSuburb(SUBURB_DAXLANDEN);
        staticAddress3.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress3.setPostalcode(POSTALCODE_12345);
        staticAddress3.setLatitude(BigDecimal.TEN);
        staticAddress3.setLongitude(BigDecimal.ONE);
        staticAddress3.setUniqueId(UNIQUE_ID_THREE);
        staticAddress3.setCountry(COUNTRY_DE);

        em.persist(staticAddress1);
        em.persist(staticAddress2);
        em.persist(staticAddress3);

        List<StaticAddress> staticAddresses = sut.findByCountryAndPostalCodeOrCity(POSTALCODE_12345, KARLSRUHE,
                COUNTRY_DE);
        assertThat(staticAddresses, hasSize(2));
        assertThat(staticAddresses.get(0).getCity(), is(KARLSRUHE));
        assertThat(staticAddresses.get(1).getCity(), is(MANNHEIM));
    }


    @Test
    public void findByCountryAndPostalCodeOrCityWithoutSuburbWorks() {

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity(KARLSRUHE);
        staticAddress1.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress1.setSuburb("");
        staticAddress1.setSuburbNormalized("");
        staticAddress1.setPostalcode(POSTAL_CODE);
        staticAddress1.setLatitude(BigDecimal.ONE);
        staticAddress1.setLongitude(BigDecimal.ONE);
        staticAddress1.setUniqueId(UNIQUE_ID_ONE);
        staticAddress1.setCountry(COUNTRY_DE);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setCity(KARLSRUHE);
        staticAddress2.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress2.setSuburb("");
        staticAddress2.setSuburbNormalized("");
        staticAddress2.setPostalcode(POSTAL_CODE);
        staticAddress2.setLatitude(BigDecimal.TEN);
        staticAddress2.setLongitude(BigDecimal.TEN);
        staticAddress2.setUniqueId(UNIQUE_ID_TWO);

        StaticAddress staticAddress3 = new StaticAddress();
        staticAddress3.setCity(MANNHEIM);
        staticAddress3.setCityNormalized(MANNHEIM.toUpperCase());
        staticAddress3.setSuburb("");
        staticAddress3.setSuburbNormalized("");
        staticAddress3.setPostalcode(POSTALCODE_12345);
        staticAddress3.setLatitude(BigDecimal.TEN);
        staticAddress3.setLongitude(BigDecimal.ONE);
        staticAddress3.setUniqueId(UNIQUE_ID_THREE);
        staticAddress3.setCountry(COUNTRY_DE);

        em.persist(staticAddress1);
        em.persist(staticAddress2);
        em.persist(staticAddress3);

        List<StaticAddress> staticAddresses = sut.findByCountryAndPostalCodeOrCity(POSTALCODE_12345, KARLSRUHE,
                COUNTRY_DE);
        assertThat(staticAddresses, hasSize(2));
        assertThat(staticAddresses.get(0).getCity(), is(KARLSRUHE));
        assertThat(staticAddresses.get(1).getCity(), is(MANNHEIM));
    }


    @Test
    public void findbyLatLonWorks() {

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity(KARLSRUHE);
        staticAddress1.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress1.setSuburb(SUBURB_INNENSTADT);
        staticAddress1.setSuburbNormalized(SUBURB_INNENSTADT.toUpperCase());
        staticAddress1.setPostalcode(POSTAL_CODE);
        staticAddress1.setLatitude(BigDecimal.ONE);
        staticAddress1.setLongitude(BigDecimal.ONE);
        staticAddress1.setUniqueId(UNIQUE_ID_ONE);
        staticAddress1.setCountry(COUNTRY_DE);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setCity(KARLSRUHE);
        staticAddress2.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress2.setSuburb(SUBURB_DAXLANDEN);
        staticAddress2.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress2.setPostalcode(POSTAL_CODE);
        staticAddress2.setLatitude(BigDecimal.ONE);
        staticAddress2.setLongitude(BigDecimal.TEN);
        staticAddress2.setUniqueId(UNIQUE_ID_TWO);

        em.persist(staticAddress1);
        em.persist(staticAddress2);

        StaticAddress staticAddress = sut.findByLatitudeAndLongitude(BigDecimal.ONE, BigDecimal.ONE);
        assertThat(staticAddress, notNullValue());
        assertReflectionEquals(staticAddress1, staticAddress);
    }


    @Test
    public void findByCityAndSuburbAndPostalcode() {

        String city = "City";
        String suburb = "suburb";
        String postalcode = "76131";

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setCity(city);
        staticAddress.setCityNormalized(city.toUpperCase());
        staticAddress.setSuburb(suburb);
        staticAddress.setSuburbNormalized(suburb.toUpperCase());
        staticAddress.setPostalcode(postalcode);
        staticAddress.setLongitude(new BigDecimal("2"));
        staticAddress.setLatitude(new BigDecimal("2"));
        staticAddress.setUniqueId(UNIQUE_ID_THREE);

        em.persist(staticAddress);

        List<StaticAddress> staticAddressList = sut.findByCityNormalizedAndSuburbNormalizedAndPostalcode(city, suburb,
                postalcode);

        assertThat(staticAddressList.get(0), is(staticAddress));
        assertThat(staticAddressList, hasSize(1));
    }


    @Test
    public void findByPostalcode() {

        createTestData();

        List<StaticAddress> staticAddressList = sut.findByPostalcode(POSTALCODE_12345);

        assertThat(staticAddressList, hasSize(1));
        assertThat(staticAddressList.get(0).getPostalcode(), is(POSTALCODE_12345));
    }


    @Test
    public void findNoAddressByPostalcode() {

        createTestData();

        List<StaticAddress> staticAddressList = sut.findByPostalcode("55443");

        assertThat(staticAddressList, emptyCollectionOf(StaticAddress.class));
    }


    @Test
    public void findNoAddressByPostalcodeAndCountry() {

        createTestData();

        List<StaticAddress> staticAddressList = sut.findByCountryAndPostalCodeOrCity("37678", "", "DE");
        assertThat(staticAddressList, emptyCollectionOf(StaticAddress.class));
    }


    private void createTestData() {

        StaticAddress withoutSuburb = new StaticAddress();
        withoutSuburb.setCountry(COUNTRY_DE);
        withoutSuburb.setCity(CITY_ENTENHAUSEN);
        withoutSuburb.setCityNormalized(CITY_ENTENHAUSEN);
        withoutSuburb.setSuburb("");
        withoutSuburb.setSuburbNormalized("");
        withoutSuburb.setPostalcode(POSTALCODE_12345);
        withoutSuburb.setLatitude(BigDecimal.ONE);
        withoutSuburb.setLongitude(BigDecimal.ONE);
        withoutSuburb.setUniqueId(UNIQUE_ID_TEN);

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setCity(KARLSRUHE);
        staticAddress.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress.setSuburb(SUBURB_DAXLANDEN);
        staticAddress.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress.setPostalcode(POSTAL_CODE);
        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(BigDecimal.TEN);
        staticAddress.setUniqueId(UNIQUE_ID_TWO);

        em.persist(withoutSuburb);
        em.persist(staticAddress);
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
        staticAddressWithoutHashkey.setLongitude(BigDecimal.ONE);
        staticAddressWithoutHashkey.setLatitude(BigDecimal.ONE);
        staticAddressWithoutHashkey.setUniqueId(UNIQUE_ID_FOUR);

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
        staticAddressWithHashKey.setLongitude(BigDecimal.TEN);
        staticAddressWithHashKey.setLatitude(BigDecimal.TEN);
        staticAddressWithHashKey.setUniqueId(UNIQUE_ID_FIVE);

        em.persist(staticAddressWithoutHashkey);
        em.persist(staticAddressWithHashKey);

        Pageable pageable = new PageRequest(0, 50);

        Page<StaticAddress> actualResult = sut.findMissingHashKeys(pageable);

        assertThat(actualResult.getContent(), contains(staticAddressWithoutHashkey));
        assertThat(actualResult.getContent(), not(contains(staticAddressWithHashKey)));
    }


    @Test
    public void findByBoundingBox() {

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity(KARLSRUHE);
        staticAddress1.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress1.setSuburb(SUBURB_INNENSTADT);
        staticAddress1.setSuburbNormalized(SUBURB_INNENSTADT.toUpperCase());
        staticAddress1.setPostalcode(POSTAL_CODE);
        staticAddress1.setLatitude(BigDecimal.ONE);
        staticAddress1.setLongitude(BigDecimal.ONE);
        staticAddress1.setUniqueId(UNIQUE_ID_ONE);
        staticAddress1.setCountry(COUNTRY_DE);

        StaticAddress staticAddress2 = new StaticAddress();
        staticAddress2.setCity(KARLSRUHE);
        staticAddress2.setCityNormalized(KARLSRUHE.toUpperCase());
        staticAddress2.setSuburb(SUBURB_DAXLANDEN);
        staticAddress2.setSuburbNormalized(SUBURB_DAXLANDEN.toUpperCase());
        staticAddress2.setPostalcode(POSTAL_CODE);
        staticAddress2.setLatitude(BigDecimal.ONE);
        staticAddress2.setLongitude(BigDecimal.TEN);
        staticAddress2.setUniqueId(UNIQUE_ID_TWO);

        em.persist(staticAddress1);
        em.persist(staticAddress2);

        List<StaticAddress> staticAddresses = sut.findByBoundingBox(BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ZERO,
                BigDecimal.ONE);

        assertThat(staticAddresses, hasSize(1));
        assertThat(staticAddresses.get(0).getSuburb(), is(SUBURB_INNENSTADT));
    }
}
