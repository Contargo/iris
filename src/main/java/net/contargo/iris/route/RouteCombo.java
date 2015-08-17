package net.contargo.iris.route;

/**
 * Enum describing which combo mode a Route has. Each combo has one or more route types.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public enum RouteCombo {

    WATERWAY(new RouteType[] { RouteType.BARGE }),
    RAILWAY(new RouteType[] { RouteType.RAIL }),
    WATERWAY_RAIL(new RouteType[] { RouteType.BARGE_RAIL }),
    ALL(new RouteType[] { RouteType.BARGE, RouteType.RAIL, RouteType.BARGE_RAIL });

    private RouteType[] routeTypes;

    RouteCombo(RouteType[] routeTypes) {

        this.routeTypes = routeTypes.clone();
    }

    public RouteType[] getRouteTypes() {

        RouteType[] thisRouteTypes = routeTypes;

        return thisRouteTypes;
    }
}
