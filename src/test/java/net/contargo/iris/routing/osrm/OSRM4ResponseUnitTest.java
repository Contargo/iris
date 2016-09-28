package net.contargo.iris.routing.osrm;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


public class OSRM4ResponseUnitTest {

    private static final String GERMAN_STREET_WITH_TOLL = "strA/;anytype/;yes/;de";
    private static final String GERMAN_STREET_WITHOUT_TOLL = "strA/;anytype/;no/;de";
    private static final String FRENCH_STREET_WITH_TOLL = "strA/;anytype/;yes/;fr";
    private static final String FRENCH_STREET_WITHOUT_TOLL = "strA/;anytype/;no/;fr";

    private OSRM4Response sut;

    @Test
    public void calculateZeroToll() {

        sut = new OSRM4Response();
        assertThat(sut.getToll(), is(BigDecimal.ZERO));
    }


    @Test
    public void calculateTollInGermany() {

        String[][] instructions = {
            { "", GERMAN_STREET_WITH_TOLL, "100", "" },
            { "", GERMAN_STREET_WITH_TOLL, "300", "" },
        };

        sut = new OSRM4Response();
        sut.setRoute_instructions(instructions);

        assertThat(sut.getToll(), is(new BigDecimal("0.40000")));
    }


    @Test
    public void calculateOnlyTollParts() {

        String[][] instructions = {
            { "", GERMAN_STREET_WITHOUT_TOLL, "200", "" },
            { "", GERMAN_STREET_WITH_TOLL, "100", "" },
            { "", FRENCH_STREET_WITH_TOLL, "300", "" },
            { "", FRENCH_STREET_WITHOUT_TOLL, "500", "" },
        };

        sut = new OSRM4Response();
        sut.setRoute_instructions(instructions);

        assertThat(sut.getToll(), is(new BigDecimal("0.10000")));
    }
}
