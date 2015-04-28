package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.terminal.Terminal;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class RouteDataRevisionServiceImpl implements RouteDataRevisionService {

    private final RouteDataRevisionRepository routeDataRevisionRepository;

    public RouteDataRevisionServiceImpl(RouteDataRevisionRepository routeDataRevisionRepository) {

        this.routeDataRevisionRepository = routeDataRevisionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDataRevision getRouteDataRevision(Terminal terminal, Address destination) {

        return routeDataRevisionRepository.findNearest(terminal, destination.getLatitude(), destination.getLongitude());
    }


    @Override
    public List<RouteDataRevision> getRouteDataRevisions() {

        return routeDataRevisionRepository.findAll();
    }


    @Override
    public List<RouteDataRevision> getRouteDataRevisions(Terminal terminal) {

        return routeDataRevisionRepository.findByTerminal(terminal);
    }
}
