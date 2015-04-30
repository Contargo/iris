package net.contargo.iris.address.nominatim.service;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Arnold Franke - franke@synyx.de *
 */
public class AddressValidatorUnitTest {

    private static final String VALID_STREET = "validStreet";
    private static final String VALID_STREET_NULL = null;
    private static final String INVALID_STREET_TOO_SHORT = "a";
    private static final String RESULT_WHEN_INVALID = "";
    private AddressValidator sut;

    @Before
    public void setUp() {

        sut = new AddressValidator();
    }


    @Test
    public void testValidateStreetSuccessNormal() {

        assertThat(sut.validateStreet(VALID_STREET), is(VALID_STREET));
    }


    @Test
    public void testValidateStreetSuccessStreetNull() {

        assertThat(sut.validateStreet(VALID_STREET_NULL), is(VALID_STREET_NULL));
    }


    @Test
    public void testValidateStreetFailTooShort() {

        assertThat(sut.validateStreet(INVALID_STREET_TOO_SHORT), is(RESULT_WHEN_INVALID));
    }
}
