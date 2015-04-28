package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.terminal.dto.TerminalDto;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class RouteDataRevisionDtoServiceImpl implements RouteDataRevisionDtoService {

    private final RouteDataRevisionService routeDataRevisionService;

    public RouteDataRevisionDtoServiceImpl(RouteDataRevisionService routeDataRevisionService) {

        this.routeDataRevisionService = routeDataRevisionService;
    }

    @Override
    public List<RouteDataRevisionDto> getRouteDataRevisions() {

        return convertToDtoList(routeDataRevisionService.getRouteDataRevisions());
    }


    @Override
    public List<RouteDataRevisionDto> getRouteDataRevisions(TerminalDto terminal) {

        return convertToDtoList(routeDataRevisionService.getRouteDataRevisions(terminal.toEntity()));
    }

    @Override
    public RouteDataRevisionDto getRouteDataRevision(Long id) {

        return new RouteDataRevisionDto(routeDataRevisionService.getRouteDataRevision(id));
    }

    @Override
    public RouteDataRevisionDto save(RouteDataRevisionDto routeDataRevision) {

        return new RouteDataRevisionDto(routeDataRevisionService.save(routeDataRevision.toEntity()));
    }

    private List<RouteDataRevisionDto> convertToDtoList(List<RouteDataRevision> entities) {

        return entities.stream().
                map(RouteDataRevisionDto::new).
                collect(Collectors.toList());
    }
}
