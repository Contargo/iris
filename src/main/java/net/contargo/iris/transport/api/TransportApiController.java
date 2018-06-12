package net.contargo.iris.transport.api;

import com.wordnik.swagger.annotations.Api;

import net.contargo.iris.transport.service.DescriptionGenerator;
import net.contargo.iris.transport.service.TransportDescriptionExtender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Api(value = "/transports")
@RestController
public class TransportApiController {

    private final TransportDescriptionExtender transportDescriptionExtender;
    private final DescriptionGenerator descriptionGenerator;
    private int maxRoutingThreads;

    @Autowired
    public TransportApiController(TransportDescriptionExtender transportDescriptionExtender,
        DescriptionGenerator descriptionGenerator,
        @Value("${routing.threads}") int maxRoutingThreads) {

        this.transportDescriptionExtender = transportDescriptionExtender;
        this.descriptionGenerator = descriptionGenerator;
        this.maxRoutingThreads = maxRoutingThreads;
    }

    @RequestMapping(value = "/transports", method = POST)
    public List<TransportResponseDto> transportDescriptionsFromTemplate(@RequestBody TransportTemplateDto template) {

        List<TransportDescriptionDto> descriptions = descriptionGenerator.from(template);

        ExecutorService executor = Executors.newFixedThreadPool(maxRoutingThreads);

        List<TransportResponseDto> result = descriptions.stream()
                .map(d ->
                            CompletableFuture.supplyAsync(() ->
                                    transportDescriptionExtender.withRoutingInformation(d),
                                executor))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        executor.shutdown();

        return result;
    }


    @RequestMapping(value = "/transport", method = POST)
    public TransportResponseDto transportDescription(@RequestBody TransportDescriptionDto description) {

        return transportDescriptionExtender.withRoutingInformation(description);
    }
}
