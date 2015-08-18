package net.contargo.iris.route.service;

import net.contargo.iris.connection.dto.RouteDto;


/**
 * Service that serializes urls for routes.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface RouteUrlSerializationService {

    /**
     * Constructs a link with the given {@link net.contargo.iris.connection.dto.RouteDto}'s detail information.
     *
     * @param  route
     * @param  baseUrlRoute
     * @param  baseUrlRoutePart
     */
    void serializeUrl(RouteDto route, String baseUrlRoute, String baseUrlRoutePart);
}
