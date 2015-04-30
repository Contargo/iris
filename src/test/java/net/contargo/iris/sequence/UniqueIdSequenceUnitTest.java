package net.contargo.iris.sequence;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class UniqueIdSequenceUnitTest {

    @Test
    public void testIncrementNextId() {

        UniqueIdSequence sut = new UniqueIdSequence("bla", new BigInteger("42"));
        sut.incrementNextId();
        assertThat(sut.getNextId(), is(new BigInteger("43")));
    }
}
