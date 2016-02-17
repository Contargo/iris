package net.contargo.iris.routedatarevision;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertThat;

import static java.util.stream.Collectors.joining;


public class RouteDataRevisionUnitTest {

    private RouteDataRevision sut;

    @Before
    public void setUp() {

        sut = new RouteDataRevision();
    }


    @Test
    public void setPostalCode() {

        sut.setPostalCode("foo");

        assertThat(sut.getPostalCode(), is("foo"));
    }


    @Test
    public void setPostalCodeNull() {

        sut.setPostalCode(null);

        assertThat(sut.getPostalCode(), nullValue());
    }


    @Test(expected = IllegalArgumentException.class)
    public void setPostalCodeValidationError() {

        sut.setPostalCode(stringWithNChars(11));
    }


    @Test
    public void setCity() {

        sut.setCity(stringWithNChars(255));

        assertThat(sut.getCity(), is(stringWithNChars(255)));
    }


    @Test
    public void setCityNull() {

        sut.setCity(null);

        assertThat(sut.getCity(), nullValue());
    }


    @Test(expected = IllegalArgumentException.class)
    public void setCityValidationError() {

        sut.setCity(stringWithNChars(256));
    }


    @Test
    public void setCityNormalized() {

        sut.setCityNormalized(stringWithNChars(255));

        assertThat(sut.getCityNormalized(), is(stringWithNChars(255)));
    }


    @Test
    public void setCityNormalizedNull() {

        sut.setCityNormalized(null);

        assertThat(sut.getCityNormalized(), nullValue());
    }


    @Test(expected = IllegalArgumentException.class)
    public void setCityNormalizedValidationError() {

        sut.setCityNormalized(stringWithNChars(256));
    }


    @Test
    public void setCountry() {

        sut.setCountry(stringWithNChars(5));

        assertThat(sut.getCountry(), is(stringWithNChars(5)));
    }


    @Test
    public void setCountryNull() {

        sut.setCountry(null);

        assertThat(sut.getCountry(), nullValue());
    }


    @Test(expected = IllegalArgumentException.class)
    public void setCountryValidationError() {

        sut.setCountry(stringWithNChars(6));
    }


    private String stringWithNChars(int n) {

        return IntStream.range(0, n).mapToObj(p -> "1").collect(joining());
    }
}
