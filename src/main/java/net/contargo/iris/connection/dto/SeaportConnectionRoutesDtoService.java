package net.contargo.iris.connection.dto;

import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.seaport.dto.SeaportDto;

import java.util.List;


/**
 * Dto interfact for {@link net.contargo.iris.connection.service.SeaportConnectionRoutesService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface SeaportConnectionRoutesDtoService {

    List<RouteDto> getAvailableSeaportConnectionRoutes(SeaportDto seaport, RouteInformation routeInformation);
}
