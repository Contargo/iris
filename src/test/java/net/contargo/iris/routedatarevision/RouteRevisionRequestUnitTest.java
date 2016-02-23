package net.contargo.iris.routedatarevision;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteRevisionRequestUnitTest {

    private RouteRevisionRequest sut;

    @Before
    public void setUp() {

        sut = new RouteRevisionRequest();
    }


    @Test
    public void setCityNull() {

        sut.setCity(null);

        assertThat(sut.getCity(), nullValue());
    }


    @Test
    public void setCityBlank() {

        sut.setCity("        ");

        assertThat(sut.getCity(), nullValue());
    }


    @Test
    public void setCity() {

        sut.setCity("Foo");

        assertThat(sut.getCity(), is("Foo"));
    }


    @Test
    public void setPostalCodeNull() {

        sut.setPostalcode(null);

        assertThat(sut.getPostalcode(), nullValue());
    }


    @Test
    public void setPostalCodeBlank() {

        sut.setPostalcode("        ");

        assertThat(sut.getPostalcode(), nullValue());
    }


    @Test
    public void setPostalCode() {

        sut.setPostalcode("Foo");

        assertThat(sut.getPostalcode(), is("Foo"));
    }


    @Test
    public void isEmpty() {

        assertThat(sut.isEmpty(), is(true));

        sut.setPostalcode("76135");

        assertThat(sut.isEmpty(), is(false));
    }


    @Test
    public void isValid() {

        assertThat(sut.isValid(), is(false));

        sut.setPostalcode("76135");

        assertThat(sut.isValid(), is(true));

        sut.setCity("Eisleben");

        assertThat(sut.isValid(), is(true));

        sut.setTerminalId(42L);

        assertThat(sut.isValid(), is(true));
    }
}
