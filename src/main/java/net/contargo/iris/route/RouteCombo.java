package net.contargo.iris.route;

/**
 * Enum describing which combo mode a Route has. Each combo has one or more route types.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public enum RouteCombo {

    WATERWAY(new RouteType[] { RouteType.BARGE }),
    RAILWAY(new RouteType[] { RouteType.RAIL }),
    DIRECT_TRUCK(new RouteType[] { RouteType.TRUCK }),
    ALL(new RouteType[] { RouteType.BARGE, RouteType.RAIL });
    // DIRECT_TRUCK is at the moment not in ALL included. #3194

    private RouteType[] routeTypes;

    private RouteCombo(RouteType[] routeTypes) {

        this.routeTypes = routeTypes.clone();
    }

    public RouteType[] getRouteTypes() {

        RouteType[] thisRouteTypes = routeTypes;

        return thisRouteTypes;
    }
}
