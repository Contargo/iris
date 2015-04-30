package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.service.SeaportConnectionRoutesService;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.seaport.dto.SeaportDto;

import java.util.ArrayList;
import java.util.List;


/**
 * Default implementation of {@link SeaportConnectionRoutesDtoService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportConnectionRoutesDtoServiceImpl implements SeaportConnectionRoutesDtoService {

    private final SeaportConnectionRoutesService seaportConnectionRoutesService;

    public SeaportConnectionRoutesDtoServiceImpl(SeaportConnectionRoutesService seaportConnectionRoutesService) {

        this.seaportConnectionRoutesService = seaportConnectionRoutesService;
    }

    @Override
    public List<RouteDto> getAvailableSeaportConnectionRoutes(SeaportDto seaportDto,
        RouteInformation routeInformation) {

        List<RouteDto> routeDtos = new ArrayList<>();

        if (seaportDto == null) {
            return routeDtos;
        }

        List<Route> routes = seaportConnectionRoutesService.getAvailableSeaportConnectionRoutes(seaportDto.toEntity(),
                routeInformation);

        for (Route route : routes) {
            routeDtos.add(new RouteDto(route));
        }

        return routeDtos;
    }
}
