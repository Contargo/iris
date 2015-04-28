package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.terminal.dto.TerminalDto;

import java.util.List;


/**
 * Delegates to {@link net.contargo.iris.routedatarevision.service.RouteDataRevisionService } and converts
 * {@link net.contargo.iris.routedatarevision.RouteDataRevision} into
 * {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public interface RouteDataRevisionDtoService {

    /**
     * Finds all {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}.
     *
     * @return  a list of all {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}
     */
    List<RouteDataRevisionDto> getRouteDataRevisions();


    /**
     * Finds all {@link net.contargo.iris.routedatarevision.RouteDataRevision} belonging to the given terminal.
     *
     * @param  terminal  The parameter to filter the
     *                   {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}s.
     *
     * @return  a list of {@link net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto}s.
     */
    List<RouteDataRevisionDto> getRouteDataRevisions(TerminalDto terminal);
}
