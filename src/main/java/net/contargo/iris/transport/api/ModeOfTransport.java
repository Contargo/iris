package net.contargo.iris.transport.api;

import net.contargo.iris.route.RouteType;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public enum ModeOfTransport {

    RAIL(net.contargo.iris.route2.ModeOfTransport.RAIL, RouteType.RAIL),
    WATER(net.contargo.iris.route2.ModeOfTransport.WATER, RouteType.BARGE),
    ROAD(net.contargo.iris.route2.ModeOfTransport.ROAD, RouteType.TRUCK);

    private final RouteType routeType;
    private final net.contargo.iris.route2.ModeOfTransport mot;

    ModeOfTransport(net.contargo.iris.route2.ModeOfTransport mot, RouteType routeType) {

        this.mot = mot;
        this.routeType = routeType;
    }

    public net.contargo.iris.route2.ModeOfTransport getMot() {

        return this.mot;
    }


    public RouteType getRouteType() {

        return routeType;
    }


    public static ModeOfTransport fromRouteType(RouteType type) {

        switch (type) {
            case BARGE:
                return WATER;

            case RAIL:
                return RAIL;

            case TRUCK:
                return ROAD;

            default:
                throw new IllegalArgumentException();
        }
    }
}
