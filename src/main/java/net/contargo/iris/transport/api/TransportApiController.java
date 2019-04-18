package net.contargo.iris.transport.api;

import com.wordnik.swagger.annotations.Api;

import net.contargo.iris.transport.TransportChainGeneratorStrategyAdvisor;
import net.contargo.iris.transport.TransportDescriptionExtender;
import net.contargo.iris.transport.elevation.Elevations;
import net.contargo.iris.transport.elevation.ElevationsService;
import net.contargo.iris.transport.elevation.Inclinations;
import net.contargo.iris.transport.elevation.InclinationsService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Petra Scherer - scherer@synyx.de
 */
@Api(value = "/transports")
@RestController
public class TransportApiController {

    private final TransportDescriptionExtender transportDescriptionExtender;
    private final TransportChainGeneratorStrategyAdvisor transportChainGeneratorAdvisor;
    private final ExecutorService executorService;
    private final InclinationsService inclinationsService;
    private final ElevationsService elevationsService;

    @Autowired
    public TransportApiController(TransportDescriptionExtender transportDescriptionExtender,
        TransportChainGeneratorStrategyAdvisor generatorStrategyAdvisor, ExecutorService executorService,
        InclinationsService inclinationsService, ElevationsService elevationsService) {

        this.transportDescriptionExtender = transportDescriptionExtender;
        this.transportChainGeneratorAdvisor = generatorStrategyAdvisor;
        this.executorService = executorService;
        this.inclinationsService = inclinationsService;
        this.elevationsService = elevationsService;
    }

    @RequestMapping(value = "/transports", method = POST)
    public List<TransportResponseDto> transportDescriptionsFromTemplate(@RequestBody TransportTemplateDto template) {

        TransportTemplateDtoValidator.validate(template);

        List<TransportDescriptionDto> descriptions = transportChainGeneratorAdvisor.advice(template).get();

        return descriptions.stream().map(description ->
                        CompletableFuture.supplyAsync(() ->
                                transportDescriptionExtender.withRoutingInformation(description),
                            executorService)).map(CompletableFuture::join).collect(Collectors.toList());
    }


    @RequestMapping(value = "/transport", method = POST)
    public TransportResponseDto transportDescription(@RequestBody TransportDescriptionDto description) {

        return transportDescriptionExtender.withRoutingInformation(description);
    }


    @RequestMapping(value = "/transport/inclinations", method = POST)
    public InclinationsResponseDto inclinations(@RequestBody TransportDescriptionDto description) {

        return mapToResponse(inclinationsService.get(description));
    }


    @RequestMapping(value = "/transport/elevations", method = POST)
    public ElevationsResponseDto elevations(@RequestBody TransportDescriptionDto description) {

        return mapToResponseElevations(elevationsService.getElevationsFor(description));
    }


    private static InclinationsResponseDto mapToResponse(Inclinations inclinations) {

        return new InclinationsResponseDto(inclinations.getUp(), inclinations.getDown());
    }


    private static ElevationsResponseDto mapToResponseElevations(Elevations elevations) {

        return new ElevationsResponseDto(elevations);
    }
}
