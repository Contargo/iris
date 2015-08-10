package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.api.NotFoundException;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;


public class RouteDataRevisionServiceImpl implements RouteDataRevisionService {

    private final RouteDataRevisionRepository routeDataRevisionRepository;
    private final TerminalService terminalService;

    public RouteDataRevisionServiceImpl(RouteDataRevisionRepository routeDataRevisionRepository,
        TerminalService terminalService) {

        this.routeDataRevisionRepository = routeDataRevisionRepository;
        this.terminalService = terminalService;
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDataRevision getRouteDataRevision(Terminal terminal, GeoLocation destination) {

        return routeDataRevisionRepository.findNearest(terminal, destination.getLatitude(), destination.getLongitude());
    }


    @Override
    @Transactional(readOnly = true)
    public RouteDataRevision getRouteDataRevision(BigInteger terminalUid, GeoLocation destination) {

        Terminal terminal = terminalService.getByUniqueId(terminalUid);

        RouteDataRevision nearest = routeDataRevisionRepository.findNearest(terminal, destination.getLatitude(),
                destination.getLongitude());

        if (nearest == null) {
            throw new RevisionDoesNotExistException("Route revision for terminal with uid " + terminalUid
                + " and coordinates " + destination.getLatitude()
                + "," + destination.getLongitude() + " does not exist", "routerevision.notfound");
        }

        return nearest;
    }


    @Override
    public List<RouteDataRevision> getRouteDataRevisions() {

        return routeDataRevisionRepository.findAll();
    }


    @Override
    public List<RouteDataRevision> getRouteDataRevisions(Long terminalId) {

        return routeDataRevisionRepository.findByTerminalId(terminalId);
    }


    @Override
    public RouteDataRevision getRouteDataRevision(Long id) {

        RouteDataRevision routeDataRevision = routeDataRevisionRepository.findOne(id);

        if (routeDataRevision == null) {
            throw new NotFoundException("Cannot find route data revision for id " + id);
        }

        return routeDataRevision;
    }


    @Override
    public RouteDataRevision save(RouteDataRevision routeDataRevision) {

        return routeDataRevisionRepository.save(routeDataRevision);
    }


    @Override
    public boolean existsEntry(BigInteger terminalUniqueId, BigDecimal latitude, BigDecimal longitude) {

        return routeDataRevisionRepository.findByTerminalAndLatitudeAndLongitude(terminalUniqueId, latitude, longitude)
            .isPresent();
    }
}
