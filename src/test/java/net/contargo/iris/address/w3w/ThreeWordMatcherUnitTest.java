package net.contargo.iris.address.w3w;

import org.junit.Test;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class ThreeWordMatcherUnitTest {

    @Test
    public void isThreeWordAddress() {

        assertThat(ThreeWordMatcher.isThreeWordAddress("riches.lofts.guessing"), is(true));
        assertThat(ThreeWordMatcher.isThreeWordAddress("      riches.lofts.guessing   "), is(true));
        assertThat(ThreeWordMatcher.isThreeWordAddress(null), is(false));
        assertThat(ThreeWordMatcher.isThreeWordAddress("Gartenstr. 67"), is(false));
    }
}
