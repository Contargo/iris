package net.contargo.iris.route.api;

import net.contargo.iris.connection.dto.RouteDto;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
class RoutesResponse extends ResourceSupport {

    private List<RouteDto> routes;

    public List<RouteDto> getRoutes() {

        return routes;
    }


    public void setRoutes(List<RouteDto> routes) {

        this.routes = routes;
    }
}
