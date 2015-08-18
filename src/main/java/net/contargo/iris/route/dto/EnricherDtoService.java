package net.contargo.iris.route.dto;

import net.contargo.iris.connection.dto.RouteDto;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public interface EnricherDtoService {

    /**
     * Enriches a {@link net.contargo.iris.connection.dto.RouteDto}.
     *
     * @param  route  to be enriched
     *
     * @return  enriched {@link net.contargo.iris.connection.dto.RouteDto}
     */
    RouteDto enrich(RouteDto route);
}
