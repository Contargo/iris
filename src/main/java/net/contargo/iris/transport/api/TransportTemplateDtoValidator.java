package net.contargo.iris.transport.api;

/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class TransportTemplateDtoValidator {

    /**
     * Validates a {@code TransportTemplateDto}. The template is rejected (indicated by an
     * {@code IllegalArgumentException} thrown) if there are terminal UUIDs or mode of transports specified on any
     * segment.
     *
     * @param  template  the transport template to validate
     *
     * @throws  IllegalArgumentException  if the template is considered invalid
     */
    static void validate(TransportTemplateDto template) {

        template.transportChain.forEach(s -> {
            noTerminalSpecified(s);
            noMoTSpecified(s);
        });
    }


    private static void noTerminalSpecified(TransportTemplateDto.TransportTemplateSegment segment) {

        if (segment.from.type == StopType.TERMINAL && segment.from.uuid != null) {
            throw new IllegalArgumentException("A transport template's terminal UUID must be null, but was: "
                + segment.from.uuid);
        }

        if (segment.to.type == StopType.TERMINAL && segment.to.uuid != null) {
            throw new IllegalArgumentException("A transport template's terminal UUID must be null, but was: "
                + segment.to.uuid);
        }
    }


    private static void noMoTSpecified(TransportTemplateDto.TransportTemplateSegment segment) {

        if (segment.modeOfTransport != null) {
            throw new IllegalArgumentException(
                "A transport template must not have any mode of transport specified, but was: "
                + segment.modeOfTransport);
        }
    }
}
