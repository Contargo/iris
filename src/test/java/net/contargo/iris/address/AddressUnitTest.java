package net.contargo.iris.address;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;

import static junit.framework.TestCase.assertFalse;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.nullValue;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class AddressUnitTest {

    Map<String, String> addressDetailsMap;

    @Before
    public void before() {

        addressDetailsMap = new HashMap<>();
        addressDetailsMap.put("postcode", "1234567");
        addressDetailsMap.put("boundary", "boundary");
        addressDetailsMap.put("suburb", "Hagsfeld");
        addressDetailsMap.put("city", "Karlsruhe");
    }


    @Test
    public void getNiceName() {

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);

        assertThat(sut.getNiceName(), is("1234567 Karlsruhe (Hagsfeld)"));
    }


    @Test
    public void getNiceNameWithEmptySuburb() {

        addressDetailsMap.put("suburb", "");

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);

        assertThat(sut.getNiceName(), is("1234567 Karlsruhe"));
    }


    @Test
    public void getNiceNameWithNullSuburb() {

        addressDetailsMap.put("suburb", null);

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);

        assertThat(sut.getNiceName(), is("1234567 Karlsruhe"));
    }


    @Test
    public void getNiceNameWithNoSuburb() {

        addressDetailsMap.remove("suburb");

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);

        assertThat(sut.getNiceName(), is("1234567 Karlsruhe"));
    }


    @Test
    public void getNiceNameWithNullPostCode() {

        addressDetailsMap.put("postcode", null);

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);

        assertThat(sut.getNiceName(), is("boundary Karlsruhe (Hagsfeld)"));
    }


    @Test
    public void getNiceNameWithNoCity() {

        addressDetailsMap.remove("city");

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);
        sut.setShortName("Short Name");

        assertThat(sut.getNiceName(), is("Short Name"));
    }


    @Test
    public void testGetNiceNameWithNoBoundary() {

        addressDetailsMap.remove("boundary");
        addressDetailsMap.remove("postcode");

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);
        sut.setShortName("Short Name");

        assertThat(sut.getNiceName(), is("Short Name"));
    }


    @Test
    public void testGetNiceNameWithNoShortName() {

        addressDetailsMap.remove("boundary");
        addressDetailsMap.remove("postcode");

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);
        sut.setDisplayName("Display Name");

        assertThat(sut.getNiceName(), is("Display Name"));
    }


    @Test
    public void testGetNiceNameWithEmptyShortName() {

        addressDetailsMap.remove("boundary");
        addressDetailsMap.remove("postcode");

        Address sut = new Address();
        sut.setAddress(addressDetailsMap);
        sut.setDisplayName("Display Name");
        sut.setShortName("");

        assertThat(sut.getNiceName(), is("Display Name"));
    }


    @Test
    public void isStaticAddress() {

        Address sut = new Address();

        sut.setAddress(addressDetailsMap);
        assertFalse(sut.isStatic());

        addressDetailsMap.put("static_id", "foo");
        assertTrue(sut.isStatic());
    }


    @Test
    public void inSwitzerland() {

        Address sut = new Address();

        sut.setAddress(addressDetailsMap);
        assertFalse(sut.inSwitzerland());

        addressDetailsMap.put("country_code", "DE");
        assertFalse(sut.inSwitzerland());

        addressDetailsMap.put("country_code", "CH");
        assertTrue(sut.inSwitzerland());
    }


    @Test
    public void getCombinedCity() {

        Address sut = new Address();
        sut.getAddress().put("city", "Rottweil");
        sut.getAddress().put("suburb", "Wimmelburg");
        sut.getAddress().put("village", "Schonach");
        sut.getAddress().put("town", "Gomaringen");
        assertThat(sut.getCombinedCity(), is("Rottweil, Gomaringen, Schonach, Wimmelburg"));
    }


    @Test
    public void getCombinedCityEmpty() {

        Address sut = new Address();
        assertThat(sut.getCombinedCity(), isEmptyString());
    }


    @Test
    public void getCity() {

        Address sut = new Address();

        assertThat(sut.getCity(), nullValue());

        sut.getAddress().put("village", "foo");
        assertThat(sut.getCity(), is("foo"));

        sut.getAddress().put("town", "bar");
        assertThat(sut.getCity(), is("bar"));

        sut.getAddress().put("city", "baz");
        assertThat(sut.getCity(), is("baz"));
    }
}
