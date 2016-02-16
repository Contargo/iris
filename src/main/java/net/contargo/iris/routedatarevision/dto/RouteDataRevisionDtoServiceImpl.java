package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.RouteRevisionRequest;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class RouteDataRevisionDtoServiceImpl implements RouteDataRevisionDtoService {

    private final RouteDataRevisionService routeDataRevisionService;
    private final TerminalService terminalService;

    public RouteDataRevisionDtoServiceImpl(TerminalService terminalService,
        RouteDataRevisionService routeDataRevisionService) {

        this.terminalService = terminalService;
        this.routeDataRevisionService = routeDataRevisionService;
    }

    @Override
    public List<RouteDataRevisionDto> getRouteDataRevisions() {

        return convertToDtoList(routeDataRevisionService.getRouteDataRevisions());
    }


    @Override
    public List<RouteDataRevisionDto> getRouteDataRevisions(Long terminalId) {

        return convertToDtoList(routeDataRevisionService.getRouteDataRevisions(terminalId));
    }


    @Override
    public RouteDataRevisionDto getRouteDataRevision(Long id) {

        return new RouteDataRevisionDto(routeDataRevisionService.getRouteDataRevision(id));
    }


    @Override
    public RouteDataRevisionDto save(RouteDataRevisionDto dto) {

        BigInteger terminalUniqueId = new BigInteger(dto.getTerminal().getUniqueId());
        Terminal terminal = terminalService.getByUniqueId(terminalUniqueId);

        return new RouteDataRevisionDto(routeDataRevisionService.save(dto.toEntity(terminal.getId())));
    }


    @Override
    public boolean existsEntry(String terminalUniqueId, BigDecimal latitude, BigDecimal longitude,
        ValidityRange validityRange, Long routeRevisionId) {

        return routeDataRevisionService.overlapsWithExisting(new BigInteger(terminalUniqueId), latitude, longitude,
                validityRange, routeRevisionId);
    }


    @Override
    public RouteDataRevisionDto findNearest(String terminalUniqueId, GeoLocation geoLocation, Date date) {

        return new RouteDataRevisionDto(routeDataRevisionService.getRouteDataRevision(new BigInteger(terminalUniqueId),
                    geoLocation, date));
    }


    @Override
    public List<RouteDataRevisionDto> search(RouteRevisionRequest routeRevisionRequest) {

        return convertToDtoList(routeDataRevisionService.search(routeRevisionRequest));
    }


    private List<RouteDataRevisionDto> convertToDtoList(List<RouteDataRevision> entities) {

        return entities.stream().map(RouteDataRevisionDto::new).collect(Collectors.toList());
    }
}
