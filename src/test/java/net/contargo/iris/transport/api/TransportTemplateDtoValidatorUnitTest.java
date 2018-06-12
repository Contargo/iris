package net.contargo.iris.transport.api;

import net.contargo.iris.transport.api.TransportTemplateDto.TransportSegment;

import org.junit.Test;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;

import static org.junit.Assert.fail;

import static java.util.Collections.singletonList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TransportTemplateDtoValidatorUnitTest {

    @Test
    public void validationSucceeds() {

        TransportSite from = new TransportSite(TERMINAL, null, null, null);
        TransportSite to = new TransportSite(TERMINAL, null, null, null);

        TransportSegment segment = new TransportSegment(from, to, null, null, null);
        TransportTemplateDtoValidator.validate(new TransportTemplateDto(singletonList(segment)));
    }


    @Test
    public void validationFailsTerminalSpecifiedOnTransportTemplate() {

        TransportSite siteWithoutTerminal = new TransportSite(TERMINAL, null, null, null);
        TransportSite siteWithTerminal = new TransportSite(TERMINAL, "uuid", null, null);
        TransportSegment segment;

        segment = new TransportSegment(siteWithoutTerminal, siteWithTerminal, null, null, null);
        assertIllegalArgumentExceptionThrown(segment);

        segment = new TransportSegment(siteWithTerminal, siteWithoutTerminal, null, null, null);
        assertIllegalArgumentExceptionThrown(segment);
    }


    @Test
    public void validationFailsMoTSpecified() {

        TransportSite from = new TransportSite(TERMINAL, null, null, null);
        TransportSite to = new TransportSite(TERMINAL, null, null, null);

        TransportSegment segment = new TransportSegment(from, to, null, null, ROAD);
        assertIllegalArgumentExceptionThrown(segment);
    }


    private static void assertIllegalArgumentExceptionThrown(TransportSegment segment) {

        TransportTemplateDto template = new TransportTemplateDto(singletonList(segment));

        try {
            TransportTemplateDtoValidator.validate(template);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }
}
