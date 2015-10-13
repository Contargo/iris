package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.api.NotFoundException;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        return routeDataRevisionRepository.findNearest(terminal, destination.getLatitude(), destination.getLongitude(), new Date());
    }


    @Override
    @Transactional(readOnly = true)
    public RouteDataRevision getRouteDataRevision(BigInteger terminalUid, GeoLocation destination, Date date) {

        Terminal terminal = terminalService.getByUniqueId(terminalUid);

        if (terminal == null) {
            throw new RevisionDoesNotExistException("Terminal with uid " + terminalUid + "  does not exist",
                "terminal.notfound");
        }

        RouteDataRevision nearest = routeDataRevisionRepository.findNearest(terminal, destination.getLatitude(),
                destination.getLongitude(), date == null? new Date(): date);

        if (nearest == null) {
            throw new RevisionDoesNotExistException("Route revision for terminal with uid " + terminalUid
                    + ", coordinates " + destination.getLatitude() + "," + destination.getLongitude() + " and date: " 
                    + new SimpleDateFormat(RouteDataRevisionDto.DATE_FORMAT).format(date)
                    + " does not exist", "routerevision.notfound");
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

        RouteDataRevision save = routeDataRevisionRepository.save(routeDataRevision);

        return getRouteDataRevision(save.getId());
    }


    @Override
    public boolean overlapsWithExisting(BigInteger terminalUniqueId, BigDecimal latitude, BigDecimal longitude,
        ValidityRange validityRange, Long routeRevisionId) {

        List<RouteDataRevision> revisions = routeDataRevisionRepository.findByTerminalAndLatitudeAndLongitude(
                terminalUniqueId, latitude, longitude);

        return revisions.stream().filter(revision -> !revision.getId().equals(routeRevisionId)).anyMatch(revision ->
                    validityRange.overlapWith(revision.getValidityRange()));
    }
}
