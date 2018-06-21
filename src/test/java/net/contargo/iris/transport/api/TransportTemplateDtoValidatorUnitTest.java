package net.contargo.iris.transport.api;

import net.contargo.iris.transport.api.TransportTemplateDto.TransportTemplateSegment;

import org.junit.Test;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.StopType.TERMINAL;

import static org.junit.Assert.fail;

import static java.util.Collections.singletonList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TransportTemplateDtoValidatorUnitTest {

    @Test
    public void validationSucceeds() {

        TransportStop from = new TransportStop(TERMINAL, null, null, null);
        TransportStop to = new TransportStop(TERMINAL, null, null, null);

        TransportTemplateSegment segment = new TransportTemplateSegment(from, to, null, null, null);
        TransportTemplateDtoValidator.validate(new TransportTemplateDto(singletonList(segment)));
    }


    @Test
    public void validationFailsTerminalSpecifiedOnTransportTemplate() {

        TransportStop stopWithoutTerminal = new TransportStop(TERMINAL, null, null, null);
        TransportStop stopWithTerminal = new TransportStop(TERMINAL, "uuid", null, null);
        TransportTemplateSegment segment;

        segment = new TransportTemplateSegment(stopWithoutTerminal, stopWithTerminal, null, null, null);
        assertIllegalArgumentExceptionThrown(segment);

        segment = new TransportTemplateSegment(stopWithTerminal, stopWithoutTerminal, null, null, null);
        assertIllegalArgumentExceptionThrown(segment);
    }


    @Test
    public void validationFailsMoTSpecified() {

        TransportStop from = new TransportStop(TERMINAL, null, null, null);
        TransportStop to = new TransportStop(TERMINAL, null, null, null);

        TransportTemplateSegment segment = new TransportTemplateSegment(from, to, null, null, ROAD);
        assertIllegalArgumentExceptionThrown(segment);
    }


    private static void assertIllegalArgumentExceptionThrown(TransportTemplateSegment segment) {

        TransportTemplateDto template = new TransportTemplateDto(singletonList(segment));

        try {
            TransportTemplateDtoValidator.validate(template);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }
}
