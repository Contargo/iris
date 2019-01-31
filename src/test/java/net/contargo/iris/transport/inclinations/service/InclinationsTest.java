package net.contargo.iris.transport.inclinations.service;

import org.junit.Test;

import static net.contargo.iris.transport.inclinations.service.Inclinations.zero;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class InclinationsTest {

    @Test
    public void add() {

        Inclinations other = zero().with(10).with(-20);

        Inclinations sut = zero().add(other);

        assertThat(sut.getUp(), is(10));
        assertThat(sut.getDown(), is(20));
    }


    @Test
    public void with() {

        Inclinations sut = zero().with(10).with(-20);

        assertThat(sut.getDown(), is(20));
        assertThat(sut.getUp(), is(10));
    }


    @Test
    public void testZero() {

        Inclinations sut = zero();

        assertThat(sut.getDown(), is(0));
        assertThat(sut.getUp(), is(0));
    }
}
