package net.contargo.iris.transport.inclinations.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.inclinations.client.InclinationsClient;
import net.contargo.iris.transport.inclinations.client.InclinationsRoutingClient;
import net.contargo.iris.transport.inclinations.dto.Point3D;

import org.springframework.core.convert.ConversionService;

import java.util.List;

import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.inclinations.service.Inclinations.zero;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class InclinationsService {

    private final InclinationsClient elevationServiceClient;
    private final InclinationsRoutingClient inclinationsRoutingClient;
    private final ConversionService conversionService;

    public InclinationsService(InclinationsClient elevationServiceClient,
        InclinationsRoutingClient inclinationsRoutingClient, ConversionService conversionService) {

        this.elevationServiceClient = elevationServiceClient;
        this.inclinationsRoutingClient = inclinationsRoutingClient;
        this.conversionService = conversionService;
    }

    public Inclinations get(TransportDescriptionDto description) {

        return description.transportChain.stream()
            .filter(t -> t.modeOfTransport == ROAD)
            .map(this::getInclinationsFor)
            .reduce(zero(), Inclinations::add);
    }


    private Inclinations getInclinationsFor(TransportDescriptionDto.TransportDescriptionSegment segment) {

        GeoLocation start = conversionService.convert(segment.from, GeoLocation.class);
        GeoLocation end = conversionService.convert(segment.to, GeoLocation.class);

        return mapToInclinations(elevationServiceClient.getElevations(
                    inclinationsRoutingClient.getPoints(start, end)));
    }


    private Inclinations mapToInclinations(List<Point3D> point3DS) {

        Inclinations result = zero();

        for (int i = 1; i < point3DS.size(); i++) {
            int diff = point3DS.get(i - 1).getElevation() - point3DS.get(i).getElevation();
            result = result.with(diff);
        }

        return result;
    }
}
