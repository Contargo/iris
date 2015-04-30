package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerType;


/**
 * Encapsulates detail information that are needed to compute a {@link net.contargo.iris.route.Route}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteInformation {

    private GeoLocation destination;
    private RouteProduct product;
    private ContainerType containerType;
    private RouteDirection routeDirection;
    private RouteCombo routeCombo;

    public RouteInformation(ContainerType containerType, RouteCombo combo, double lat, double lon, boolean isRoundTrip,
        boolean isImport) {

        this.destination = new GeoLocation(lat, lon);
        this.containerType = containerType;
        this.routeCombo = combo;
        this.product = RouteProduct.fromIsRoundtrip(isRoundTrip);
        this.routeDirection = RouteDirection.fromIsImport(isImport);
    }


    public RouteInformation(GeoLocation destination, RouteProduct product, ContainerType containerType,
        RouteDirection routeDirection, RouteCombo combo) {

        this.destination = destination;
        this.product = product;
        this.containerType = containerType;
        this.routeDirection = routeDirection;
        this.routeCombo = combo;
    }

    public GeoLocation getDestination() {

        return destination;
    }


    public void setDestination(GeoLocation destination) {

        this.destination = destination;
    }


    public RouteProduct getProduct() {

        return product;
    }


    public void setProduct(RouteProduct product) {

        this.product = product;
    }


    public ContainerType getContainerType() {

        return containerType;
    }


    public void setContainerType(ContainerType containerType) {

        this.containerType = containerType;
    }


    public RouteDirection getRouteDirection() {

        return routeDirection;
    }


    public void setRouteDirection(RouteDirection routeDirection) {

        this.routeDirection = routeDirection;
    }


    public RouteCombo getRouteCombo() {

        return routeCombo;
    }


    public void setRouteCombo(RouteCombo routeCombo) {

        this.routeCombo = routeCombo;
    }
}
