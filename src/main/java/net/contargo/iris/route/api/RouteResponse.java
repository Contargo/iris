package net.contargo.iris.route.api;

import net.contargo.iris.connection.dto.RouteDto;

import org.springframework.hateoas.ResourceSupport;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneid0r - schneider@synyx.de
 */
class RouteResponse extends ResourceSupport {

    private RouteDto route;

    public RouteDto getRoute() {

        return route;
    }


    public void setRoute(RouteDto route) {

        this.route = route;
    }
}
