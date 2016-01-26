package net.contargo.iris.normalizer;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * Unit tests for {@link NormalizerServiceImpl}.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class NormalizerServiceImplUnitTest {

    private NormalizerService sut;

    @Before
    public void before() {

        sut = new NormalizerServiceImpl();
    }


    @Test
    public void testGermanSpecialCharsReplacement() {

        String text = "ö ä ü  Ö Ä Ü";

        assertThat("OEAEUEOEAEUE", is(sut.normalize(text)));
    }


    @Test
    public void testGermanDoubleSReplacement() {

        String text = "Karlstraße";

        assertThat("KARLSTRASSE", is(sut.normalize(text)));
    }


    @Test
    public void testNumbersRemovement() {

        String text = "Karlstrasse 68 76137 Karlsruhe";

        assertThat("KARLSTRASSEKARLSRUHE", is(sut.normalize(text)));
    }


    @Test
    public void testNullText() {

        assertThat(null, is(sut.normalize(null)));
    }


    @Test
    public void testEmptyText() {

        String text = "";

        assertThat(text, is(sut.normalize(text)));
    }


    @Test
    public void testPunctRemovement() {

        String text = "Karlstraße.68, 76137 Karlsruhe -.,";

        assertThat("KARLSTRASSEKARLSRUHE", is(sut.normalize(text)));
    }


    @Test
    public void testWhitespaceRemovement() {

        String text = "a b c d e f g             h              i  ";

        assertThat("ABCDEFGHI", is(sut.normalize(text)));
    }


    @Test
    public void testToUpperCase() {

        String text = "karlsruhe";

        assertThat("KARLSRUHE", is(sut.normalize(text)));
    }
}
