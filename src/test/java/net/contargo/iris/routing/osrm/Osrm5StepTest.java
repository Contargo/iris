package net.contargo.iris.routing.osrm;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;


public class Osrm5StepTest {

    @Test
    public void getToll_germany() {

        Osrm5Route.Osrm5Step sut = new Osrm5Route.Osrm5Step("A 281/;motorway/;yes/;DE", new BigDecimal("42.42"),
                "geometry");

        assertThat(sut.getToll(), equalTo(new BigDecimal("42.42")));
    }


    @Test
    public void getToll_notGermany() {

        Osrm5Route.Osrm5Step sut = new Osrm5Route.Osrm5Step("A 281/;motorway/;yes/;FR", new BigDecimal("42.42"),
                "geometry");

        assertThat(sut.getToll(), equalTo(new BigDecimal("0")));
    }


    @Test
    public void getToll_germanyNoToll() {

        Osrm5Route.Osrm5Step sut = new Osrm5Route.Osrm5Step("A 281/;motorway/;no/;DE", new BigDecimal("42.42"),
                "geometry");

        assertThat(sut.getToll(), equalTo(new BigDecimal("0")));
    }


    @Test
    public void getCountry() {

        Osrm5Route.Osrm5Step sut = new Osrm5Route.Osrm5Step("A 281/;motorway/;no/;DE", new BigDecimal("42.42"),
                "geometry");

        assertThat(sut.getCountry(), is("DE"));
    }
}
