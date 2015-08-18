package net.contargo.iris.route.dto;

import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.route.service.EnricherService;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class EnricherDtoServiceImpl implements EnricherDtoService {

    private final EnricherService enricherService;

    public EnricherDtoServiceImpl(EnricherService enricherService) {

        this.enricherService = enricherService;
    }

    @Override
    public RouteDto enrich(RouteDto routeDto) {

        return new RouteDto(enricherService.enrich(routeDto.toRoute()));
    }
}
