package net.contargo.iris.address;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


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
}
