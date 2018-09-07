package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;

import java.util.Collections;
import java.util.List;


/**
 * Dto layer for {@link RoutePart}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutePartDto {

    private String name;
    private RoutePartDataDto data;
    private GeoLocationDto origin;
    private ContainerState containerState;
    private ContainerType containerType;
    private RoutePart.Direction direction;
    private RouteType routeType;
    private GeoLocationDto destination;

    public RoutePartDto() {

        // needed for Spring MVC instantiation of Controller parameter
    }


    public RoutePartDto(RoutePart part) {

        if (part != null) {
            this.name = part.getName();
            this.containerState = part.getContainerState();
            this.containerType = part.getContainerType();
            this.direction = part.getDirection();
            this.routeType = part.getRouteType();
            this.data = part.getData() == null ? null : new RoutePartDataDto(part.getData());
            this.origin = GeolocationDtoFactory.createGeolocationDto(part.getOrigin());
            this.destination = GeolocationDtoFactory.createGeolocationDto(part.getDestination());
        }
    }

    public RoutePart toRoutePart() {

        RoutePart routePart = new RoutePart();

        if (this.origin != null && this.destination != null) {
            routePart.setDestination(this.destination.toEntity());
            routePart.setOrigin(this.origin.toEntity());
        }

        routePart.setRouteType(this.routeType);
        routePart.setContainerState(this.containerState);
        routePart.setContainerType(this.containerType);

        return routePart;
    }


    public String getName() {

        return name;
    }


    public RoutePartDataDto getData() {

        return data;
    }


    public GeoLocationDto getOrigin() {

        return origin;
    }


    public ContainerState getContainerState() {

        return containerState;
    }


    public ContainerType getContainerType() {

        return containerType;
    }


    public RoutePart.Direction getDirection() {

        return direction;
    }


    public RouteType getRouteType() {

        return routeType;
    }


    public GeoLocationDto getDestination() {

        return destination;
    }


    public void setName(String name) {

        this.name = name;
    }


    public void setData(RoutePartDataDto data) {

        this.data = data;
    }


    public void setOrigin(GeoLocationDto origin) {

        this.origin = origin;
    }


    public void setContainerState(ContainerState containerState) {

        this.containerState = containerState;
    }


    public void setContainerType(ContainerType containerType) {

        this.containerType = containerType;
    }


    public void setDirection(RoutePart.Direction direction) {

        this.direction = direction;
    }


    public void setRouteType(RouteType routeType) {

        this.routeType = routeType;
    }


    public void setDestination(GeoLocationDto destination) {

        this.destination = destination;
    }


    /**
     * This method exists in order to maintain backward compatibility regarding clients that are calling API endpoints
     * /routeDetails and /connections.
     *
     * @return  the empty list
     */
    public List<?> getSubRouteParts() {

        return Collections.emptyList();
    }
}
