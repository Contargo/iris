package net.contargo.iris.address.staticsearch;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link StaticAddress}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressUnitTest {

    StaticAddress sut;

    @Before
    public void setUp() {

        sut = new StaticAddress();
    }


    @Test
    public void generateHashKey() {

        sut.setUniqueId(new BigInteger("1301000000000001"));
        assertThat(sut.getHashKey(), is("D3YTD"));

        sut.setUniqueId(new BigInteger("1300000000100011"));
        assertThat(sut.getHashKey(), is("CJMWB"));

        sut.setUniqueId(new BigInteger("0001000000000001"));
        assertThat(sut.getHashKey(), is("0MH35"));
    }


    @Test
    public void generateHashKeyCheckDuplicates() {

        // tests every single incremented uniqueId for duplication.
        // shows when the first duplication appears.
        // use this test to try out the hashkey algorithm and possible outcomes.
        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setUniqueId(new BigInteger("1301000000000001"));

        boolean duplicateFound = false;
        Set<String> hashKeys = new HashSet<>();
        String currentHashKey;

        while (!duplicateFound) {
            staticAddress.setUniqueId(staticAddress.getUniqueId().add(BigInteger.ONE));
            currentHashKey = staticAddress.getHashKey();

            if (hashKeys.contains(currentHashKey)) {
                duplicateFound = true;
            }

            // commented because of performance issues. uncomment to get clearer output.
//            System.out.println(staticAddress.getUniqueId() + ": " + currentHashKey);
            hashKeys.add(currentHashKey);
        }

        System.out.println("Fail at " + staticAddress.getUniqueId());

        // With the current implementation the first duplication should appear with this number.
        assertThat(staticAddress.getUniqueId(), is(new BigInteger("1301000001048578")));
    }


    @Test
    public void generateHashKeyWithNullUniqueId() {

        assertThat(sut.getHashKey(), is(""));
    }


    @Test
    public void generateHashKeyWithTooShortUniqueId() {

        sut.setUniqueId(BigInteger.ONE);
        assertThat(sut.getHashKey(), is("00001"));
    }


    @Test
    public void areAddressParametersDifferent() {

        sut.setSuburb("suburb");
        sut.setCity("city");
        sut.setPostalcode("12345");

        StaticAddress staticAddress = new StaticAddress();

        staticAddress.setSuburb("suburb");
        staticAddress.setCity("city");
        staticAddress.setPostalcode("12345");

        boolean areDifferent = sut.areAddressParametersDifferent(staticAddress);
        assertThat(areDifferent, is(false));
    }


    @Test
    public void addressParameterSuburbDifferent() {

        sut.setSuburb("suburb");
        sut.setCity("city");
        sut.setPostalcode("12345");

        StaticAddress staticAddress = new StaticAddress();

        staticAddress.setSuburb("newsuburb");
        staticAddress.setCity("city");
        staticAddress.setPostalcode("12345");

        boolean areDifferent = sut.areAddressParametersDifferent(staticAddress);
        assertThat(areDifferent, is(true));
    }


    @Test
    public void addressParameterCityDifferent() {

        sut.setSuburb("suburb");
        sut.setCity("city");
        sut.setPostalcode("12345");

        StaticAddress staticAddress = new StaticAddress();

        staticAddress.setSuburb("suburb");
        staticAddress.setCity("newcity");
        staticAddress.setPostalcode("12345");

        boolean areDifferent = sut.areAddressParametersDifferent(staticAddress);
        assertThat(areDifferent, is(true));
    }


    @Test
    public void addressParameterPostalcodeDifferent() {

        sut.setSuburb("suburb");
        sut.setCity("city");
        sut.setPostalcode("12345");

        StaticAddress staticAddress = new StaticAddress();

        staticAddress.setSuburb("suburb");
        staticAddress.setCity("city");
        staticAddress.setPostalcode("67890");

        boolean areDifferent = sut.areAddressParametersDifferent(staticAddress);
        assertThat(areDifferent, is(true));
    }


    @Test
    public void latitudeAndLongitudeEquals() {

        BigDecimal latitude = new BigDecimal("34.12345");
        BigDecimal longitude = new BigDecimal("55.98765");

        sut.setLatitude(latitude);
        sut.setLongitude(longitude);

        StaticAddress staticAddress = new StaticAddress();

        staticAddress.setLatitude(latitude);
        staticAddress.setLongitude(longitude);

        boolean areDifferent = sut.areLatitudeAndLongitudeDifferent(staticAddress);
        assertThat(areDifferent, is(false));
    }


    @Test
    public void latitudeDifferent() {

        BigDecimal latitude = new BigDecimal("34.12345");
        BigDecimal longitude = new BigDecimal("55.98765");

        sut.setLatitude(latitude);
        sut.setLongitude(longitude);

        StaticAddress staticAddress = new StaticAddress();

        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(longitude);

        boolean areDifferent = sut.areLatitudeAndLongitudeDifferent(staticAddress);
        assertThat(areDifferent, is(true));
    }


    @Test
    public void longitudeDifferent() {

        BigDecimal latitude = new BigDecimal("34.12345");
        BigDecimal longitude = new BigDecimal("55.98765");

        sut.setLatitude(latitude);
        sut.setLongitude(longitude);

        StaticAddress staticAddress = new StaticAddress();

        staticAddress.setLatitude(latitude);
        staticAddress.setLongitude(BigDecimal.TEN);

        boolean areDifferent = sut.areLatitudeAndLongitudeDifferent(staticAddress);
        assertThat(areDifferent, is(true));
    }


    @Test
    public void latitudeAndLongitudeDifferent() {

        BigDecimal latitude = new BigDecimal("34.12345");
        BigDecimal longitude = new BigDecimal("55.98765");

        sut.setLatitude(latitude);
        sut.setLongitude(longitude);

        StaticAddress staticAddress = new StaticAddress();

        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(BigDecimal.TEN);

        boolean areDifferent = sut.areLatitudeAndLongitudeDifferent(staticAddress);
        assertThat(areDifferent, is(true));
    }
}
