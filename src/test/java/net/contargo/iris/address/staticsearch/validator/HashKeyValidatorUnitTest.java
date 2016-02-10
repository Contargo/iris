package net.contargo.iris.address.staticsearch.validator;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Unit test of {@link HashKeyValidator}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class HashKeyValidatorUnitTest {

    private HashKeyValidator sut;

    @Before
    public void initData() {

        sut = new HashKeyValidator();
    }


    @Test
    public void ValidHashKeys() {

        String[] hashKeys = { "DAJHZ", "DAJ8T", "D57VJ", "D3YTN", "D6JKC" };

        for (String hashKey : hashKeys) {
            boolean valid = sut.validate(hashKey);
            assertThat(valid, is(true));
        }
    }


    @Test
    public void InValidHashKeys() {

        String[] hashKeys = { "AAAAAA", "abcde", "_D57VJ", " D3YTN", "D6JKC ", "sd867", null };

        for (String hashKey : hashKeys) {
            boolean valid = sut.validate(hashKey);
            assertThat(valid, is(false));
        }
    }
}
