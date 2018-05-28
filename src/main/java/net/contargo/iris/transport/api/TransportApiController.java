package net.contargo.iris.transport.api;

import com.wordnik.swagger.annotations.Api;

import net.contargo.iris.transport.service.DescriptionGenerator;
import net.contargo.iris.transport.service.TransportDescriptionExtender;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import static java.util.stream.Collectors.toList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Api(value = "/transports", description = "UNDER TEST FOO")
@RestController
@RequestMapping("/transports")
public class TransportApiController {

    private final TransportDescriptionExtender transportDescriptionExtender;
    private final DescriptionGenerator descriptionGenerator;

    @Autowired
    public TransportApiController(TransportDescriptionExtender transportDescriptionExtender,
        DescriptionGenerator descriptionGenerator) {

        this.transportDescriptionExtender = transportDescriptionExtender;
        this.descriptionGenerator = descriptionGenerator;
    }

    @RequestMapping(value = "/all", method = POST)
    public List<TransportResponseDto> producePossibleTransportDescriptions(
        @RequestBody TransportTemplateDto template) {

        return descriptionGenerator.from(template)
            .stream()
            .map(transportDescriptionExtender::withRoutingInformation)
            .collect(toList());
    }


    @RequestMapping(value = "/single", method = POST)
    public TransportResponseDto route(@RequestBody TransportDescriptionDto description) {

        return transportDescriptionExtender.withRoutingInformation(description);
    }
}
