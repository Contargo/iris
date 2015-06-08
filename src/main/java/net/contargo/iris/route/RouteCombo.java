package net.contargo.iris.route;

/**
 * Enum describing which combo mode a Route has. Each combo has one or more route types.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public enum RouteCombo {

    WATERWAY(new RouteType[] { RouteType.BARGE }),
    RAILWAY(new RouteType[] { RouteType.RAIL }),
    ALL(new RouteType[] { RouteType.BARGE, RouteType.RAIL });

    private RouteType[] routeTypes;

    private RouteCombo(RouteType[] routeTypes) {

        this.routeTypes = routeTypes.clone();
    }

    public RouteType[] getRouteTypes() {

        RouteType[] thisRouteTypes = routeTypes;

        return thisRouteTypes;
    }
}
